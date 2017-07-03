package com.hydra.core.common;

import lombok.Getter;


@Getter
public enum BooleanEnum {

    TRUE("TRUE", "TRUE"),
    FALSE("FALSE","FALSE");

    private  String code;
    private  String value;

    BooleanEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static BooleanEnum explain(String code) {

        for (BooleanEnum entityNameEnum : BooleanEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }
}
