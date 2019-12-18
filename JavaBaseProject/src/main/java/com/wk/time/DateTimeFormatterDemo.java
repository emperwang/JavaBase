package com.wk.time;

import net.sf.cglib.core.Local;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 17:58 2019/12/18
 * @modifier:
 */
public class DateTimeFormatterDemo {

    private static Map<String,DateTimeFormatter> formatters;
    static {
        formatters = new ConcurrentHashMap<>();
    }

    /**
     *  data 转换位 localDate
     * @param date
     * @return
     */
    public LocalDate dateToLocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     *  date 转换位LocalDate
     * @param date
     * @param zoneId
     * @return
     */
    public LocalDate dateToLocalDate(Date date, ZoneId zoneId){
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     *  date转换位Localtime
     * @param date
     * @param zoneId
     * @return
     */
    public LocalTime dateToLocalTime(Date date, ZoneId zoneId){
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalTime();
    }

    public LocalTime dateToLocalTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalTime();
    }

    /**
     *  date 转换为 LocalDateTime
     * @param date
     * @return
     */
    public LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime;
    }

    public LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId){
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant,zoneId);
    }

    /**
     *  localDateTime 转换为  data
     * @param localDateTime
     * @return
     */
    public Date localDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date from = Date.from(instant);
        return from;
    }

    /**
     *  LocalDateTime 转换为 String
     */
    public String localDateTimeToString(LocalDateTime localDateTime, String pattern){
        DateTimeFormatter formatter = getFormatter(pattern);
        return localDateTime.format(formatter).toString();
    }

    /**
     *  localDate 转换为 Date
     * @param localDate
     * @return
     */
    public Date localDateToDate(LocalDate localDate){
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private DateTimeFormatter getFormatter(String pattern){
        if (formatters.containsKey(pattern)){
            return formatters.get(pattern);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
            formatters.put(pattern,formatter);

            return formatter;
        }
    }

    /**
     *  字符串转换为 localDateTime
     * @param time
     * @param pattern
     * @return
     */
    public LocalDateTime strToLocalDateTime(String time, String pattern){
        DateTimeFormatter formatter = getFormatter(pattern);
        return LocalDateTime.parse(time,formatter);
    }

    /**
     * 字符串转换位Date
     */
    public Date strToDate(String time, String pattern){
        LocalDateTime localDateTime = strToLocalDateTime(time, pattern);
        Date date = localDateTimeToDate(localDateTime);
        return date;
    }

    /**
     * Date 转换位字符串
     */
    public String dateToString(Date date, String pattern){
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        String s = localDateTimeToString(localDateTime, pattern);

        return s;
    }


    public void travelLocale(){
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale availableLocale : availableLocales) {
            System.out.println(availableLocale.toString());
        }
    }

    /**
     *  ZoneOffset就是时区之间的差距时间
     */
    public void zoneIdAndOffset(){
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset offset = OffsetDateTime.now().getOffset();
        System.out.println(offset.toString());
    }

    public void queryZoneId(){
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        for (String zoneId : zoneIds) {
            System.out.println("zoneId: " + zoneId);
        }
    }

    public static void main(String[] args) {
        DateTimeFormatterDemo formatterDemo = new DateTimeFormatterDemo();
        // formatterDemo.queryZoneId();
        System.out.println("default ZoneId: " + ZoneId.systemDefault());
        formatterDemo.zoneIdAndOffset();

        LocalDateTime localDateTime = formatterDemo.dateToLocalDateTime(new Date());
        System.out.println(localDateTime.toString());

        LocalDateTime localDateTime1 = formatterDemo.strToLocalDateTime("2019-09-09 12:12:05", "yyyy-MM-dd HH:mm:ss");
        System.out.println(localDateTime1.toString().replace("T"," "));

        String s = formatterDemo.dateToString(new Date(), "yyyy-MM-ddHHmmss");
        System.out.println(s);
        String ss = formatterDemo.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(ss);

        Date date = formatterDemo.strToDate("2019-12-18 21:42:53", "yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
    }
}
