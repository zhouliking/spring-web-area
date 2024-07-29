package com.joelly.area.enums;

import lombok.Getter;

/**
 * 区域级别：0-中国，1-省，2-市，3-县，4-乡，5-村
 */
public enum AreaLevelEnum {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5),
    ;
    @Getter
    private int level;

    AreaLevelEnum(int level) {
        this.level = level;
    }
}
