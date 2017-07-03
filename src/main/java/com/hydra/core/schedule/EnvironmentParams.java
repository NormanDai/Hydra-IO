package com.hydra.core.schedule;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class EnvironmentParams implements Serializable{

    private  String jobName;
    private  int invokeIndex;
    private  int totalInvoke;
    private ConcurrentHashMap<String,Object> invokeParams;

}
