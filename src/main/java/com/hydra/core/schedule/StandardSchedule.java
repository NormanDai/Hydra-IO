package com.hydra.core.schedule;

import lombok.Setter;

import java.util.Date;

@Setter
public class StandardSchedule implements  Schedule{

    private long nextInvokeTime = 0;

    private boolean whetherReady = false;
    public long nextInvokeTimeByLong() {
        return nextInvokeTime;
    }

    public boolean whetherReady() {

        if(!whetherReady){
            Date nowDate = new Date();
            long timeLong = nowDate.getTime();
            if(nextInvokeTime >= timeLong){
                return true;
            }
            return false;
        }
        return whetherReady;
    }


}
