package com.joelly.area.convert;

import com.google.common.reflect.TypeToken;
import com.joelly.area.constants.CodeType;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.Area;
import com.joelly.area.utils.GsonProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AreaConvert {


    public static Area convert2Area(AreaDO areaDO) {
        if (areaDO == null) {
            return null;
        }
        Area area = new Area();
        BeanUtils.copyProperties(areaDO, area);
        if (!Strings.isBlank(areaDO.getExtInfo())) {
            Map<String, Object> extInfo = GsonProvider
                    .str2Obj(areaDO.getExtInfo(),
                            new TypeToken<Map<String, Object>>(){});
            area.setExtInfo(extInfo);
        }
        return area;
    }

    public static List<Area> convert2AreaList(List<AreaDO> areaDOList) {
        if (CollectionUtils.isEmpty(areaDOList)) {
            return new ArrayList<>();
        }
        return areaDOList.stream()
                .map(AreaConvert::convert2Area)
                .collect(Collectors.toList());
    }

    private static int codeType(Long code, int level, Long parentCode) {
        if (code == null) {
            return CodeType.NO_CODE;
        } else {
            //前3级不会出现这个问题，从第四级开始可能有不匹配现象
            if (level > 3) {
                Long thisCode = code;
                Long computedCode = (thisCode / 1000) * 1000;
                if (!computedCode.equals(parentCode)) {
                    return CodeType.NOT_MATCH;
                }
            }
        }
        return CodeType.NORMAL;
    }

}
