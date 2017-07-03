package com.hydra.core.common;

import lombok.Getter;

@Getter
public enum TimingSplitLineEnum {
    //yyyy/MM/dd HH:mm:ss
    YEAR("YEAR","/"),
    MONTH("MONTH","/"),
    DAY("DAY"," "),
    HOUR("HOUR",":"),
    MINUTE("MINUTE",":"),
    SECOND("SECOND","");

    private  String code;
    private  String value;

    TimingSplitLineEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public static TimingSplitLineEnum explain(String code) {

        for (TimingSplitLineEnum entityNameEnum : TimingSplitLineEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }


}
