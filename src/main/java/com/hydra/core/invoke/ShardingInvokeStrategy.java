package com.hydra.core.invoke;


import com.hydra.core.common.DistributedStrategyEnum;
import com.hydra.core.common.MessageCodeEnum;
import com.hydra.core.common.MessageKeyEnum;
import com.hydra.core.remote.RemoteAddress;
import com.hydra.core.remote.beans.RemoteTaskMessage;
import com.hydra.core.remote.beans.TaskInvokeAskRequest;
import com.hydra.core.remote.jgroups.JGroupsRemoteService;
import com.hydra.core.remote.jgroups.JGroupsRemoteTaskAdapter;
import com.hydra.core.schedule.EnvironmentParams;
import com.hydra.core.wrapper.DistributedWrapper;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class ShardingInvokeStrategy implements InvokeStrategy{



    public void invoke(String taskName, DistributedWrapper distributedWrapper) {

        JGroupsRemoteService remoteService = JGroupsRemoteService.getInstance();

        if(remoteService.isMaster()){
            List<RemoteAddress> groupsMember = remoteService.getGroupsMember();
            int invokerNumber = distributedWrapper.getNumber();
            if(invokerNumber <= 0 ){
                throw new RuntimeException("invalid execute DistributedWrapper.number " + invokerNumber);
            }
            int[] invokeIndex = allocateInvokeIndex(groupsMember.size(), invokerNumber);

            for (int i = 0; i < invokeIndex.length ; i++) {

                EnvironmentParams params = new EnvironmentParams();
                params.setInvokeIndex(i);
                params.setJobName(taskName);
                params.setTotalInvoke(distributedWrapper.getNumber());

                RemoteAddress remoteAddress = groupsMember.get(invokeIndex[i]);
                new JGroupsRemoteTaskAdapter().process(taskName,remoteAddress,groupsMember,params);

            }
        }

    }


    private  int[] allocateInvokeIndex(int nodeNum,int invokeNum){
        // 0 1 2 0 1 2 0 1
        int[] invokeIndexs = new int[invokeNum];
        // 0 1 2
        int[] nodeArr = new int[nodeNum];
        int j = 0;
        for (int i = 0; i < invokeNum ; i++) {
            invokeIndexs[i] = j;
            j ++ ;
           if(j >= nodeNum){
               j = 0;
           }
        }
        return invokeIndexs;
    }





}
