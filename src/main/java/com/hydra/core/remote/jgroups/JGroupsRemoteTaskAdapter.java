package com.hydra.core.remote.jgroups;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.hydra.core.invoke.HydraThreadPool;
import com.hydra.core.invoke.RemoteCallableTaskWrapper;
import com.hydra.core.invoke.RemoteTaskFutureCallback;
import com.hydra.core.remote.RemoteAddress;
import com.hydra.core.remote.RemoteTaskAdapter;
import com.hydra.core.schedule.EnvironmentParams;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class JGroupsRemoteTaskAdapter implements RemoteTaskAdapter {

    private static final int THREAD_NUM = 64;

    private static final int THREAD_QNM = 1024;

    private static final int processorNum = Runtime.getRuntime().availableProcessors();

    public static final Executor executor = HydraThreadPool.getExecutor(THREAD_NUM * processorNum,THREAD_QNM * processorNum);

    private static ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) executor);



    public void process(String taskName , RemoteAddress address, List<RemoteAddress> addressList, EnvironmentParams params) {

        RemoteCallableTaskWrapper callableTask = new RemoteCallableTaskWrapper();
        callableTask.setTaskName(taskName);
        callableTask.setRemoteAddress(address);
        UUID uuid = UUID.randomUUID();
        String randomId = uuid.toString().replace("-", "");
        callableTask.setMessageId(randomId);
        callableTask.setParams(params);
        /**  submit remote calling */
        ListenableFuture<Object> listenableFuture = threadPoolExecutor.submit(callableTask);

        RemoteTaskFutureCallback futureCallback = new RemoteTaskFutureCallback();
        futureCallback.setAddressList(addressList);
        futureCallback.setMessageId(randomId);
        /**  callback handling*/
        Futures.addCallback(listenableFuture,futureCallback,threadPoolExecutor);


    }


}
