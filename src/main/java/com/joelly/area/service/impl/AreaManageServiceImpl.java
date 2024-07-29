package com.joelly.area.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.JsonObject;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.vo.AreaVO;
import com.joelly.area.exception.AreaException;
import com.joelly.area.file.AreaFileReader;
import com.joelly.area.service.AreaManageService;
import com.joelly.area.service.inner.AreaService;
import com.joelly.area.temp.entity.ProvinceAreaNational2022;
import com.joelly.area.utils.AssertUtils;
import com.joelly.area.utils.GsonProvider;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.joelly.area.constants.CommonConstants.*;


@Slf4j
public class AreaManageServiceImpl implements AreaManageService {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);


    @Autowired
    private AreaService areaService;


    @Override
    public Boolean addArea(AreaVO areaVO) {
        if (areaVO.getCode() == null || areaVO.getId() == null) {
            throw new AreaException("添加的地区必须有地区code");
        }
        Long parentId = areaVO.getParentId();
        AssertUtils.assertNotNull(parentId, "添加的地区必须包含父区域id（parentId）");
        AreaDO parentArea = areaService.getById(parentId);
        AssertUtils.assertNotNull(parentArea, String.format("父区域id（parentId: %s）不存在", parentId));

        AreaDO newAreaDO = new AreaDO();
        BeanUtils.copyProperties(areaVO, newAreaDO);
        fillAreaProperties(newAreaDO, parentArea.getLevel());
        return areaService.addArea(newAreaDO);
    }

    @Override
    public Boolean deleteArea(Long id, String operator) {
        AreaDO areaDO = areaService.getById(id);
        AssertUtils.assertNotNull(areaDO, String.format("区域（id: %s）不存在", areaDO.getId()));
        boolean deleteResult = this.areaService.deleteAreaSoft(areaDO);
        log.info("addArea, operator: {}, deleteResult: {}, id: {}", operator, deleteResult, id);
        return deleteResult;
    }

    @Override
    public Boolean updateById(AreaVO areaVO) {
        // 子区域
        AssertUtils.assertNotNull(areaVO.getId(), "修改区域必须有地区id");
        AreaDO dbAreaDO = areaService.getById(areaVO.getId());
        AssertUtils.assertNotNull(dbAreaDO, String.format("修改的区域（id: %s）不存在", areaVO.getId()));

        AreaDO parentArea = null;
        if (areaVO.getParentId() != null) {
            parentArea = areaService.getById(areaVO.getParentId());
            AssertUtils.assertNotNull(parentArea, String.format("修改的父区域id（parentId: %s）不存在",
                    areaVO.getParentId()));
        } else {
            parentArea = areaService.getById(dbAreaDO.getParentId());
        }
        // update
        boolean refreshChildNames = updateCurrentArea(parentArea, dbAreaDO, areaVO);
        if (refreshChildNames) {
            final int currentLevel = parentArea.getLevel() + 1;
            log.warn("updateById, need refreshChildNames, id:{}, currentLevel: {}", areaVO.getId(), currentLevel);
            executor.submit(() -> {
                refreshChildAreaNameAndId(areaVO.getId(), currentLevel, true, 0);
            });
        }
        return true;
    }

    @Override
    public boolean refreshAllAreaData() {
        LambdaQueryWrapper<AreaDO> pageQueryWrapper = new LambdaQueryWrapper<>();
        pageQueryWrapper.gt(AreaDO::getLevel, 0);
        List<AreaDO> childList = areaService.list(pageQueryWrapper);
        log.info("refreshAreaData totalSize : {}", childList.size());

        Map<Long, AreaDO> map = new HashMap<>();
        for (AreaDO areaDO : childList) {
            map.put(areaDO.getId(), areaDO);
        }
        int count = 0;
        for (AreaDO areaDO : childList) {
            count++;
            Pair<String, String> pair = buildMergeIdName(areaDO);
            // 是否有子节点
            List<Long> childIds = areaService.getChildIdListById(areaDO.getId());
            long size = childIds.size();
            UpdateWrapper<AreaDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",  areaDO.getId()) // WHERE name = 'John'
                    .set("child_num", size)
                    .set("merge_unique_id", pair.getKey());
            boolean result = areaService.update(updateWrapper);
            log.info("{} refreshAreaData, id: {}, size: {}, result: {}, idStr: {}",
                    count, areaDO.getId(), size, result, pair.getKey());
        }
        log.info("---------refreshAllAreaData end -----------");
        return true;
    }

    @Override
    public boolean refreshSingleAreaData(long id, boolean refreshChild) {
        AreaDO areaDO = areaService.getById(id);
        AssertUtils.assertNotNull(areaDO, String.format("需要刷新的区域（id: %s）不存在", id));
        AreaDO parentArea = areaService.getById(areaDO.getParentId());
        AssertUtils.assertNotNull(parentArea, String.format("需要刷新的父区域id（parentId: %s）不存在", areaDO.getParentId()));

        AreaDO newAreaDO = new AreaDO();
        newAreaDO.setId(areaDO.getId());
        fillAreaProperties(newAreaDO, parentArea.getLevel());
        boolean updateResult = areaService.updateById(newAreaDO);
        if (refreshChild) {
            refreshChildAreaNameAndId(id, areaDO.getLevel(), true, 0);
        }
        log.info("refreshSingleAreaData end result: {}, newAreaDO: {}",
                updateResult, GsonProvider.obj2Str(newAreaDO));
        return true;
    }

    @Transactional
    public boolean updateCurrentArea(AreaDO parentArea, AreaDO dbAreaDO, AreaVO areaVO) {
        // 父区域关系有没有变化
        boolean refreshChildNames = false;
        if (areaVO.getParentId() != null
                && dbAreaDO.getParentId() != areaVO.getParentId()) {
            refreshChildNames = true;
            boolean newFatherAddNum = areaService
                    .addChildrenNum(areaVO.getParentId(), 1);
            boolean oldFatherAddNum = areaService
                    .addChildrenNum(dbAreaDO.getParentId(), -1);
            log.info("updateById father change, id: {}, newFatherId: {} {}, oldFatherId: {} {}",
                    areaVO.getId(), areaVO.getParentId(), newFatherAddNum, dbAreaDO.getParentId(), oldFatherAddNum);
        }
        // 修改了区域名字
        if (!Strings.isNullOrEmpty(areaVO.getName())
                && !areaVO.getName().equals(dbAreaDO.getName())) {
            refreshChildNames = true;
        }
        // 更新当前区域信息
        AreaDO newAreaDO = new AreaDO();
        BeanUtils.copyProperties(areaVO, newAreaDO);
        fillAreaProperties(newAreaDO, parentArea.getLevel());
        boolean updateResult = areaService.updateById(newAreaDO);
        log.info("updateCurrentArea result: {}, newAreaDO: {}",
                updateResult, GsonProvider.obj2Str(newAreaDO));
        return refreshChildNames;
    }

    /**
     * 递归的刷新子区域
     *
     * @param id
     * @param thisAreaLevel
     * @param refreshChild
     * @param count
     */
    private void refreshChildAreaNameAndId(long id, int thisAreaLevel, boolean refreshChild, int count) {
        List<AreaDO> areaDOList = areaService.getChildListById(id);
        if (CollectionUtils.isEmpty(areaDOList)) {
            return;
        }
        if (count >= RECURSION_MAX_COUNT) {
            log.warn("recursion times over max_count, id:{}, count: {}", id, count);
            return;
        }
        // 更新
        for (AreaDO child : areaDOList) {
            AreaDO newAreaDO = new AreaDO();
            newAreaDO.setId(child.getId());
            fillAreaProperties(newAreaDO, thisAreaLevel);
            boolean updateResult = areaService.updateById(newAreaDO);
            log.info("refreshChildAreaNameAndId, count:{}, id: {}, updateResult: {}, newAreaDO: {}",
                    count, child.getId(), updateResult, newAreaDO);
            if (refreshChild) {
                refreshChildAreaNameAndId(child.getId(), thisAreaLevel + 1, true, ++count);
            }
        }
    }

    /**
     * 填充区域其他信息
     */
    private void fillAreaProperties(AreaDO currentArea, int parentLevel) {
        /**
         * 行政编码code说明：0=正常；1=缺失；2=父级计算规则不匹配 Integer codeType;
         * todo
         */

        /**
         *  //级别，1-省，2-市，3-县，4-乡，5-村 private Integer level;
         */
        currentArea.setLevel(++parentLevel);

        /**
         * 区域全称，如杭州市的全称为 中国,浙江省,杭州市 private String mergeName;
         *  父id到当前id列表 String mergeUniqueId
         */
        Pair<String, String> pair = buildMergeIdName(currentArea);
        currentArea.setMergeUniqueId(pair.getKey());
        currentArea.setMergeName(pair.getValue());
        List<Long> childIds= areaService.getChildIdListById(currentArea.getId());
        /**
         * 子节点数量 Long childNum
         */
        currentArea.setChildNum(CollectionUtils.size(childIds));
    }


    /**
     * 生成名字与id(merge_unique_id)混合字段
     * @param areaDO
     * @return
     */
    private Pair<String, String> buildMergeIdName(AreaDO areaDO) {
        List<AreaDO> listArea = new ArrayList<>();
        listArea.add(areaDO);

        // 遍历
        AreaDO thisArea = areaDO;
        do {
            long parentId = thisArea.getParentId();
            if (parentId == 0) {
                break;
            }
            thisArea = areaService.getById(parentId);
            listArea.add(thisArea);
        } while (thisArea != null);

        List<AreaDO> reversedList = new ArrayList<>(listArea);
        Collections.reverse(reversedList);
        List<String> names = reversedList.stream()
                .map(AreaDO::getName)
                .collect(Collectors.toList());
        List<String> ids = reversedList.stream()
                .map(area -> String.valueOf(area.getId()))
                .collect(Collectors.toList());

        String idStr = PARENT_0_CODE + AREA_DELIMITER + String.join(AREA_DELIMITER, ids);
        String mameStr = PARENT_0_NAME + AREA_DELIMITER + String.join(AREA_DELIMITER, names);

        log.info("---> idSize : {}, level: {}, idStr: {}, mameStr: {}",
                listArea.size(), areaDO.getLevel(), idStr, mameStr);
        return new Pair<>(idStr, mameStr);
    }

}
