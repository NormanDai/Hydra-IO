package com.hydra.core.invoke;


import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.schedule.NormalTaskDescParser;
import com.hydra.core.schedule.Schedule;

import java.util.Map;
import java.util.concurrent.Executor;

public class ManagerInvokeTask implements Runnable{

    private static final EnvironmentContext environmentContext = EnvironmentContext.getInstance();

    private static Executor executor = InvokeHandler.executor;

    private boolean closeFlag = false;

    public void run() {

        Map<String,Schedule> environmentSchedules = environmentContext.getObject(NormalTaskDescParser.CONTXT_SCHEDUS, Map.class);
        while (!this.closeFlag){

            for(Map.Entry<String,Schedule> entry : environmentSchedules.entrySet()){
                String jobName = entry.getKey();
                Schedule schedule = entry.getValue();
                if(schedule.whetherReady()){
                    executor.execute(new TaskInvokeBonne(jobName));
                }

            }

        }
    }

    public void close(){
        closeFlag = true;
    }

}
