package com.joelly.area.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joelly.area.controller.base.BaseController;
import com.joelly.area.controller.respose.R;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.vo.AreaVO;
import com.joelly.area.service.AreaManageService;
import com.joelly.area.service.AreaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 国家行政区域(Area)表控制层
 *
 * @author makejava
 * @since 2024-06-04 11:01:50
 */
@RestController
@ConditionalOnProperty(name = "joelly.area.enable", havingValue = "true")
public class AreaController extends BaseController {

    @Autowired
    private AreaManageService areaManageService;

    @Autowired
    private AreaQueryService areaQueryService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param areaDO 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<AreaDO> page, AreaDO areaDO) {
        return success(areaQueryService.page(page, areaDO));
    }

    /**
     * 更加区域id查询子区域
     * @param parentId
     * @return
     */
    @GetMapping("queryChildArea")
    public R queryChildArea(Long parentId) {
        return success(areaQueryService.getChildListById(parentId));
    }

    @GetMapping("queryChildAreaId")
    public R queryChildIdListById(Long parentId) {
        return success(areaQueryService.getChildIdListById(parentId));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Long id) {
        return success(this.areaQueryService.getById(id));
    }

    /**
     * 新增单条区域数据
     *
     * @param areaVO 实体对象
     * @return 新增结果
     */
    @PostMapping("add")
    public R insert(@RequestBody AreaVO areaVO) {
        return success(this.areaManageService.addArea(areaVO));
    }

    /**
     * 修改数据
     *
     * @param areaVO 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    public R update(@RequestBody AreaVO areaVO) {
        return success(this.areaManageService.updateById(areaVO));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("id") Long id, @RequestParam("operator") String operator) {
        return success(areaManageService.deleteArea(id, operator));
    }

    @PostMapping("refreshSingleAreaData")
    public R refreshSingleAreaData(@RequestParam("id") long id, @RequestParam("refreshChild") boolean refreshChild) {
        return success(this.areaManageService.refreshSingleAreaData(id, refreshChild));
    }

    /**
     * 刷新所有数据预计 65万数据预计3小时
     * @return
     * @throws IOException
     */
    @PostMapping("refreshAreaData")
    public R refreshAreaData() throws IOException {
        return success(this.areaManageService.refreshAllAreaData());
    }


}

