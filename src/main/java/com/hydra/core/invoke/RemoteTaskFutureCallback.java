package com.hydra.core.invoke;

import com.google.common.util.concurrent.FutureCallback;
import com.hydra.core.remote.RemoteAddress;
import com.hydra.core.remote.beans.RemoteTaskMessage;
import com.hydra.core.remote.beans.TaskInvokeHashMap;
import com.hydra.core.remote.jgroups.JGroupsRemoteTaskAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public class RemoteTaskFutureCallback implements FutureCallback<Object>{


    private Throwable throwable;

    private List<RemoteAddress> addressList;

    private String messageId;

    public void onSuccess(@Nullable Object o) {

        TaskInvokeHashMap invokeHashMap = TaskInvokeHashMap.getInstance();
        RemoteTaskMessage respMessage = invokeHashMap.getRespMessage(messageId, 1000);
        if(null == respMessage.getResponse() && null != addressList && !addressList.isEmpty()){
            RemoteAddress address = addressList.get(getRandom(addressList.size()));
            new JGroupsRemoteTaskAdapter().process(respMessage.getRequest().getMessageContext(),address,addressList,respMessage.getParams());
        }
    }

    public void onFailure(Throwable throwable) {
        throw new RuntimeException(throwable);
    }


    private int getRandom(int max){
        int min=0;
        Random random = new Random();
        return  random.nextInt(max)%(max-min+1) + min;
    }
}
