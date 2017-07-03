package com.hydra.core.common;

import lombok.Getter;

@Getter
public enum TimingPartingEnum {

    YEAR("YEAR","yyyy"),
    MONTH("MONTH","yyyy/MM"),
    DAY("DAY","yyyy/MM/dd"),
    HOUR("HOUR","yyyy/MM/dd HH"),
    MINUTE("MINUTE","yyyy/MM/dd HH:mm"),
    SECOND("SECOND","yyyy/MM/dd HH:mm:ss");

    private  String code;
    private  String value;

    TimingPartingEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public static TimingPartingEnum explain(String code) {

        for (TimingPartingEnum entityNameEnum : TimingPartingEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }


}
