package com.joelly.area.dao.objs;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家行政区域(Area)表实体类
 *
 * @author makejava
 * @since 2024-06-04 11:01:52
 */
@SuppressWarnings("serial")
@Data
@TableName("hx_area")
public class AreaDO extends Model<AreaDO> {
    //ID
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    //上级ID
    private Long parentId;
    //行政编码code
    private Long code;
    //行政编码code说明：0=正常；1=缺失；2=父级计算规则不匹配
    private Integer codeType;
    //地区名称
    private String name;
    //地区名称别名
    private String aliasName;
    //级别，1-省，2-市，3-县，4-乡，5-村
    private Integer level;
    //同级别展示顺序
    private Integer displayOrder;
    //经度
    private String lon;
    //纬度
    private String lat;
    //区域全称，如杭州市的全称为 中国,浙江省,杭州市
    private String mergeName;
    //父id到当前id列表
    private String mergeUniqueId;
    //扩展信息
    private String extInfo;
    //子节点数量
    private Integer childNum;

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

