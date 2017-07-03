package com.hydra.core.invoke;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ManagerInvokeMonitor implements Runnable{

    private Thread manager;
    private ManagerInvokeTask managerInvokeTask;
    private static final long monTime = 3 * 1000L;

    public void run() {

        System.out.println("ManagerInvokeMonitor start at " + new Date());
        if(null == manager){
           throw new RuntimeException("ManagerInvokeTask is null!");
        }
        while (true){
            try {
                System.out.println("ManagerInvokeMonitor sleep at " + new Date());
                Thread.sleep(monTime);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            if(!manager.isAlive()){
                System.out.println(manager + " TaskManager is deal  " + new Date());
                ManagerInvokeTask managerInvokeTask = new ManagerInvokeTask();
                Thread manager = new Thread(managerInvokeTask);
                this.manager = manager;
                manager.start();
            }
        }
    }
}
