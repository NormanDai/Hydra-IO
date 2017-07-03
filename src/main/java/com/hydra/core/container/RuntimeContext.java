package com.hydra.core.container;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeContext extends AbstractContext{

    private static RuntimeContext instance = new RuntimeContext();

    private static Map<String,Object> beanMap = new ConcurrentHashMap<String, Object>();

    private RuntimeContext(){
    }

    public RuntimeContext getInstance(){
        if (instance == null) {
            synchronized (RuntimeContext.class) {
                if (instance == null) {
                    instance = new RuntimeContext();
                }
            }
        }
        return instance;
    }

    @Override
    Map<String, Object> getObjectMap() {
        return beanMap;
    }
}
