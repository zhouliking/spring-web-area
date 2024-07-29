package com.joelly.area.temp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joelly.area.dao.ProvinceAreaNational2022Dao;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.service.inner.AreaService;
import com.joelly.area.temp.entity.ProvinceAreaNational2022;
import com.joelly.area.utils.GsonProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

//@RestController
public class TempController extends ServiceImpl<ProvinceAreaNational2022Dao, ProvinceAreaNational2022> {

    @Autowired
    private AreaService areaService;

    @GetMapping("initFromTable")
    private boolean initFromTable() throws IOException {
        long count = count();
        System.out.println("----> " + count);


//        LambdaQueryWrapper<ProvinceAreaNational2022> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ProvinceAreaNational2022::getLevel, 1);
//        List<ProvinceAreaNational2022> level1 = provinceAreaNational2022Service.list(queryWrapper);
//        System.out.println("-----------> " + level1.size());


        LambdaQueryWrapper<ProvinceAreaNational2022> pageQueryWrapper = new LambdaQueryWrapper<>();
        pageQueryWrapper.gt(ProvinceAreaNational2022::getLevel, 0);
        List<ProvinceAreaNational2022> childList = list(pageQueryWrapper);
        System.out.println("-----------> " + childList.size());

        HashMap<Long, Long> map = new HashMap<>();
        for(ProvinceAreaNational2022 areaNational2022 : childList) {
            map.put(areaNational2022.getUniqueId(), areaNational2022.getCode());
        }

        int num = 0;
        for (ProvinceAreaNational2022 areaNational2022 : childList) {
            //rateLimiter.acquire();
            AreaDO areaDO = covert(map, areaNational2022);
            //areaService.save(area);
            num++;
//            if (num>10) {
//                break;
//            }
        }
        return false;
    }


    private static AreaDO covert(HashMap<Long, Long> map, ProvinceAreaNational2022  provinceAreaNational2022) {
        AreaDO areaDO = new AreaDO();
        BeanUtils.copyProperties(provinceAreaNational2022, areaDO);

        areaDO.setDisplayOrder(provinceAreaNational2022.getShowOrder());
        areaDO.setCodeType(provinceAreaNational2022.getIsCustomCode());

//        area.setId(provinceAreaNational2022.getUniqueId());
//        area.setParentId(provinceAreaNational2022.getParentUniqueId());
        areaDO.setId(provinceAreaNational2022.getCode());
        Long parentCode = map.get(provinceAreaNational2022.getParentUniqueId());
        System.out.println("----------->" + provinceAreaNational2022.getCode() + " " + parentCode);
        areaDO.setParentId(parentCode == null ? 0 : parentCode);
        System.out.println("AREA: " + GsonProvider.obj2Str(areaDO));
        System.out.println("OLD:  " + GsonProvider.obj2Str(provinceAreaNational2022));
        return areaDO;
    }
}
