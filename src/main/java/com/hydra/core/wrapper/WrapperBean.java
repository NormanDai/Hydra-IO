package com.hydra.core.wrapper;


import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Setter
@Getter
public class WrapperBean {

    private TaskWrapper taskWrapper;

    private JoinWrapper joinWrapper;

    private ExpressionWrapper expressionWrapper;

    private ExecutorWrapper executorWrapper;

    private DistributedWrapper distributedWrapper;

    private Class invokeClass;

    private Object invokeInstance;

    private Method invokeMethod;

}
