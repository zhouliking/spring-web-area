package com.joelly.area.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joelly.area.convert.AreaConvert;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.Area;
import com.joelly.area.service.AreaQueryService;
import com.joelly.area.service.cache.LocalCacheHelper;
import com.joelly.area.service.inner.AreaService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AreaQueryServiceImpl implements AreaQueryService {

    /**
     * 数据库直接读取
     */
    private AreaService areaService;

    public AreaQueryServiceImpl(AreaService areaService) {
        this.areaService = areaService;
    }


    @Override
    public Page<AreaDO> page(Page<AreaDO> page, AreaDO areaDO) {
        Page<AreaDO> pageDO = this.areaService.page(page, new QueryWrapper<>(areaDO));
        return pageDO;
    }

    @Override
    public List<Area> getChildListById(Long parentId) {
        if (LocalCacheHelper.isOpen()) {
            log.info("getChildListById read from cache, parentId: {}", parentId);
            try {
                List<Long> listIds = LocalCacheHelper.relationLocalCacheService().getChildIdFromCache(parentId);
                return LocalCacheHelper.areaInfoLocalCacheService().getFromCache(listIds);
            } catch (ExecutionException e) {
                log.error("getChildListById, read local cache error, parentId: {}", parentId, e);
            }
        }
        log.info("getChildListById read from db, parentId: {}", parentId);
        List<AreaDO> list = areaService.getChildListById(parentId);
        return AreaConvert.convert2AreaList(list);
    }

    @Override
    public List<Long> getChildIdListById(Long parentId) {
        if (LocalCacheHelper.isOpen()) {
            log.info("getChildIdListById read from cache, parentId: {}", parentId);
            try {
                List<Long> listIds = LocalCacheHelper.relationLocalCacheService().getChildIdFromCache(parentId);
                return listIds;
            } catch (ExecutionException e) {
                log.error("getChildIdListById, read local cache error, parentId: {}", parentId, e);
            }
        }
        log.info("getChildIdListById read from db, parentId: {}", parentId);
        return areaService.getChildIdListById(parentId);
    }

    @Override
    public AreaDO getById(Long id) {
        return areaService.getById(id);
    }
}
