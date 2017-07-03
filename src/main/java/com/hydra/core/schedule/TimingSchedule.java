package com.hydra.core.schedule;

import com.hydra.core.common.ExpressionMeasureEnum;
import com.hydra.core.common.TimingCalendarEnum;
import com.hydra.core.common.TimingPartingEnum;
import com.hydra.core.common.TimingSplitLineEnum;
import com.hydra.core.common.utils.DateUtil;
import com.hydra.core.common.utils.StringUtil;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Setter
public class TimingSchedule implements  Schedule{


    private ExpressionMeasureEnum measure;

    private String factor;

    private Date basicDate;

    public long nextInvokeTimeByLong() {
        return basicDate.getTime();
    }

    public boolean whetherReady() {
        if(null == basicDate){
            basicDate = measureProtor();
        }
        while (basicDate.getTime() < new Date().getTime()){
            basicDate = nextInvokeTime();
        }
        if(basicDate.getTime() == new Date().getTime()){
            basicDate = nextInvokeTime();
            return true;
        }
        return false;
    }

    private Date nextInvokeTime() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(basicDate);
        calendar.add(TimingCalendarEnum.explain(measure.getCode()).getValue(), 1);
        return calendar.getTime();
    }


    /**
     *  basic date
     * @return
     */
    private Date measureProtor(){
        // get now measure
        String formatDateStr = DateUtil.format(new Date(), TimingPartingEnum.explain(measure.getCode()).getValue());
        if(StringUtil.isNotEmpty(factor)){
            String fullStrDate = formatDateStr + TimingSplitLineEnum.explain(measure.getCode()).getValue() + factor;
            return  DateUtil.parse(fullStrDate,DateUtil.FULL_PATTERN);
        }
        return null;
    }

    public  static void main(String[] agr){
        Calendar calendar = new GregorianCalendar();
        TimingSchedule schedule = new TimingSchedule();
        schedule.setMeasure(ExpressionMeasureEnum.MONTH);
        schedule.setFactor("26 12:34:12");
        while (true){

            if(schedule.whetherReady()){
                System.out.println(new Date());
                calendar.setTimeInMillis(schedule.nextInvokeTimeByLong());
                System.out.println(calendar.getTime());
              //  return;
            }
        }
    }



}
