package com.hydra.core.remote.jgroups;


import com.hydra.core.common.BooleanEnum;
import com.hydra.core.common.MessageCodeEnum;
import com.hydra.core.common.MessageKeyEnum;
import com.hydra.core.common.utils.StringUtil;
import com.hydra.core.invoke.InvokeHandler;
import com.hydra.core.invoke.TaskArrows;
import com.hydra.core.remote.DistributedRemoteService;
import com.hydra.core.remote.RemoteAddress;
import com.hydra.core.remote.beans.RemoteTaskMessage;
import com.hydra.core.remote.beans.TaskInvokeAskRequest;
import com.hydra.core.remote.beans.TaskInvokeAskResponse;
import com.hydra.core.remote.beans.TaskInvokeHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jgroups.*;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class JGroupsRemoteService  extends ReceiverAdapter implements DistributedRemoteService {

    private static JGroupsRemoteService remoteService;

    private AtomicBoolean isMaster = new AtomicBoolean(false);

    private Vector<RemoteAddress> addressList = new Vector<RemoteAddress>();

    private  ConcurrentLinkedQueue<String> taskQueue = new ConcurrentLinkedQueue<String>();

    private ConcurrentHashMap<String,String> messageKeyMap = new ConcurrentHashMap<String, String>();

    private TaskInvokeHashMap invokeHashMap = TaskInvokeHashMap.getInstance();

    private static Executor executor = InvokeHandler.executor;
    /**
     * 集群名称.
     */
    private static final String CLUSTER_NAME = "HYDRA-SERVICE";
    /**
     * 节点通道.
     */
    private JChannel channel = null;


    public static final String SPLIT_STR = ":";


    private static final int MSG_SENDER = 0;

    private static final int MSG_KEY = 1;

    private static final int MSG_CONTEXT = 2;


    private ReentrantLock viewLock = new ReentrantLock();
    private ReentrantLock mssterLock = new ReentrantLock();
    private JGroupsRemoteService(){
        if(null == this.channel){
            try {
                System.out.println("启动分布式通信组件........" + " JGROUPS");
                channel = new JChannel();
                channel.setReceiver(this);
                channel.connect(CLUSTER_NAME);
                channel.getState(null, 10000);
            }catch (Exception e){
                throw new RuntimeException("启动JGroups节点异常!", e);
            }
        }
    }

    public static JGroupsRemoteService getInstance(){
        if (remoteService == null) {
            synchronized (JGroupsRemoteService.class) {
                if (remoteService == null) {
                    remoteService = new JGroupsRemoteService();
                }
            }
        }
        return remoteService;
    }


    public boolean isMaster() {
       return isMaster.get();
    }

    public List<RemoteAddress> getGroupsMember() {
        viewLock.lock();
        try{
            return addressList;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            viewLock.unlock();
        }
    }

    /**
     * Jgroups 目前不实现该方法
     */
    public void joinGroups() {}

    /**
     *
     */
    public void leaveGroups() {
        if(null != this.channel){
            if(channel.isConnected() && !channel.isClosed()){
                channel.close();
            }
        }
    }

    public void leaveGroups(long timeOut) {
        try {
            Thread.sleep(timeOut);
            leaveGroups();
        }catch (InterruptedException exception){
            throw  new RuntimeException(exception);
        }
    }

    @Override
    public void receive(Message msg) {

        RemoteTaskMessage taskMessage = (RemoteTaskMessage)msg.getObject();
        System.out.println("message received at " + new Date() + " message boday:" + taskMessage);
        String messageKey = taskMessage.getMessageKey();
        TaskInvokeAskRequest request = taskMessage.getRequest();
        TaskInvokeAskResponse response = taskMessage.getResponse();
        /**  若为执行请求，则将任务放入执行队列中，并返回消息*/
        if(MessageKeyEnum.EXE.getCode().equals(request.getMessageKey()) && MessageCodeEnum.ASK.getCode().equals(request.getMessageCode())){
            response = new TaskInvokeAskResponse();
            response.setMessageKey(MessageKeyEnum.EXE.getCode());
            response.setMessageCode(MessageCodeEnum.ACK.getCode());
            response.setMessageContext(request.getMessageContext());
            if(StringUtil.isEmpty(request.getMessageContext())){
                response.setSuccess(BooleanEnum.FALSE.getCode());
                response.setErrorContext("消息上下文不合法 null");
            }else {
                System.out.println(" task offer at " + new Date() + " task key:" + request.getMessageContext());
                TaskArrows arrows = new TaskArrows(request.getMessageContext(),taskMessage.getParams());
                executor.execute(arrows);
//                taskQueue.offer(request.getMessageContext());
//                messageKeyMap.put(request.getMessageContext(),messageKey);
            }
            /**  消息回复*/
            request.setMessageCode(MessageCodeEnum.ACK.getCode());
            response.setSuccess(BooleanEnum.TRUE.getCode());
            response.setErrorContext("任务已接受");
            taskMessage.setResponse(response);
            JGroupsAdress address = new JGroupsAdress();
            address.setAddress(msg.getSrc());
            this.sendMsg(address,taskMessage);
            System.out.println(" message replaied at " + new Date() + " message key:" + messageKey);
        }

        if(MessageKeyEnum.EXE.getCode().equals(request.getMessageKey()) && MessageCodeEnum.ACK.getCode().equals(request.getMessageCode())){
            RemoteTaskMessage remoteTaskMessage = invokeHashMap.getMessage(messageKey);
            if(null != remoteTaskMessage){
                System.out.println(" ACK message replaied at " + new Date() + " message key:" + messageKey);
                remoteTaskMessage.setResponse(response);
            }
        }
    }

    @Override
    public void viewAccepted(View view) {

        addressList.clear();
        List<Address> members = view.getMembers();
        for(Address address : members){
            JGroupsAdress Jaddress = new JGroupsAdress();
            Jaddress.setAddress(address);
            addressList.add(Jaddress);
        }
        System.out.println("new remote node viewAccepted at " + new Date() + " members:" + addressList);
        if(view.getCreator().toString().equals(this.channel.getAddressAsString())){
            System.out.println(view.getCreator().toString() + " get master AUTH  at " + new Date() + " members:" + addressList);
            isMaster.set(true);
        }
    }

    /**
     * 发送消息
     * @param address
     * @param message
     */
    public void sendMsg(RemoteAddress address, RemoteTaskMessage message) {
        Address jAddress = null;
        if(null != address && address instanceof JGroupsAdress){
            JGroupsAdress jGroupsAdress = (JGroupsAdress) address;
            jAddress = jGroupsAdress.getAddress();
        }
        Message JMessage = new Message(jAddress,message);
        invokeHashMap.addTask(message.getMessageKey(),message);
        try {
            channel.send(JMessage);
        }catch (Exception e){
            throw new RuntimeException("发送消息异常",e);
        }
    }
}
