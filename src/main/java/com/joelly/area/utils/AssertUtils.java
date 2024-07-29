package com.joelly.area.utils;

import com.joelly.area.exception.AreaException;

import java.util.Objects;

public class AssertUtils {

    public static <T> void assertNotNull(T t, String msg) {
        if (Objects.isNull(t)) {
            throw new AreaException(msg);
        }
    }
}
