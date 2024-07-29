package com.joelly.area.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "joelly.area")
@Data
public class AreaProperties {

    /**
     * 是否开启区域组件能力
     */
    private boolean enable;

    /**
     * 是否开启缓存
     */
    private boolean cache;

    /**
     * 区域关系缓存
     * 用途：用户生成区域树，父子层级等
     */
    private AreaRelationCacheConfig relationCache;

    /**
     * 单个区域信息缓存
     * 用途：加速区域信息补全
     */
    private AreaInfoCacheConfig infoCache;
}
