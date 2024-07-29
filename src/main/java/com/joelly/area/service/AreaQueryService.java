package com.joelly.area.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.Area;

import java.util.List;

public interface AreaQueryService {

    /**
     * 分页查询
     * @param page
     * @param areaDO
     * @return
     */
    Page<AreaDO> page(Page<AreaDO> page, AreaDO areaDO);

    /**
     * 根据父id查询子区域
     * @param parentId
     * @return
     */
    List<Area> getChildListById(Long parentId);

    /**
     * 根据父id查询子区域
     * @param parentId
     * @return
     */
    List<Long> getChildIdListById(Long parentId);

    /**
     * 主键查询
     * @param id
     * @return
     */
    AreaDO getById(Long id);
}
