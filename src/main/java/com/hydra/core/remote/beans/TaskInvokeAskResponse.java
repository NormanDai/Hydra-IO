package com.hydra.core.remote.beans;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TaskInvokeAskResponse  implements java.io.Serializable{

    private String messageKey;

    private String messageCode;

    private String messageContext;

    private String success;

    private String errorContext;

}
