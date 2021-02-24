package com.wk.analyseFile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author: ekiawna
 * @Date: 2021/2/23 16:17
 * @Description
 */
@Slf4j
@Setter
@Getter
public abstract class AbstractFileAnalyse implements FileAnalyse {
    public SimpleDateFormat format;
    public SimpleDateFormat dirFormat;
    public SimpleDateFormat nameFormat;
    public final  String Separa = File.separator;
    public String filePath;

    @Override
    public void generateTask(Date time) throws Exception{
        final Date to = Date.from(LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
        log.info("task startTime: {}, eTime:{}",format.format(time), format.format(to));
    }

    @Override
    public Date getLastExecuteTime(String cron, Date startTime) throws Exception {
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

    @Override
    public Date getLastExecuteTime(Date lTime,int period) throws Exception {
        LocalDateTime dateTime = LocalDateTime.ofInstant(lTime.toInstant(), ZoneId.systemDefault());
        int minute = dateTime.getMinute();
        minute = minute / period * period;
        LocalDateTime now = dateTime.withMinute(minute);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }


    public  Date getNextExecuteTime(String cron, Date startTime) throws Exception {
        if (!CronExpression.isValidExpression(cron)){
            return null;
        }
        final CronExpression expression = new CronExpression(cron);
        final Date next = expression.getNextValidTimeAfter(startTime);
        return next;
    }

    public abstract void analyse(Date tmp) throws Exception;
}
