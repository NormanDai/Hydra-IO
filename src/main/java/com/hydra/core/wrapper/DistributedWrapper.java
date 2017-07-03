package com.hydra.core.wrapper;

import com.hydra.core.common.DistributedStrategyEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributedWrapper {

    private DistributedStrategyEnum strategy;

    private int number;

}
