package com.joelly.area.entity.vo;

import lombok.Data;

@Data
public class AreaVO {
    //ID
    private Long id;
    //上级ID
    private Long parentId;
    //行政编码code
    private Long code;
    //地区名称
    private String name;
    //地区名称别名
    private String aliasName;
    //同级别展示顺序
    private Integer displayOrder;
    //经度
    private String lon;
    //纬度
    private String lat;
    //扩展信息
    private String extInfo;
}
