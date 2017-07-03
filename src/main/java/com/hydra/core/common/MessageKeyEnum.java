package com.hydra.core.common;

import lombok.Getter;

import java.util.Calendar;

@Getter
public enum MessageKeyEnum {

    EXE("EXE", "任务执行"),
    REXE("REXE","重新执行"),
    RPT("RPT","结果报告");

    private  String code;
    private  String value;

    MessageKeyEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static MessageKeyEnum explain(String code) {

        for (MessageKeyEnum entityNameEnum : MessageKeyEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }

}
