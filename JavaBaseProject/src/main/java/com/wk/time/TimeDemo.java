package com.wk.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeDemo {
    public static void main(String[] args) {
       // InstantDemo();
        //LocalDataDemo();
        //LocalTimeDemo();
        //LocalDateTimeDemo();
        //calendarDemo();
        dataFormat();
    }

    public static void LocalDataDemo(){
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        System.out.println("year is "+year);

        Month month = now.getMonth();
        int i1 = month.get(ChronoField.MONTH_OF_YEAR);
        System.out.println("month is "+i1);

        int monthValue = now.getMonthValue();
        System.out.println("monthValue " + monthValue);

        int dayOfYear = now.getDayOfYear();
        int dayOfMonth = now.getDayOfMonth();
        System.out.println("dayOfYear " + dayOfYear);
        System.out.println("dayOfMonth " + dayOfMonth);

        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int i = dayOfWeek.get(ChronoField.DAY_OF_WEEK);
        System.out.println("dayOfWeek " + dayOfWeek.toString() + " dayOfWeek is "+i);
    }

    public static void LocalDateTimeDemo(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int month = now.getMonth().get(ChronoField.MONTH_OF_YEAR);
        int dayOfYear = now.getDayOfYear();
        int dayOfMonth = now.getDayOfMonth();
        int week = now.getDayOfWeek().get(ChronoField.DAY_OF_WEEK);
        System.out.println("year is :"+year +" monthValue :"+monthValue + " month: " +
                ""+month+"  dayOfYear:  "+dayOfYear + " dayofmonth :"+dayOfMonth +" week :"+week);
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        System.out.println("hour: " +hour +" minute: "+minute +" second :"+second);

        // 设置时间为 2020-12-22 23:12:12
        now.withYear(2020);
        now.withMonth(12);
        now.withDayOfMonth(22);
        now.withHour(23);
        now.withMonth(12);
        now.withSecond(12);
    }

    public static void LocalTimeDemo(){
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int nano = now.getNano();
        System.out.println("hour + "+hour+ ", minute is "+minute +", second "+second+", nano "+nano);

    }

    /**
     *  瞬时 时间
     */
    public static void InstantDemo(){
        Instant now = Instant.now();
        int nano = now.getNano();
        System.out.println("instant nano is :"+nano);

        long epochSecond = now.getEpochSecond();
        System.out.println("instant epochSecond is "+epochSecond);

        String s = now.toString();
        System.out.println("instant string is "+s);

        int month = now.get(ChronoField.NANO_OF_SECOND);
        System.out.println("instant month is "+month);
    }

    /**
     *  日历类
     */
    public static void calendarDemo(){
        Calendar instance = Calendar.getInstance();
        String calendarType = instance.getCalendarType();
        Date time = instance.getTime();
        TimeZone timeZone = instance.getTimeZone();

        System.out.println("calendarType :" + calendarType +" timeZone:"+timeZone);
    }

    /**
     *  时间格式转换  多线程安全
     */
    public static void dataFormat(){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        TemporalAccessor parse = formatter.parse("2019-09-08");
        int i = parse.get(ChronoField.YEAR);
        int i2 = parse.get(ChronoField.MONTH_OF_YEAR);
        int i1 = parse.get(ChronoField.DAY_OF_MONTH);

        System.out.println("year = "+i + ", month = "+i2 + ", day = "+i1);
    }

    public static void toBeijingInstant(){
        LocalDateTime now = LocalDateTime.now();
        // 转换为北京时间
        Instant instant = now.toInstant(ZoneOffset.of("+08:00"));

        ZoneId bjZone = ZoneId.of("GMT+08:00");
    }
}
