package com.joelly.area.service.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.joelly.area.config.bean.AreaRelationCacheConfig;
import com.joelly.area.service.inner.AreaService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AreaRelationLocalCacheService {

    /**
     * 默认缓存个数
     */
    private static final int DEFAULT_MAX_CACHE_NUMBER = 30000;

    /**
     * 默认缓存时长(分钟)
     */
    private static final int DEFAULT_PARENT_CHILD_ID_RELATION_DURATION = 60;
    private LoadingCache<Long, List<Long>> parentChildIdRelationCache = null;

    public AreaRelationLocalCacheService(AreaService areaService, AreaRelationCacheConfig relationCacheConfig) {
        long maxCacheSize = DEFAULT_MAX_CACHE_NUMBER;
        long duration = DEFAULT_PARENT_CHILD_ID_RELATION_DURATION;
        if (relationCacheConfig != null) {
            maxCacheSize = relationCacheConfig.getMaxCacheSize() > 0 ? relationCacheConfig.getMaxCacheSize() : maxCacheSize;
            duration = relationCacheConfig.getCacheExpireMinutes() > 0 ? relationCacheConfig.getCacheExpireMinutes() : duration;
        }
        parentChildIdRelationCache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheSize)
                .expireAfterWrite(duration, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, List<Long>>() {
                    @Override
                    public List<Long> load(Long parentId) throws Exception {
                        try {
                            List<Long> childIds = areaService.getChildIdListById(parentId);
                            if (childIds == null) {
                                childIds = Collections.emptyList();
                            }
                            log.info("load cache PARENT_CHILD_ID_RELATION, parentId: {}, size: {}",
                                    parentId, childIds.size());
                            return childIds;
                        } catch (Exception e) {
                            // 处理异常，例如记录日志或返回默认值
                            log.error("Error loading cache PARENT_CHILD_ID_RELATION, parentId: {}", parentId, e);
                            return Collections.emptyList(); // 或抛出特定的异常
                        }
                    }
                });
        log.info("AreaRelationLocalCache Create Success! maxCacheSize:{}, duration:{}", maxCacheSize, duration);
    }

    public List<Long> getChildIdFromCache(Long parentId) throws ExecutionException{
        if (parentChildIdRelationCache == null) {
            return new ArrayList<>();
        }
        return parentChildIdRelationCache.get(parentId);
    }

}
