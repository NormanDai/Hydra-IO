package com.hydra.core;


import com.hydra.core.invoke.ManagerInvokeMonitor;
import com.hydra.core.invoke.ManagerInvokeTask;

public class TaskManager {

    private static TaskManager instance;

    private TaskManager(){}


    public  void main(){

        /**  任务管理线程*/
        ManagerInvokeTask managerInvokeTask = new ManagerInvokeTask();
        Thread manager = new Thread(managerInvokeTask);
        ManagerInvokeMonitor monitor = new ManagerInvokeMonitor();
        Thread managerMonitor = new Thread(monitor);
        monitor.setManager(manager);
        manager.start();
        managerMonitor.start();

    }


    public static TaskManager getInstance(){
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }
}
