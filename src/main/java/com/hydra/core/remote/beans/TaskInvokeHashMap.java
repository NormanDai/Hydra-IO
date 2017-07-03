package com.hydra.core.remote.beans;


import com.hydra.core.common.BooleanEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class TaskInvokeHashMap {

    private ConcurrentHashMap<String,RemoteTaskMessage> taskMap = new ConcurrentHashMap<String, RemoteTaskMessage>();

    private static TaskInvokeHashMap instance;

    private TaskInvokeHashMap(){

    }

    public static TaskInvokeHashMap getInstance(){
        if (instance == null) {
            synchronized (TaskInvokeHashMap.class) {
                if (instance == null) {
                    instance = new TaskInvokeHashMap();
                }
            }
        }
        return instance;
    }

    public ConcurrentHashMap<String,RemoteTaskMessage> getMap(){
        return this.taskMap;
    }


    public void addTask(String msgKey,RemoteTaskMessage message){
        this.taskMap.put(msgKey,message);
    }

    public RemoteTaskMessage getMessage(String msgKey){
        return this.taskMap.get(msgKey);
    }

    public RemoteTaskMessage getRespMessage(String msgKey,long timeOut){
        long begTime = new Date().getTime();
        long outTime = begTime + timeOut;
        TaskInvokeAskResponse response = null;
        RemoteTaskMessage message = this.taskMap.get(msgKey);
        while (true){
            response = message.getResponse();
            if(null != response){
                break;
            }
            if (outTime >= new Date().getTime()){
                response = new TaskInvokeAskResponse();
                response.setSuccess(BooleanEnum.FALSE.getCode());
                response.setErrorContext("获取回复超时");
                message.setResponse(response);
               break;
            }
        }
        return message;
    }






}
