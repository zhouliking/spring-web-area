package com.joelly.area.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joelly.area.constants.CommonConstants;
import com.joelly.area.dao.AreaDao;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.service.inner.AreaService;
import com.joelly.area.utils.GsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 国家行政区域(Area)表服务实现类
 *
 * @author makejava
 * @since 2024-06-04 11:01:53
 */
@Slf4j
public class AreaServiceImpl extends ServiceImpl<AreaDao, AreaDO> implements AreaService {
    @Override
    public List<AreaDO> getChildListById(Long parentId) {
        Page<AreaDO> page = new Page<>(0, CommonConstants.SINGLE_QUERY_DB_MAX_SIZE);
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getParentId, parentId);
        Page<AreaDO> pageResult = page(page, queryWrapper);
        return pageResult.getRecords();
    }


    public List<Long> getChildIdListById(Long parentId) {
        return getBaseMapper().selectIdsByParentId(parentId);
    }

    @Override
    @Transactional
    public boolean addArea(AreaDO areaDO) {
        boolean saveResult = save(areaDO);
        int updateChildNumResult = getBaseMapper().addChildrenNum(areaDO.getParentId(), 1);
        log.info("addArea, saveResult: {},updateChildNumResult:{} area: {}", saveResult, updateChildNumResult,
                GsonProvider.obj2Str(areaDO));
        return saveResult && updateChildNumResult > 0;
    }

    @Override
    public boolean deleteAreaSoft(AreaDO areaDO) {
        int deleteAreaNum = getBaseMapper().deleteSoft(areaDO.getId());
        return deleteAreaNum > 0;
    }

    @Override
    public boolean addChildrenNum(long id, int addNum) {
        return getBaseMapper().addChildrenNum(id, addNum) > 0;
    }
}

