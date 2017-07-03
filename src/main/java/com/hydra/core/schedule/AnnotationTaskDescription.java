package com.hydra.core.schedule;


import com.hydra.core.annotation.*;
import com.hydra.core.common.utils.ReflactUtil;
import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.flow.TaskFlowGraph;
import com.hydra.core.flow.TaskFlowParser;
import com.hydra.core.invoke.InvokeHandler;
import com.hydra.core.test.TestInvokeTask;
import com.hydra.core.test.TestInvokeTask2;
import com.hydra.core.wrapper.*;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
public class AnnotationTaskDescription implements  TaskDescription{


    private List<WrapperBean> wrappers;

    private Class clazz;

    /**
     * 标准模式 作业描述抽取
     * @param
     */
    public void extractDescription() {
        wrappers = new ArrayList<WrapperBean>();
        /**
         * 该方法主要功能：从类中提取作业执行描述信息，并组装到 wrappers中
          */
        Annotation annotsTask = ReflactUtil.getTypeAnnotationByName(Task.class, clazz);
        if(null != annotsTask){
            Method[] methods = ReflactUtil.getMethodsByAnnotation(Executor.class, clazz);
            TaskWrapper taskWrapper = this.taskDescExt();
            for(Method method : methods){
                WrapperBean bean = new WrapperBean();
                //set TaskWrapper
                bean.setTaskWrapper(taskWrapper);
                //set executorWrapper
                bean.setExecutorWrapper(this.executorDescExt(method));
                //set ExpressionWrapper
                bean.setExpressionWrapper(this.expressionDescExt(method));
                // set JoinWrapper
                bean.setJoinWrapper(this.joinDescExt(method));
                //set DistributedWrapper
                bean.setDistributedWrapper(this.distributedDescExt(method));
                // set invoke class
                bean.setInvokeClass(clazz);
                // set invoke method
                bean.setInvokeMethod(method);
                wrappers.add(bean);
            }
        }

    }

    /**
     * 获取 wrappers
     * @return
     */
    public List<WrapperBean> getWrappers(){
        if(this.wrappers == null || this.wrappers.size() == 0){
            return null;
        }
        return wrappers;
    }

    /**
     * 抽取 Task 注解
     */
    private TaskWrapper taskDescExt(){
        /** 抽取注解*/
        Annotation annotation = ReflactUtil.getTypeAnnotationByName(Task.class, clazz);
        /**  注解类型转换*/
        if(null != annotation){
            Task task = ReflactUtil.getAnnotationInstance(annotation, Task.class);
            TaskWrapper wrapper = new TaskWrapper();
            wrapper.setName(task.value());
            return wrapper;
        }
        return null;
    }

    /**
     * 抽取 Executor 注解
     * @param method
     * @return
     */
    private ExecutorWrapper executorDescExt(Method method){
        Annotation annotation = ReflactUtil.getAnnotationFromMethod(Executor.class, method);
        if(null != annotation){
            Executor executor = ReflactUtil.getAnnotationInstance(annotation, Executor.class);
            ExecutorWrapper wrapper = new ExecutorWrapper();
            wrapper.setValue(executor.value());
            return wrapper;
        }
        return null;
    }

    /**
     * 抽取 Executor 注解
     * @param method
     * @return
     */
    private ExpressionWrapper expressionDescExt(Method method){
        Annotation annotation = ReflactUtil.getAnnotationFromMethod(Expression.class, method);
        if(null != annotation){
            Expression expression = ReflactUtil.getAnnotationInstance(annotation, Expression.class);
            ExpressionWrapper wrapper = new ExpressionWrapper();
            wrapper.setStrategy(expression.strategy());
            wrapper.setFactor(expression.factor());
            wrapper.setMeasure(expression.measure());
            return wrapper;
        }
        return null;
    }
    /**
     * 抽取 Join 注解
     * @param method
     * @return
     */
    private JoinWrapper joinDescExt(Method method){
        Annotation annotation = ReflactUtil.getAnnotationFromMethod(Join.class, method);
        if(null != annotation){
            Join join = ReflactUtil.getAnnotationInstance(annotation, Join.class);
            JoinWrapper wrapper = new JoinWrapper();
            wrapper.setName(join.value());
            return wrapper;
        }
        return null;
    }

