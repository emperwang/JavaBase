package com.wk.analyseFile.ems;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: wk
 * @Date: 2021/2/3 14:30
 * @Description
 */

@Slf4j
public class EmsFileAnalyse {
    public static String efilePath="C:\\work\\collectorFiles\\ems\\pm\\4100ERHX1";
    public static final String timeStart="2021011913";
    public static final String timeSEnd="2021011915";
    static final  String Separa = File.separator;
    static final String timeCron="0 11,26,41,56 * * * ?";
    static final DateTimeFormatter dirFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    static final DateTimeFormatter nameFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    static final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static  final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static  final SimpleDateFormat dirFormat = new SimpleDateFormat("yyyyMMddHH");
    static  final SimpleDateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    static final String sTime = "2021-01-19 16:00:00";
    static final int minutes = 26;

    public static void analyseLastBatch() throws ParseException {
        final Date now = new Date();
        final Date lastTime = getLastExecuteTime(timeCron, now);
        final Date tmp = Date.from(LocalDateTime.ofInstant(lastTime.toInstant(), ZoneId.systemDefault()).minusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
        if (!efilePath.endsWith(Separa)){
            efilePath = efilePath+Separa;
        }
        String filePath = efilePath+dirFormat.format(tmp);
        log.info("now: {},lastTime: {},analyse batch time: {}, filePath:{}" ,format.format(now), format.format(lastTime), format.format(tmp),filePath);
        final File file = new File(filePath);
        if (file.exists()){
            final String[] list = file.list(new emsNameFilter(nameFormat.format(tmp)));
            log.info("file num: {} " ,list.length);
            if (list == null || list.length <= 0){
                generateTask(tmp);
            }
        }else{
            // generate supplement task
            generateTask(tmp);
        }
    }

    public static void analyseLast12Hour() throws ParseException {
        // 1. 获取当前时间的前一次 collect时间
        final Date sTime = format.parse(EmsFileAnalyse.sTime);
        final LocalDateTime time = LocalDateTime.ofInstant(sTime.toInstant(), ZoneId.systemDefault());
        final LocalDateTime eTime = time.minusHours(12);
        log.info("sTime: {}, eTime:{} ", outputFormat.format(time), outputFormat.format(eTime));
        final Date endTime = Date.from(eTime.atZone(ZoneId.systemDefault()).toInstant());
        // 2. 迭代判断是否 存在对应的
        Date last = getLastExecuteTime(timeCron, sTime);
        // 2.1 get dir
        if (!efilePath.endsWith(Separa)){
            efilePath = efilePath+Separa;
        }
        while (last.after(endTime)){
            log.info("analyse time: {}", format.format(last));
            final Date tmp = Date.from(LocalDateTime.ofInstant(last.toInstant(), ZoneId.systemDefault()).minusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
            String filePath = efilePath+dirFormat.format(tmp);
            log.info("analyse batch time: {},filePath:{}", format.format(tmp), filePath);
            final File file = new File(filePath);
            if (file.exists()){
                final String[] list = file.list(new emsNameFilter(nameFormat.format(tmp)));
                log.info("file num:  {}" ,list.length);
                if (list == null || list.length <= 0){
                   generateTask(tmp);
                }
            }else{
                // generate supplement task
                generateTask(tmp);
            }
            last = Date.from(LocalDateTime.ofInstant(last.toInstant(),ZoneId.systemDefault()).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
            last = getLastExecuteTime(timeCron, last);
        }

    }

    public static void generateTask(Date time){
        final Date to = Date.from(LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
        log.info("task startTime: {}, eTime:{}",format.format(time), format.format(to));
    }

    static class emsNameFilter implements FilenameFilter {
        private String pat;
        emsNameFilter(String patter){
            this.pat = patter;
        }

        @Override
        public boolean accept(File dir, String name) {
            if (name.contains(pat) && !name.endsWith("tmp")){
                return true;
            }
            return false;
        }
    }
    public static Date getNextExecuteTime(String cron, Date startTime) throws ParseException {
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronExpression expression = new CronExpression(cron);
        final Date next = expression.getNextValidTimeAfter(startTime);
        return next;
    }

    public static Date getLastExecuteTime(String cron, Date startTime) throws ParseException {
        if (! CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronExpression expression = new CronExpression(cron);
        final Date next = expression.getNextValidTimeAfter(startTime);
        final Date next1 = expression.getNextValidTimeAfter(next);
        final Date next2 = expression.getNextValidTimeAfter(next1);
        final Date last = Date.from(Instant.ofEpochMilli(next.getTime() - (next2.getTime() - next1.getTime())));
        return last;
    }

    public static void main(String[] args) throws ParseException {
        analyseLast12Hour();
        log.info("-----------------------------------------");
        analyseLastBatch();

    }
}
