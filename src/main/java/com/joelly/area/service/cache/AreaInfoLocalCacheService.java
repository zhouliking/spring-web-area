package com.joelly.area.service.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.joelly.area.config.bean.AreaInfoCacheConfig;
import com.joelly.area.convert.AreaConvert;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.Area;
import com.joelly.area.service.inner.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AreaInfoLocalCacheService {

    /**
     * 默认缓存个数
     */
    private static final int DEFAULT_MAX_CACHE_NUMBER = 30000;

    /**
     * 默认缓存时长(分钟)
     */
    private static final int DEFAULT_PARENT_CHILD_ID_RELATION_DURATION = 60;
    private LoadingCache<Long, Area> areaLoadingCache = null;

    public AreaInfoLocalCacheService(AreaService areaService, AreaInfoCacheConfig areaInfoCacheConfig) {
        long maxCacheSize = DEFAULT_MAX_CACHE_NUMBER;
        long duration = DEFAULT_PARENT_CHILD_ID_RELATION_DURATION;
        if (areaInfoCacheConfig != null) {
            maxCacheSize = areaInfoCacheConfig.getMaxCacheSize() > 0 ? areaInfoCacheConfig.getMaxCacheSize() : maxCacheSize;
            duration = areaInfoCacheConfig.getCacheExpireMinutes() > 0 ? areaInfoCacheConfig.getCacheExpireMinutes() : duration;
        }
        areaLoadingCache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheSize)
                .expireAfterWrite(duration, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, Area>() {
                    @Override
                    public Area load(Long thisAreaId) throws Exception {
                        try {
                            AreaDO areaDO = areaService.getById(thisAreaId);
//                            boolean hasChild = areaService.hasChildArea(thisAreaId);
                            log.info("load AreaInfo cache, cacheSize:{}, thisAreaId: {}, area: {}",
                                    areaLoadingCache.size(), thisAreaId, areaDO);
                            Area area = AreaConvert.convert2Area(areaDO);
                            return area;
                        } catch (Exception e) {
                            // 处理异常，例如记录日志或返回默认值
                            log.error("Error loading AreaInfo cache, parentId: {}", thisAreaId, e);
                            return null; // 或抛出特定的异常
                        }
                    }
                });
        log.info("AreaInfoLocalCache Create success! maxCacheSize:{}, duration:{}", maxCacheSize, duration);
    }

    public Area getFromCache(Long thisAreaId) throws ExecutionException {
        if (areaLoadingCache == null || thisAreaId == null) {
            return null;
        }
        return areaLoadingCache.get(thisAreaId);
    }

    public List<Area> getFromCache(List<Long> areaIds) throws ExecutionException {
        if (areaLoadingCache == null || CollectionUtils.isEmpty(areaIds)) {
            return new ArrayList<>();
        }
        List<Area> list = new ArrayList<>();
        for (Long id : areaIds) {
            list.add(areaLoadingCache.get(id));
        }
        return list;
    }
}
