package com.joelly.area.config.bean;

import lombok.Data;

@Data
public class AreaRelationCacheConfig {

    /**
     * 默认缓存 60 分钟
     */
    private int cacheExpireMinutes = 60 * 24;

    /**
     * 最大缓存数量, 目前 1-4级区域约为45000个
     */
    private int maxCacheSize = 30000;

//    /**
//     * 是否应用启动时加载缓存
//     */
//    private boolean startLoadCacheEnable = false;
//
//    /**
//     * 启动加载缓存配置
//     */
//    private StartLoadCacheConfig startLoadCache;
}
