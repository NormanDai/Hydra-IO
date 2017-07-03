package com.hydra.core.remote;


import com.hydra.core.common.utils.StringUtil;
import com.hydra.core.remote.jgroups.JGroupsRemoteService;
import lombok.Setter;


public class RemoteServiceHelper implements Runnable{

    public static final String ZOOKEEPER = "zookeeper";
    public static final String JGROUPS = "jgroups";

    private String compModel = JGROUPS;

    public RemoteServiceHelper(String compModel){

        if(StringUtil.isNotEmpty(compModel) && compModel.equals(ZOOKEEPER)){
            compModel = ZOOKEEPER;
        }

    }


    public void run() {
        /** 按照配置启动不同的分布式组件*/

        if(compModel.equals(JGROUPS)){
            /**1. 启动Jgroups 分布式通信组件*/
            JGroupsRemoteService.getInstance();
        }

        if(compModel.equals(ZOOKEEPER)){

        }

    }
}
