package com.joelly.area.enums;

import lombok.Getter;

public enum ProvinceCodeEnum {

    BEIJING("北京市", "110000000000"),
    TIANJIN("天津市", "120000000000"),
    HEBEI("河北省", "130000000000"),
    SHANXI("山西省", "140000000000"),
    INNER_MONGOLIA("内蒙古自治区", "150000000000"),
    LIAONING("辽宁省", "210000000000"),
    JILIN("吉林省", "220000000000"),
    HEILONGJIANG("黑龙江省", "230000000000"),
    SHANGHAI("上海市", "310000000000"),
    JIANGSU("江苏省", "320000000000"),
    ZHEJIANG("浙江省", "330000000000"),
    ANHUI("安徽省", "340000000000"),
    FUJIAN("福建省", "350000000000"),
    JIANGXI("江西省", "360000000000"),
    SHANDONG("山东省", "370000000000"),
    HENAN("河南省", "410000000000"),
    HUBEI("湖北省", "420000000000"),
    HUNAN("湖南省", "430000000000"),
    GUANGDONG("广东省", "440000000000"),
    GUANGXIZHUANG("广西壮族自治区", "450000000000"),
    HAINAN("海南省", "460000000000"),
    CHONGQING("重庆市", "500000000000"),
    SICHUAN("四川省", "510000000000"),
    GUIZHOU("贵州省", "520000000000"),
    YUNNAN("云南省", "530000000000"),
    XIZANG("西藏自治区", "540000000000"),
    SHAANXI("陕西省", "610000000000"),
    GANSU("甘肃省", "620000000000"),
    QINGHAI("青海省", "630000000000"),
    NINGXIA("宁夏回族自治区", "640000000000"),
    XINJIANG("新疆维吾尔自治区", "650000000000");

    @Getter
    private final String name;

    @Getter
    private final String code;

    ProvinceCodeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
