package com.joelly.area.service.inner;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joelly.area.dao.objs.AreaDO;

import java.util.List;

/**
 * 国家行政区域(Area)表服务接口
 *
 * @author makejava
 * @since 2024-06-04 11:01:53
 */
public interface AreaService extends IService<AreaDO> {
    /**
     * 根据父级唯一ID获取直接子区域列表
     *
     * @param parentId 父级唯一ID
     * @return 辖区列表
     */
    List<AreaDO> getChildListById(Long parentId);

    /**
     * 根据父级唯一ID获取直接子区域id
     * @param parentId
     * @return
     */
    List<Long> getChildIdListById(Long parentId);

    /**
     * 新增地区
     * @param areaDO
     * @return
     */
    boolean addArea(AreaDO areaDO);


    /**
     * 删除地区
     * @param areaDO
     * @return
     */
    boolean deleteAreaSoft(AreaDO areaDO);

    /**
     * 修改子节点数据
     * @return
     */
    boolean addChildrenNum(long id, int addNum);

}

