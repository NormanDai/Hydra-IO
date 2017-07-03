package com.hydra.core.container;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnvironmentContext extends AbstractContext{

    private static EnvironmentContext instance = new EnvironmentContext();

    private static Map<String,Object> beanMap = new ConcurrentHashMap<String, Object>();

    private EnvironmentContext(){
    }

    public static EnvironmentContext getInstance(){
        if (instance == null) {
            synchronized (EnvironmentContext.class) {
                if (instance == null) {
                    instance = new EnvironmentContext();
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
