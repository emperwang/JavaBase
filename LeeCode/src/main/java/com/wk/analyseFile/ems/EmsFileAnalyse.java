package com.wk.analyseFile.ems;

import com.wk.analyseFile.AbstractFileAnalyse;
import com.wk.analyseFile.NameFilter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: wk
 * @Date: 2021/2/3 14:30
 * @Description
 */

@Slf4j
public class EmsFileAnalyse extends AbstractFileAnalyse {
    public String efilePath="C:\\work\\collectorFiles\\ems\\pm\\4100ERHX1";
    private final String timeStart="2021011913";
    private final String timeSEnd="2021011915";
    private final String timeCron="0 11,26,41,56 * * * ?";
    //private final DateTimeFormatter dirFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    //private final DateTimeFormatter nameFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    //private final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private final SimpleDateFormat dirFormat = new SimpleDateFormat("yyyyMMddHH");
//    private final SimpleDateFormat nameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final String ssTime = "2021-01-19 16:00:00";
    private final int minutes = 26;
    EmsFileAnalyse(){
        setFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        setDirFormat(new SimpleDateFormat("yyyyMMddHH"));
        setNameFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
        setFilePath(efilePath);
    }

    @Override
    public void analyseLastBatch() throws Exception {
        final Date now = new Date();
        final Date lastTime = getLastExecuteTime(timeCron, now);
        final Date tmp = Date.from(LocalDateTime.ofInstant(lastTime.toInstant(), ZoneId.systemDefault()).minusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
        if (!efilePath.endsWith(Separa)){
            efilePath = efilePath+Separa;
        }
        analyse(tmp);
    }

    @Override
    public void analyseHistoryHour() throws Exception {
        final Date sTime = format.parse(ssTime);
        final LocalDateTime time = LocalDateTime.ofInstant(sTime.toInstant(), ZoneId.systemDefault());
        final LocalDateTime eTime = time.minusHours(12);
        final Date endTime = Date.from(eTime.atZone(ZoneId.systemDefault()).toInstant());
        log.info("sTime: {}, eTime:{} ", format.format(time), format.format(endTime));
        Date last = getLastExecuteTime(timeCron, sTime);
        if (!efilePath.endsWith(Separa)){
            efilePath = efilePath+Separa;
        }
        while (last.after(endTime)){
            log.info("analyse time: {}", format.format(last));
            final Date tmp = Date.from(LocalDateTime.ofInstant(last.toInstant(), ZoneId.systemDefault()).minusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
            analyse(tmp);
            last = Date.from(LocalDateTime.ofInstant(last.toInstant(),ZoneId.systemDefault()).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
            last = getLastExecuteTime(timeCron, last);
        }
    }

    @Override
    public void analyse(Date tmp) throws Exception {
        String filePath = efilePath+dirFormat.format(tmp);
        log.info("analyse batch time: {},filePath:{}", format.format(tmp), filePath);
        final File file = new File(filePath);
        if (file.exists()){
            final String[] list = file.list(new NameFilter(nameFormat.format(tmp)));
            String files = Arrays.toString(list);
            log.info("file num:  {}, files: {}" ,list.length,files);
            if (list == null || list.length <= 0){
                generateTask(tmp);
            }
        }else{
            // generate supplement task
            generateTask(tmp);
        }
    }






    public static void main(String[] args) throws Exception {
        EmsFileAnalyse fileAnalyse = new EmsFileAnalyse();
        fileAnalyse.analyseHistoryHour();
        log.info("-----------------------------------------");
        fileAnalyse.analyseLastBatch();

    }
}
