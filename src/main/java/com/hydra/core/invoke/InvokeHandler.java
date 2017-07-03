package com.hydra.core.invoke;


import com.google.common.util.concurrent.*;
import com.hydra.core.schedule.EnvironmentParams;
import com.hydra.core.schedule.NormalTaskDescParser;
import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.flow.TaskFlowGraph;
import com.hydra.core.flow.TaskFlowNode;
import com.hydra.core.flow.TaskFlowParser;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class InvokeHandler {

    private static final int THREAD_NUM = 64;

    private static final int THREAD_QNM = 1024;

    private static final int processorNum = Runtime.getRuntime().availableProcessors();

    public static final Executor executor = HydraThreadPool.getExecutor(THREAD_NUM * processorNum,THREAD_QNM * processorNum);

    private static ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) executor);

    private static InvokeHandler instance;

    private static final EnvironmentContext environmentContext = EnvironmentContext.getInstance();

    private InvokeHandler(){

    }

    public static InvokeHandler getInstance(){
        if (instance == null) {
            synchronized (InvokeHandler.class) {
                if (instance == null) {
                    instance = new InvokeHandler();
                }
            }
        }
        return instance;
    }

    /**
     *  执行单线任务
     * @param taskName
     * @param params
     */
    public void invoke(String taskName, final EnvironmentParams params){

        /**  get task flow graph*/
        Map<String,TaskFlowGraph> environmentflows = environmentContext.getObject(TaskFlowParser.CONTXT_FLW_GRAPH, Map.class);
        TaskFlowGraph flowGraph = environmentflows.get(taskName);
        TaskFlowNode initExecutorNode = flowGraph.getInitExecutorNode();
        /** invoke all sub task*/
        invokeSubTaskByClass(initExecutorNode,null,params);
    }

    /**
     * 递归执行所有关联任务
     * @param node
     * @param param
     * @param environmentParams
     */
    private void invokeSubTaskByClass(TaskFlowNode node,Object param,EnvironmentParams environmentParams){
        if(null != node){
            String labName = node.getLabName();
            /**  get invoke class and invoke method*/
            Map<String,Class> environmentInvokeClass = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_CLS, Map.class);
            Map<String,Method> environmentInvokeMrthod = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_MTHD, Map.class);
            Class invokeClass = environmentInvokeClass.get(labName);
            Method invokeMethod = environmentInvokeMrthod.get(labName);
            /** set callback task wrapper*/
            CallableTaskWrapper callableTask = new CallableTaskWrapper(labName,invokeMethod,invokeClass,null, param);
            EnvironmentParams environmentPar = getNewEnvironmentParams(environmentParams);
            environmentPar.setJobName(labName);
            callableTask.setEnvironmentParams(environmentPar);

            ListenableFuture<Object> listenableFuture = threadPoolExecutor.submit(callableTask);
            /** add callback to this invoke*/
            TaskFutureCallback taskFutureCallback = new TaskFutureCallback();
            CountDownLatch waitLatch = new CountDownLatch(1);
            taskFutureCallback.setDownLatch(waitLatch);
            Futures.addCallback(listenableFuture,taskFutureCallback,threadPoolExecutor);

            try {
                waitLatch.await();
            }catch (Exception exception){
                ExceptionUtils.printRootCauseStackTrace(exception);
            }

            Object result = taskFutureCallback.getResult();
            Throwable throwable = taskFutureCallback.getThrowable();
            if(null != throwable){
                ExceptionUtils.printRootCauseStackTrace(throwable);
            }
            List<TaskFlowNode> nextNodes = node.getNextNodes();
            if(null != nextNodes && !nextNodes.isEmpty()){
                for (TaskFlowNode nextNode:nextNodes){
                    invokeSubTaskByClass(nextNode,result,environmentPar);
                }
            }

        }

    }

    private EnvironmentParams getNewEnvironmentParams(final EnvironmentParams environmentParams){
        EnvironmentParams environmentPar = new EnvironmentParams();
        environmentPar.setInvokeIndex(environmentParams.getInvokeIndex());
        environmentPar.setInvokeParams(environmentParams.getInvokeParams());
        environmentPar.setJobName(environmentParams.getJobName());
        environmentPar.setTotalInvoke(environmentParams.getTotalInvoke());
        return environmentPar;
    }


    public void invoke(String taskName,Object instance,final EnvironmentParams params){

    }


}
