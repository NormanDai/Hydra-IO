package com.hydra.core.invoke;

import com.hydra.core.common.DistributedStrategyEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvokeStrategySelector {

    public static InvokeStrategy selectStrategy(DistributedStrategyEnum strategy){

        InvokeStrategy invokeStrategy = new ParallelInvokeStrategy();
        if(strategy == DistributedStrategyEnum.SHARDING){
            invokeStrategy = new ShardingInvokeStrategy();
        }
        return invokeStrategy;
    }

}
