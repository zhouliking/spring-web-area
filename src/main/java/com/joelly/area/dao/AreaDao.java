package com.joelly.area.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joelly.area.dao.objs.AreaDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 国家行政区域(Area)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-04 11:08:48
 */
//@Mapper
public interface AreaDao extends BaseMapper<AreaDO> {

    @Select("SELECT id FROM hx_area WHERE parent_id = #{parentId}")
    List<Long> selectIdsByParentId(Long parentId);

    @Select("UPDATE hx_area SET child_num = child_num + #{addNum} WHERE id = #{id}")
    int addChildrenNum(@Param("id") long id, @Param("addNum") int addNum);

    @Select("UPDATE hx_area SET delete = 2 WHERE id = #{id}")
    int deleteSoft(long id);

    @Select("UPDATE hx_area SET delete = 1 WHERE id = #{id}")
    int enableArea(long id);

}

