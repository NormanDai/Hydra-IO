package com.hydra.core;

import com.hydra.core.common.utils.PackageScanner;
import com.hydra.core.common.utils.StringUtil;
import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.remote.RemoteServiceHelper;
import com.hydra.core.remote.jgroups.JGroupsRemoteService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


public class AnnotationBatchSystem implements BatchSystem{

    private static final EnvironmentContext environmentContext = EnvironmentContext.getInstance();

    protected static final String CLASS_PATH_PACKAGE ="allClass";

    private static  AnnotationBatchSystem instance;

    private String scanPackage;

    private AnnotationBatchSystem(){}

    public static AnnotationBatchSystem getInstance(){
        if (instance == null) {
            synchronized (AnnotationBatchSystem.class) {
                if (instance == null) {
                    instance = new AnnotationBatchSystem();
                }
            }
        }
        return instance;
    }

    /**
     * 启动作业系统
     */
    public void start() {

        if(StringUtil.isEmpty(this.scanPackage)){
            return;
        }
        /** 初始化*/
        initEnv();
        /** 启动任务管理线程*/
        TaskManager.getInstance().main();
        /**启动分布式线程组*/
        new Thread(new RemoteServiceHelper(RemoteServiceHelper.JGROUPS)).start();
    }
    private void initEnv(){
        scanPackage();
        EnvironmentInitializer.getInstance().initialize();
    }

    public  AnnotationBatchSystem setScanPackage (String scanPackage){
        this.scanPackage = scanPackage;
        return getInstance();
    }

    private void scanPackage(){
        List<Class<?>> classes = PackageScanner.getClasses(this.scanPackage);
        environmentContext.addBean(CLASS_PATH_PACKAGE,classes);
    }


    public void stop() {

    }


}
