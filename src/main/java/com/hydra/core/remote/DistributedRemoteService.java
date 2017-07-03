package com.hydra.core.remote;


import com.hydra.core.remote.beans.RemoteTaskMessage;

import java.util.List;

public interface DistributedRemoteService {


    /**
     * 返回当前节点是否为master
     * @return
     */
    boolean isMaster();

    /**
     * 获取该分组下的所有机器地址
     * @return
     */
    List<RemoteAddress> getGroupsMember();

    /**
     * 加入一个集群
     */
    void joinGroups();

    /**
     * 离开一个集群
     */
    void leaveGroups();

    /**
     * 离开一个集群
     * @param timeOut
     */
    void leaveGroups(long timeOut);

    /**
     * 发送消息
     * @param address
     * @param message
     */
    void sendMsg(RemoteAddress address, RemoteTaskMessage message);

}
