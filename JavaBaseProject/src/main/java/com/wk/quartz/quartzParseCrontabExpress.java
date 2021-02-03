package com.wk.quartz;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author: wk
 * @Date: 2021/2/3 10:22
 * @Description 利用quartz 对crontab 表达式的解析
 */
public class quartzParseCrontabExpress {
    private static String CronTab = "0 11,26,41,56 * * * ?";
    static  final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    /////////////////////////  基于表达式的操作
    // 获取指定时间的下次执行时间
    public static Date getNextExecuteTimeWithExpress(String cron, Date startTime) throws ParseException {
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronExpression expression = new CronExpression(cron);
        final Date next = expression.getNextValidTimeAfter(startTime);
        System.out.println("nextTime: " + format.format(next));
        return next;
    }

    public static Date getLastExecuteTimeWithExpress(String cron, Date startTime) throws ParseException {
        if (! CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronExpression expression = new CronExpression(cron);
        final Date next = expression.getNextValidTimeAfter(startTime);
        final Date next1 = expression.getNextValidTimeAfter(next);
        final Date next2 = expression.getNextValidTimeAfter(next1);
        final Date last = Date.from(Instant.ofEpochMilli(next.getTime() - (next2.getTime() - next1.getTime())));
        System.out.println("startTime: "+ format.format(startTime));
//        System.out.println("nextTime: "+ format.format(next));
//        System.out.println("nextTime1: "+ format.format(next1));
//        System.out.println("nextTime2: "+ format.format(next2));
        System.out.println("lastTime : "+ format.format(last));
        return last;
    }





    //////////////////////   基于触发器的操作
    // 当前时间的下一次执行点
    public static Date getNextExecuteTimeWithTrigger(String cron){
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronTrigger caclulate_date = TriggerBuilder.newTrigger().withIdentity("Caclulate Date")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        final Date time0 = caclulate_date.getStartTime();
        final Date time1 = caclulate_date.getFireTimeAfter(time0);
        System.out.println("startTime: " + format.format(time0));
        System.out.println("nextTime: " + format.format(time1));
        return time1;
    }

    // 获取执行开始时间的 下一次执行点
    /*
       触发器获取下次执行时间点,都是基于当前时间操作的, 故会得到基于当前时间的上次执行时间
     */
    public static Date getNextExecuteTimeWithTriggerCustimeStartTime(String cron,Date startTime){
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronTrigger caclulate_date = TriggerBuilder.newTrigger().withIdentity("Caclulate Date")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        final Date time1 = caclulate_date.getFireTimeAfter(startTime);
        System.out.println("startTime: " + format.format(startTime));
        System.out.println("nextTime: " + format.format(time1));
        return time1;
    }

    // 当前时间对应的上次执行点
    public static Date getLastRunTimeWithTriggerNow(String cron){
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("caclulate time")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        final Date sTime = trigger.getStartTime();
        final Date time1 = trigger.getFireTimeAfter(sTime);
        final Date time2 = trigger.getFireTimeAfter(time1);
        final Date time3 = trigger.getFireTimeAfter(time2);
        final Date from = Date.from(Instant.ofEpochMilli(time1.getTime() - (time3.getTime() - time2.getTime())));
        System.out.println("sTime: " + format.format(sTime));
        System.out.println("Time1: " + format.format(time1));
        System.out.println("Time2: " + format.format(time2));
        System.out.println("Time3: " + format.format(time3));
        System.out.println("from: " + format.format(from));
        return from;
    }
    // 获取指定时间的上一次执行点
    /*
     使用trigger计算的下次执行时间点 会基于当前时间进行计算;故计算指定了开始时间,其计算的同样是基于当前时间点的下次执行时间
     */
    public static Date getLastRunTimeTriggerCusTimeStartTime(String cron,Date startTime){
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("caclulate time")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        final Date time1 = trigger.getFireTimeAfter(startTime);
        final Date time2 = trigger.getFireTimeAfter(time1);
        final Date time3 = trigger.getFireTimeAfter(time2);
        final Date from = Date.from(Instant.ofEpochMilli(time1.getTime() - (time3.getTime() - time2.getTime())));
        System.out.println("sTime: " + format.format(startTime));
        System.out.println("Time1: " + format.format(time1));
        System.out.println("Time2: " + format.format(time2));
        System.out.println("Time3: " + format.format(time3));
        System.out.println("lastTime: " + format.format(from));
        return from;
    }

    public static void main(String[] args) throws ParseException {
        /*Date start = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = format.parse("2021-02-03 10:41:00");
        start = parse;
        for (int i=0; i < 10; i++) {
            final Date next = getNextExecuteTime(CronTab, start);
            System.out.println("next time: "+ format.format(next));
            start = next;
        }*/
        Date parse = format.parse("2021-01-19 16:00:00");
        Date last1 = getLastExecuteTimeWithExpress(CronTab, parse);
        last1 = Date.from(LocalDateTime.ofInstant(last1.toInstant(),ZoneId.systemDefault()).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        Date last2 = getLastExecuteTimeWithExpress(CronTab, last1);
        last2 = Date.from(LocalDateTime.ofInstant(last2.toInstant(),ZoneId.systemDefault()).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        final Date last3 = getLastExecuteTimeWithExpress(CronTab, last2);
        /*System.out.println("getNextExecuteTimeWithTrigger 使用当前时间: ");
        getNextExecuteTimeWithTrigger(CronTab);
        System.out.println("----------------------------------");

        System.out.println("getNextExecuteTime 指定开始时间的下次执行点: ");
        getNextExecuteTime(CronTab,parse);
        System.out.println("----------------------------------");


        System.out.println("getNextExecuteTimeWithTriggerCustimeStartTime  指定开始时间的下次执行点: ");
        getNextExecuteTimeWithTriggerCustimeStartTime(CronTab, parse);
        System.out.println("----------------------------------");

        System.out.println("getLastRunTimeNow 当前时间的上次执行点");
        getLastRunTimeNow(CronTab);
        System.out.println("----------------------------------");

        System.out.println("getLastRunTimeCusTimeStartTime 指定开始时间的上次执行点: ");
        getLastRunTimeCusTimeStartTime(CronTab,parse);*/

    }
}
