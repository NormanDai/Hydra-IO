package com.hydra.core.invoke;


import com.hydra.core.schedule.EnvironmentParams;

public class TaskArrows implements Runnable{

    private final String jobName;

    private final EnvironmentParams params;

    public TaskArrows(final String jobName,final EnvironmentParams params){
        this.jobName = jobName;
        this.params = params;
    }
    public void run() {
        InvokeHandler.getInstance().invoke(jobName,params);
    }

}
