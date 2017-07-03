package com.hydra.core.invoke;


import com.hydra.core.common.DistributedStrategyEnum;
import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.schedule.NormalTaskDescParser;
import com.hydra.core.wrapper.DistributedWrapper;
import com.hydra.core.wrapper.WrapperBean;

import java.util.Date;
import java.util.List;

public class TaskInvokeBonne implements Runnable{
    
    private static final EnvironmentContext environmentContext = EnvironmentContext.getInstance();
    
    List<WrapperBean> environmentWrappers = environmentContext.getObject(NormalTaskDescParser.CONTXT_WRAPERS, List.class);
    
    private final String jobName;

    public TaskInvokeBonne(final String jobName){
        this.jobName = jobName;
    }

    public void run() {

        WrapperBean wrapper = null;
        for (WrapperBean wrapperBean : environmentWrappers){
            String wrapperName = wrapperBean.getTaskWrapper().getName() + "@" + wrapperBean.getExecutorWrapper().getValue();
            if(wrapperName.equals(jobName)){
                wrapper = wrapperBean;
            }
        }
        
        if(null != wrapper){
            DistributedStrategyEnum strategy = wrapper.getDistributedWrapper().getStrategy();
            InvokeStrategy invokeStrategy = InvokeStrategySelector.selectStrategy(strategy);
            invokeStrategy.invoke(jobName,wrapper.getDistributedWrapper());
        }
    }
}
