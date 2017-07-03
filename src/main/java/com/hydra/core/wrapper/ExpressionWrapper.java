package com.hydra.core.wrapper;

import com.hydra.core.common.ExpressionMeasureEnum;
import com.hydra.core.common.ExpressionStrategyEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpressionWrapper {


    private  ExpressionStrategyEnum strategy;

    private ExpressionMeasureEnum measure;

    private String factor;

}
