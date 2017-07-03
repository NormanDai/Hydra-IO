package com.hydra.core.schedule;


import com.hydra.core.common.ExpressionMeasureEnum;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Setter
public class IntervalSchedule implements  Schedule{

    private ExpressionMeasureEnum measure;

    private String factor;

    private Date initDate;

    private boolean firstTime = false;

    public long nextInvokeTimeByLong() {
        return timeAddition().getTime();
    }

    public boolean whetherReady() {
        Date nowDate = new Date();
        if(firstTime || nowDate.getTime() >= nextInvokeTimeByLong()){
            initDate = nowDate;
            firstTime = false;
            return true;
        }
        return false;
    }

    private Date timeAddition(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(initDate);

        int addFactor = Integer.valueOf(factor);
        if(addFactor <= 0){
            throw new RuntimeException("invalid int factor value for interval model"+" value:" +factor);
        }
        if(measure == ExpressionMeasureEnum.YEAR){
            calendar.add(Calendar.YEAR,addFactor);
        }
        if(measure == ExpressionMeasureEnum.MONTH){
            calendar.add(Calendar.MONTH,addFactor);
        }
        if(measure == ExpressionMeasureEnum.DAY){
            calendar.add(Calendar.DAY_OF_MONTH,addFactor);
        }
        if(measure == ExpressionMeasureEnum.HOUR){
            calendar.add(Calendar.HOUR,addFactor);
        }
        if(measure == ExpressionMeasureEnum.MINUTE){
            calendar.add(Calendar.MINUTE,addFactor);
        }
        if(measure == ExpressionMeasureEnum.SECOND){
            calendar.add(Calendar.SECOND,addFactor);
        }
        return calendar.getTime();
    }


    public  static void main(String[] agr){
        Calendar calendar = new GregorianCalendar();
        IntervalSchedule schedule = new IntervalSchedule();
        schedule.setInitDate(new Date());
        schedule.setMeasure(ExpressionMeasureEnum.MINUTE);
        schedule.setFactor("1");

        while (true){

            if(schedule.whetherReady()){
                System.out.println(new Date());
                calendar.setTimeInMillis(schedule.nextInvokeTimeByLong());
                System.out.println(calendar.getTime());

            }
        }
    }


}
