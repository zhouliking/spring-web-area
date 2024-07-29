package com.joelly.area.temp.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;

/**
 * 行政区域全国数据表(ProvinceAreaNational2022)表实体类
 *
 * @author makejava
 * @since 2024-06-05 16:15:50
 */
@SuppressWarnings("serial")
@TableName("province_area_national_2022")
public class ProvinceAreaNational2022 extends Model<ProvinceAreaNational2022> {
//主键
    private Long id;
//内部唯一编码
    private Long uniqueId;
//父级唯一编码
    private Long parentUniqueId;
//地区名称
    private String name;
//地区行政编码
    private Long code;

    private Integer showOrder;
//全路径地区名
    private String mergeName;
//层级
    private Integer level;
//经度
    private String lon;
//纬度
    private String lat;
//数据创建时间
    private Date createTime;
//数据更新时间
    private Date updateTime;
//是否内部行政编码，默认否
    private Integer isCustomCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getParentUniqueId() {
        return parentUniqueId;
    }

    public void setParentUniqueId(Long parentUniqueId) {
        this.parentUniqueId = parentUniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsCustomCode() {
        return isCustomCode;
    }

    public void setIsCustomCode(Integer isCustomCode) {
        this.isCustomCode = isCustomCode;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

