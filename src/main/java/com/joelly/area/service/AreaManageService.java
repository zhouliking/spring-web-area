package com.joelly.area.service;

import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.vo.AreaVO;

import java.io.IOException;

public interface AreaManageService {


    /**
     * 增加单个区域
     * @param areaVO
     * @return
     */
    Boolean addArea(AreaVO areaVO);

    /**
     * 删除区域
     * @param id
     * @param operator
     * @return
     */
    Boolean deleteArea(Long id, String operator);


    /**
     * 更新一个行政区域，注意只会更新名称、经纬度
     * @param areaVO
     * @return 是否更新成功
     */
    Boolean updateById(AreaVO areaVO);


    /**
     * 从文件初始化数据
     * @return
     */
    boolean refreshAllAreaData();

    /**
     * 刷新单个区域的子区域信息
     * @param id
     * @param refreshChild
     */
    boolean refreshSingleAreaData(long id, boolean refreshChild);
}
