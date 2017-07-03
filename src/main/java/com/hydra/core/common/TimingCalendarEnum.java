package com.hydra.core.common;

import lombok.Getter;

import java.util.Calendar;

@Getter
public enum TimingCalendarEnum {

    YEAR("YEAR", Calendar.YEAR),
    MONTH("MONTH",Calendar.MONTH),
    DAY("DAY",Calendar.DAY_OF_MONTH),
    HOUR("HOUR",Calendar.HOUR),
    MINUTE("MINUTE",Calendar.MINUTE),
    SECOND("SECOND",Calendar.SECOND);

    private  String code;
    private  int value;

    TimingCalendarEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public static TimingCalendarEnum explain(String code) {

        for (TimingCalendarEnum entityNameEnum : TimingCalendarEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }

}
