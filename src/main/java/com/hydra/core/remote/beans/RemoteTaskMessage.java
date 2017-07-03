package com.hydra.core.remote.beans;

import com.hydra.core.schedule.EnvironmentParams;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jgroups.util.Streamable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Serializable;

@Setter
@Getter
@ToString
public  class RemoteTaskMessage implements Serializable{

    private String messageKey;

    private TaskInvokeAskRequest request;

    private TaskInvokeAskResponse response;

    private EnvironmentParams params;

}
