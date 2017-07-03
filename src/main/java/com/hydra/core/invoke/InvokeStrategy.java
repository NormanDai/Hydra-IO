package com.hydra.core.invoke;


import com.hydra.core.wrapper.DistributedWrapper;

public interface InvokeStrategy {

    public void invoke(String taskName,final DistributedWrapper distributedWrapper);

}
