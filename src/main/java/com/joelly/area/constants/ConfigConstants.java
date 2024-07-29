package com.joelly.area.constants;

public class ConfigConstants {

    /**
     * 行政区划数据类型
     */
    public static final Integer AREA_DATA = 0;

    /**
     * 行政区划省范围
     */
    public static final String KEY_AREA_SCOPE = "province_area_scope";
    /**
     * 行政区划数据是否在线同步
     */
    public static final String KEY_AREA_SYNC_ONLINE = "province_area_sync_online";
    /**
     * 行政区划数据是否启用
     */
    public static final String KEY_AREA_ENABLE = "province_area_enable";
    /**
     * 行政区划数据是否使用redis
     */
    public static final String KEY_AREA_USE_REDIS = "province_area_use_redis";
    /**
     * 行政区划数据的redisTemplate bean名称
     */
    public static final String KEY_AREA_REDIS_TEMPLATE_BEAN_NAME = "province_area_redis_template_bean_name";
}
