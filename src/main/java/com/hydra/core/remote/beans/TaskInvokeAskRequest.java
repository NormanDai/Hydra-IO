package com.hydra.core.remote.beans;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskInvokeAskRequest  implements java.io.Serializable{

    private String messageKey;

    private String messageCode;

    private String messageContext;
}
