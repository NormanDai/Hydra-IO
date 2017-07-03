package com.hydra.core.invoke;


import com.hydra.core.schedule.EnvironmentParams;
import com.hydra.core.wrapper.DistributedWrapper;

import java.util.concurrent.Executor;

public class ParallelInvokeStrategy implements InvokeStrategy{

    private static Executor executor = InvokeHandler.executor;

    public void invoke(String taskName, DistributedWrapper distributedWrapper) {
        int number = distributedWrapper.getNumber();

        for (int i = 0; i < number; i++) {
            EnvironmentParams params = new EnvironmentParams();
            params.setInvokeIndex(i);
            params.setJobName(taskName);
            params.setTotalInvoke(number);
            TaskArrows arrows = new TaskArrows(taskName,params);
            executor.execute(arrows);
        }
    }

}
