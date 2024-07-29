package com.joelly.area.service.cache;

import com.joelly.area.config.bean.AreaProperties;
import com.joelly.area.utils.ApplicationContextUtils;

public class LocalCacheHelper {
    private static Boolean cacheOpen;
    private static AreaRelationLocalCacheService relationLocalCacheService;

    private static AreaInfoLocalCacheService areaInfoLocalCacheService;
    public static synchronized boolean isOpen() {
        if (cacheOpen != null) {
            return cacheOpen;
        }
        AreaProperties areaProperties = ApplicationContextUtils.getBean(AreaProperties.class);
        cacheOpen = areaProperties.isCache();
        return cacheOpen;
    }

    public static synchronized AreaRelationLocalCacheService relationLocalCacheService() {
        if (relationLocalCacheService != null) {
            return relationLocalCacheService;
        }
        AreaRelationLocalCacheService localCacheService = ApplicationContextUtils.getBean(AreaRelationLocalCacheService.class);
        relationLocalCacheService = localCacheService;
        return localCacheService;
    }

    public static synchronized AreaInfoLocalCacheService areaInfoLocalCacheService() {
        if (areaInfoLocalCacheService != null) {
            return areaInfoLocalCacheService;
        }
        AreaInfoLocalCacheService infoLocalCacheService = ApplicationContextUtils.getBean(AreaInfoLocalCacheService.class);
        areaInfoLocalCacheService = infoLocalCacheService;
        return infoLocalCacheService;
    }

}
