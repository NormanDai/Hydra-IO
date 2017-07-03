package com.hydra.core.invoke;


import com.hydra.core.schedule.EnvironmentParams;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Setter
@Getter

public class CallableTaskWrapper  implements Callable<Object> {

    private String invokeName;

    private Method method;

    private Class clazz;

    private Object instance;

    private Object param;

    private Object result;

    private Throwable exception;

    private boolean  isSuccess ;

    private EnvironmentParams environmentParams;



    public CallableTaskWrapper(){

    }

    public CallableTaskWrapper(String invokeName,Method method,Class clazz,Object instance,Object param){
        this.invokeName = invokeName;
        this.method = method;
        this.clazz = clazz;
        this.instance = instance;
        this.param = param;
    }

    public Object call() throws Exception {
        try {
            if(null == method){
                throw new RuntimeException("invoke method "+invokeName+" is NULL!");
            }
            if(null != instance){
                /** invoke special method*/
                return method.invoke(instance, paramValidation());

            }else if(null != clazz){
                /**  get class's instance*/
                Object objInstance = clazz.newInstance();
                return method.invoke(objInstance, paramValidation());
            }else {
              throw new RuntimeException("No invoke instance or class for "+invokeName+"!");
            }
        }catch (Exception exception){
            isSuccess = false;
            this.exception = exception;
            ExceptionUtils.printRootCauseStackTrace(exception);
        }
        return null;
    }

    private Object[] paramValidation(){

        Class<?>[] parameterTypes = method.getParameterTypes();
        if(parameterTypes.length == 0){
            return null;
        }
        Object[] paramters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length ; i++) {
            Class<?> type = parameterTypes[i];
            if(type.equals(EnvironmentParams.class)){
                paramters[i] = environmentParams;
            }else if(null != param && type.equals(param.getClass())){
                paramters[i] = param;
            }else {
                paramters[i] = null;
            }
        }
        return paramters;
    }


}
