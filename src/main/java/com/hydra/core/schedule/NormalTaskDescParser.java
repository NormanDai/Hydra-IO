package com.hydra.core.schedule;


import com.hydra.core.wrapper.ExecutorWrapper;
import com.hydra.core.wrapper.ExpressionWrapper;
import com.hydra.core.wrapper.TaskWrapper;
import com.hydra.core.wrapper.WrapperBean;
import com.hydra.core.common.ExpressionMeasureEnum;
import com.hydra.core.common.ExpressionStrategyEnum;
import com.hydra.core.common.utils.StringUtil;
import com.hydra.core.container.EnvironmentContext;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NormalTaskDescParser implements TaskDescParser<TaskDescription>{


    public static  final String CONTXT_WRAPERS = "wrappers";
    public static  final String CONTXT_SCHEDUS = "schedules";
    public static  final String CONTXT_JBNMS = "jobNames";
    public static  final String CONTXT_IVK_CLS = "invokeClass";
    public static  final String CONTXT_IVK_MTHD = "invokeMethods";


    public void parser(TaskDescription taskDescription) {

        taskDescription.extractDescription();
        List<WrapperBean> wrappers = taskDescription.getWrappers();
        /**  添加运行环境容器*/
        EnvironmentContext environmentContext = EnvironmentContext.getInstance();
        List environmentWrappers = environmentContext.getObject(CONTXT_WRAPERS, List.class);
        if(null == environmentWrappers){
            environmentWrappers = new ArrayList<WrapperBean>();
        }
        environmentWrappers.addAll(wrappers);
        environmentContext.addBean(CONTXT_WRAPERS,environmentWrappers);
        /**  schedules容器*/
        Map<String,Schedule> environmentSchedules = environmentContext.getObject(CONTXT_SCHEDUS, Map.class);
        if(null == environmentSchedules){
            environmentSchedules = new ConcurrentHashMap<String, Schedule>();
        }
        environmentContext.addBean(CONTXT_SCHEDUS,environmentSchedules);
        /**  jobNames 容器*/
        List<String> environmentJobNames = environmentContext.getObject(CONTXT_JBNMS, List.class);
        if(null == environmentJobNames){
            environmentJobNames = new ArrayList<String>();
        }
        environmentContext.addBean(CONTXT_JBNMS,environmentJobNames);
        /** invokeClass容器 */
        Map<String,Class> environmentInvokeClass = environmentContext.getObject(CONTXT_IVK_CLS, Map.class);
        if(null == environmentInvokeClass){
            environmentInvokeClass = new ConcurrentHashMap<String, Class>();
        }
        environmentContext.addBean(CONTXT_IVK_CLS,environmentInvokeClass);
        /** invoke method*/
        Map<String,Method> environmentInvokeMrthod = environmentContext.getObject(CONTXT_IVK_MTHD, Map.class);
        if(null == environmentInvokeMrthod){
            environmentInvokeMrthod = new ConcurrentHashMap<String, Method>();
        }
        environmentContext.addBean(CONTXT_IVK_MTHD,environmentInvokeMrthod);


        // 遍历executor
        for(WrapperBean wrapperBean : wrappers){
            ExpressionWrapper expressionWrapper = wrapperBean.getExpressionWrapper();
            TaskWrapper taskWrapper = wrapperBean.getTaskWrapper();
            ExecutorWrapper executorWrapper = wrapperBean.getExecutorWrapper();
            String jobName = taskWrapper.getName()+"@"+executorWrapper.getValue();
            if(null != expressionWrapper){
                ExpressionStrategyEnum strategy = expressionWrapper.getStrategy();
                Schedule schedule = null;
                /**  标准策略*/
                if(strategy.equals(ExpressionStrategyEnum.STANDARD)){
                    schedule = parserStandard(wrapperBean);
                }
                /**  定时策略*/
                if(strategy.equals(ExpressionStrategyEnum.TIMING)){
                    schedule = parserTiming(wrapperBean);
                }
                /**  间隔策略*/
                if(strategy.equals(ExpressionStrategyEnum.INTERVAL)){
                    schedule = parserInterval(wrapperBean);
                }

                environmentSchedules.put(jobName,schedule);
                environmentJobNames.add(jobName);
            }
            environmentInvokeClass.put(jobName,wrapperBean.getInvokeClass());
            environmentInvokeMrthod.put(jobName,wrapperBean.getInvokeMethod());

        }
    }


    /**
     * 标准模式：解析
     * @param wrapperBean
     * @return
     */
    private Schedule parserStandard(WrapperBean wrapperBean){
        ExpressionWrapper expressionWrapper = wrapperBean.getExpressionWrapper();
        if(null != expressionWrapper){
            String factor = expressionWrapper.getFactor();
            ExpressionMeasureEnum measure = expressionWrapper.getMeasure();
            if(measure != ExpressionMeasureEnum.STANDARD){
                throw new RuntimeException("Measure must be ExpressionMeasureEnum.STANDARD for Standard model");
            }
            StandardSchedule standardSchedule = new StandardSchedule();

            if(StringUtil.isEmpty(factor)){
                standardSchedule.setNextInvokeTime(0);
                standardSchedule.setWhetherReady(true);
            }else {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ExpressionMeasureEnum.STANDARD.getValue());
                try {
                    Date parse = simpleDateFormat.parse(factor);
                    standardSchedule.setNextInvokeTime( parse.getTime());
                }catch (ParseException ex){
                    throw new RuntimeException(ex);
                }
                standardSchedule.setWhetherReady(false);

            }
            return standardSchedule;
        }
        return  null;
    }

    /**
     * 定时模式解析
     * @param wrapperBean
     * @return
     */
    private Schedule parserTiming(WrapperBean wrapperBean){

        ExpressionWrapper expressionWrapper = wrapperBean.getExpressionWrapper();

        if(null != expressionWrapper){
            ExpressionMeasureEnum measure = expressionWrapper.getMeasure();
            String factor = expressionWrapper.getFactor();
            TimingSchedule schedule = new TimingSchedule();
            schedule.setFactor(factor);
            schedule.setMeasure(measure);
            return schedule;
        }
        return  null;
    }

    /**
     *  间隔模式解析
     * @param wrapperBean
     * @return
     */
    private Schedule parserInterval(WrapperBean wrapperBean){
        ExpressionWrapper expressionWrapper = wrapperBean.getExpressionWrapper();
        if(null != expressionWrapper){
            ExpressionMeasureEnum measure = expressionWrapper.getMeasure();
            String factor = expressionWrapper.getFactor();
            IntervalSchedule schedule = new IntervalSchedule();
            schedule.setFactor(factor);
            schedule.setMeasure(measure);
            schedule.setInitDate(new Date());
            schedule.setFirstTime(true);
            return schedule;
        }
        return  null;
    }
}
