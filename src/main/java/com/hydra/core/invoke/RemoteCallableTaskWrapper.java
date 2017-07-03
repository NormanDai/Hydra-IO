package com.hydra.core.invoke;


import com.hydra.core.common.MessageCodeEnum;
import com.hydra.core.common.MessageKeyEnum;
import com.hydra.core.remote.RemoteAddress;
import com.hydra.core.remote.beans.RemoteTaskMessage;
import com.hydra.core.remote.beans.TaskInvokeAskRequest;
import com.hydra.core.remote.jgroups.JGroupsRemoteService;
import com.hydra.core.schedule.EnvironmentParams;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
@Setter
public class RemoteCallableTaskWrapper  implements Callable<Object> {

    private String taskName;

    private RemoteAddress remoteAddress;

    private String messageId;

    private EnvironmentParams params;

    public Object call() throws Exception {

        JGroupsRemoteService remoteService = JGroupsRemoteService.getInstance();
        RemoteTaskMessage message = new RemoteTaskMessage();
        message.setMessageKey(messageId);
        TaskInvokeAskRequest request = new TaskInvokeAskRequest();
        request.setMessageCode(MessageCodeEnum.ASK.getCode());
        request.setMessageKey(MessageKeyEnum.EXE.getCode());
        request.setMessageContext(taskName);
        message.setRequest(request);
        message.setResponse(null);
        message.setParams(params);
        remoteService.sendMsg(remoteAddress,message);
        System.out.println("任务执行请求已发送：" + message);

        return null;
    }
}
