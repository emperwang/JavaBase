package com.wk.analyseFile.vim;

import com.wk.analyseFile.AbstractFileAnalyse;
import com.wk.analyseFile.NameFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

/**
 * @author: wk
 * @Date: 2021/1/18 18:07
 * @Description
 */
@Slf4j
public class vimFile extends AbstractFileAnalyse {
    public static String filePath="C:\\work\\collectorFiles\\vim\\pm\\NFV-RP-HNGZ-04A-ER-01";
    public static final String timeStart="20210114";
    public static final String timeSEnd="20210116";
    /*public final DateTimeFormatter dirFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
    public final DateTimeFormatter nameFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    public final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");*/
    /*public final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final SimpleDateFormat dirFormat = new SimpleDateFormat("yyyyMMdd");
    public final SimpleDateFormat nameFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");*/
    //public final String sTime = "2021-01-19 16:00:00";
    public final Integer pmPeriod = 15;

    public vimFile(){
        setFilePath(filePath);
        setFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        setDirFormat(new SimpleDateFormat("yyyyMMdd"));
        setNameFormat(new SimpleDateFormat("yyyyMMdd-HHmmss"));
    }

    @Override
    public void analyseLastBatch() throws Exception{
        if (!filePath.endsWith(Separa)){
            filePath += Separa;
        }
        Date lastTime = getLastExecuteTime(new Date(),pmPeriod);
        analyse(lastTime);
    }

    @Override
    public void analyseHistoryHour() throws Exception {
        Date endTime = dirFormat.parse(timeStart);
        Date startTime = dirFormat.parse(timeSEnd);
        if (!filePath.endsWith(Separa)){
            filePath += Separa;
        }
        while (startTime.after(endTime)){
            Date ltime = getLastExecuteTime(startTime,pmPeriod);
            log.info("history start time: {}", format.format(ltime));
            analyse(ltime);
            startTime = Date.from(LocalDateTime.ofInstant(ltime.toInstant(),ZoneId.systemDefault()).minusMinutes(pmPeriod).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @Override
    public void analyse(Date ltime) throws Exception {
        String fileDir = new StringBuilder().append(filePath).append(dirFormat.format(ltime)).toString();
        File file = new File(fileDir);
        if (file.exists()) {
            Date nameTime = Date.from(LocalDateTime.ofInstant(ltime.toInstant(),ZoneId.systemDefault()).minusHours(8).atZone(ZoneId.systemDefault()).toInstant());
            String[] list = file.list(new NameFilter(nameFormat.format(nameTime)));
            String flist = Arrays.toString(list);
            log.info("file list: {}", flist);
            if (list == null || list.length <= 0){
                // generate task
                generateTask(ltime);
            }
        }else { // file not exist
            // generate task
            generateTask(ltime);
        }
    }


    public static void main(String[] args) throws Exception {
        vimFile vimFile = new vimFile();
        vimFile.analyseHistoryHour();
    }
}
