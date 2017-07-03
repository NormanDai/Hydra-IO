package com.hydra.core.common;

import lombok.Getter;

@Getter
public enum  ExpressionMeasureEnum {

    STANDARD("STANDARD","yyyy/MM/dd HH:mm:ss"),
    YEAR("YEAR","MM/dd HH:mm:ss"),
    MONTH("MONTH","dd HH:mm:ss"),
    DAY("DAY","HH:mm:ss"),
    HOUR("HOUR","mm:ss"),
    MINUTE("MINUTE","ss"),
    SECOND("SECOND","ss");

    private  String code;
    private  String value;

    ExpressionMeasureEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
