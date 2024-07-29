package com.joelly.area.config.bean;

import lombok.Data;

@Data
public class StartLoadCacheConfig {

    /**
     * 同步还是异步加载，默认值为false异步多线程加载
     */
    private boolean synLoad = false;

    /**
     * 启动加载区域配置
     */
    private int asyLoadCacheNum = 10000;
}
