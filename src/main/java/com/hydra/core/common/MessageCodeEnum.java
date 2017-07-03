package com.hydra.core.common;

import lombok.Getter;

@Getter
public enum MessageCodeEnum {

    ASK("ASK", "询问"),
    ACK("ACK","应答"),
    REJ("REJ","拒绝"),
    NAK("NAK","无应答");

    private  String code;
    private  String value;

    MessageCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static MessageCodeEnum explain(String code) {

        for (MessageCodeEnum entityNameEnum : MessageCodeEnum.values()) {
            if (entityNameEnum.code.equals(code)) {
                return entityNameEnum;
            }
        }
        return null;
    }

}
