package com.hydra.core;


import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.flow.TaskFlowParser;
import com.hydra.core.schedule.AnnotationTaskDescription;
import com.hydra.core.schedule.NormalTaskDescParser;

import java.util.List;

public class EnvironmentInitializer {

    private static final EnvironmentContext environmentContext = EnvironmentContext.getInstance();
    private static EnvironmentInitializer instance;

    private EnvironmentInitializer(){}

    public static EnvironmentInitializer getInstance(){
        if (instance == null) {
            synchronized (EnvironmentInitializer.class) {
                if (instance == null) {
                    instance = new EnvironmentInitializer();
                }
            }
        }
        return instance;
    }


    protected void initialize(){

        List<Class<?>> allClass = environmentContext.getObject(AnnotationBatchSystem.CLASS_PATH_PACKAGE, List.class);
        for(Class<?> clazz : allClass){

            if(!clazz.isInterface()){
                /** load task descriptions*/
                AnnotationTaskDescription taskDescription = new AnnotationTaskDescription();
                taskDescription.setClazz(clazz);
                taskDescription.extractDescription();
                if(null != taskDescription.getWrappers() && !taskDescription.getWrappers().isEmpty()){
                    NormalTaskDescParser parser = new NormalTaskDescParser();
                    parser.parser(taskDescription);
                }
            }
        }
        /** task flow parse*/
        new TaskFlowParser().parser();
    }
}