    /**
     * 抽取 Distributed 注解
     * @param method
     * @return
     */
    private DistributedWrapper distributedDescExt(Method method){
        Annotation annotation = ReflactUtil.getAnnotationFromMethod(Distributed.class, method);
        if(null != annotation){
            Distributed distributed = ReflactUtil.getAnnotationInstance(annotation, Distributed.class);
            DistributedWrapper wrapper = new DistributedWrapper();
            wrapper.setStrategy(distributed.strategy());
            wrapper.setNumber(distributed.number());
            return wrapper;
        }
        return null;
    }

    public static void main(String[] args){
        AnnotationTaskDescription taskDescription = new AnnotationTaskDescription();
        taskDescription.setClazz(TestInvokeTask.class);
        taskDescription.extractDescription();
        List<WrapperBean> wrappers = taskDescription.getWrappers();
       // System.out.println(wrappers);



        AnnotationTaskDescription taskDescription2 = new AnnotationTaskDescription();
        taskDescription2.setClazz(TestInvokeTask2.class);
        taskDescription2.extractDescription();
        List<WrapperBean> wrappers2 = taskDescription.getWrappers();
        //System.out.println(wrappers2);

        NormalTaskDescParser parser = new NormalTaskDescParser();
        parser.parser(taskDescription);

        NormalTaskDescParser parser2 = new NormalTaskDescParser();
        parser2.parser(taskDescription2);


        EnvironmentContext environmentContext = EnvironmentContext.getInstance();
        List environmentWrappers = environmentContext.getObject(NormalTaskDescParser.CONTXT_WRAPERS, List.class);
        Map<String,Schedule> environmentSchedules = environmentContext.getObject(NormalTaskDescParser.CONTXT_SCHEDUS, Map.class);
        List<String> environmentJobNames = environmentContext.getObject(NormalTaskDescParser.CONTXT_JBNMS, List.class);
        Map<String,Class> environmentInvokeClass = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_CLS, Map.class);
        Map<String,Method> environmentInvokeMrthod = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_MTHD, Map.class);
       // System.out.println(environmentWrappers);
       // System.out.println(environmentSchedules);
        //System.out.println(environmentJobNames);
        //System.out.println(environmentInvokeClass);


        TaskFlowParser flowParser = new TaskFlowParser();
        flowParser.parser();

        Map<String,TaskFlowGraph> environmentflows = environmentContext.getObject(TaskFlowParser.CONTXT_FLW_GRAPH, Map.class);
        //System.out.println(environmentflows);

//        Map<String,Schedule> environmentSchedules = environmentContext.getObject(NormalTaskDescParser.CONTXT_SCHEDUS, Map.class);
//        List<String> environmentJobNames = environmentContext.getObject(NormalTaskDescParser.CONTXT_JBNMS, List.class);
//        Map<String,Class> environmentInvokeClass = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_CLS, Map.class);
//        Map<String,Method> environmentInvokeMrthod = environmentContext.getObject(NormalTaskDescParser.CONTXT_IVK_MTHD, Map.class);
//        List environmentWrappers = environmentContext.getObject(NormalTaskDescParser.CONTXT_WRAPERS, List.class);
//        Map<String,TaskFlowGraph> environmentflows = environmentContext.getObject(TaskFlowParser.CONTXT_FLW_GRAPH, Map.class);
//        System.out.println(environmentContext);

        InvokeHandler invokeHandler = InvokeHandler.getInstance();
        EnvironmentParams environmentParams = new EnvironmentParams();
        invokeHandler.invoke("testInvokeTask@executorA",environmentParams);


    }


}
