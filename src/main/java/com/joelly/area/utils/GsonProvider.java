package com.joelly.area.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.joelly.area.dao.objs.AreaDO;
import com.joelly.area.entity.Area;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GsonProvider {
    private static final Gson GSON = new Gson();

    private GsonProvider() {
        // 私有构造函数防止实例化
    }

    private static Gson getInstance() {
        return GSON;
    }


    /**
     * 字符串转换
     *
     * @param jsonString
     * @param type : new TypeToken<T>(){}
     * @return
     * @param <T>
     */
    public static <T> T str2Obj(String jsonString, TypeToken<T> type) {
        T map = getInstance().fromJson(jsonString, type.getType());
        return map;
    }

    public static <T> T str2Obj(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return (T) gson.fromJson(jsonString, clazz);
    }

    public static String obj2Str(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return getInstance().toJson(obj);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();

        map.put("a", Arrays.asList("A", "B"));
        map.put("b", 2222);
        Map<String, Object> map2 = str2Obj(obj2Str(map), new TypeToken<Map<String, Object>>() {});
        System.out.println(map2);


        Area area = new Area();
//        area.setId(10000L);
//        System.out.println(str2Obj(obj2Str(area), Area.class));

        AreaDO areaDO = new AreaDO();
        areaDO.setId(100001L);
        BeanUtils.copyProperties(areaDO, area);

        System.out.println(obj2Str(area));
    }
}
