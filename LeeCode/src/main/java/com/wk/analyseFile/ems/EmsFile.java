package com.wk.analyseFile.ems;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: wk
 * @Date: 2021/1/18 18:06
 * @Description
 */
public class EmsFile {
    public static String efilePath="C:\\work\\collectorFiles\\ems\\pm\\4100ERHX1";
    public static final String timeStart="2021011909";
    public static final String timeSEnd="2021011911";
    static final  String Separa = File.separator;
    static final String timeCron="0 11,26,41,56 * * * ?";
    static String pattern = "yyyyMMddHH";
    static final SimpleDateFormat format = new SimpleDateFormat(pattern);
    // 延迟多久去进行扫描(应该是一个范围)
    static final int configDelay = 2;
    static final int collectInterval = 15;
    static final int TaskPeriod = 15;

    public void getCommonFilesize() throws ParseException {
        int check = 12;
        final String[] s = timeCron.split(" ");
        final String[] split = s[1].split(",");
        Arrays.sort(split);
        Date sTime = format.parse(timeStart);
        List<String> emptyFile = new ArrayList<>();
        List<Integer> dirSize = new ArrayList<>();
        Map<Integer,String> size2File = new HashMap<>();
        // get dir file size
        for (int i=0; i<check; i++){
            final File fileDir = new File(efilePath, format.format(sTime));
            if (fileDir.exists() && fileDir.listFiles().length>0){
                dirSize.add(fileDir.listFiles().length);
                size2File.put(fileDir.listFiles().length,format.format(sTime));
            }else{
                emptyFile.add(format.format(sTime));
            }
            sTime = Date.from(LocalDateTime.ofInstant(sTime.toInstant(), ZoneId.systemDefault()).plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        }
        // get common dir size
        int dirCom = getCommon(dirSize);
        System.out.println("common dir size: " + dirCom);
        // get batch size
        System.out.println("batch size: " + (dirCom / split.length));
        int batchSize = dirCom / split.length;
    }

    public void analyse(int[] ints,int batchSize) throws ParseException {
        final LocalDateTime now = LocalDateTime.now();
        System.out.println("now: " + now.toString());
        LocalDateTime startTime = fixTime(now,ints);
        System.out.println("fix :" +startTime);
        LocalDateTime endTime = startTime.minusHours(12);
        final DateTimeFormatter dirFormat = DateTimeFormatter.ofPattern(pattern);
        final DateTimeFormatter nameFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        final DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        while (startTime.isAfter(endTime)){
            if (!efilePath.endsWith(Separa)){
                efilePath = efilePath + Separa;
            }
            String epath = efilePath + dirFormat.format(startTime);
            final File file = new File(epath);
            if (file.exists()){
                System.out.println(epath +"  exist..");
                final String[] names = file.list(new FileNameFileter(nameFormat.format(startTime)));
                if (names == null || names.length < batchSize){
                    System.out.println("file exist, task time: " + outputFormat.format(startTime));
                }
                startTime = startTime.minusMinutes(TaskPeriod);
            }else{
                // 某个目录不存在,则补采对应的4个时间点
                System.out.println(epath +"  does not exist..");
                LocalDateTime tmp = startTime.withMinute(0);
                LocalDateTime eTime = tmp.plusHours(1);
                while (tmp.isBefore(eTime)){
                    System.out.println("task time: " + outputFormat.format(tmp));
                    tmp = tmp.plusMinutes(TaskPeriod);
                }
                startTime = startTime.withMinute(0).minusMinutes(TaskPeriod);
            }
        }
    }

    public LocalDateTime fixTime(LocalDateTime time,int[] ints){
        //final int[] ints = {11, 26, 41, 56};
        if (ints == null ||ints.length <=0){
            System.out.println("ints must be given..");
        }
        //time = time.withMinute(56);
        if (ints.length == 1){
            System.out.println("ints length == 1");
            return time;
        }
        final int minute = time.getMinute();
        if (minute >= ints[ints.length-1]){
            time = time.withMinute(ints[ints.length-1]);
            time = time.minusMinutes(26);
            //System.out.println(time.toString());
            return time;
        }
        for (int i= 1; i <ints.length; i++){
            if (minute <= ints[i]){
                time = time.withMinute(ints[i-1]);
                time = time.minusMinutes(26);
                //System.out.println(time.toString());
                return time;
            }
        }
        return time;
    }

    public int getCommon(List<Integer> dirSize){
        int common = dirSize.get(0);
        int count=0;
        for (Integer integer : dirSize) {
            if (count == 0){
                common = integer;
            }
            count += (common == integer ? 1:-1);
        }
        return common;
    }

    public static void printArray(int[] t){
        for (int i : t) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    class FileNameFileter implements FilenameFilter{
        String fileName = "";
        FileNameFileter(String pattern){
            this.fileName = pattern;
        }
        @Override
        public boolean accept(File dir, String name) {
            if (name!=null && name.length()> 0 && name.contains(fileName)){
                return true;
            }
            return false;
        }
    }

    class FileFilterImpl implements FileFilter{
        String fileName = "";
        FileFilterImpl(String fileName){
            this.fileName = fileName;
        }
        @Override
        public boolean accept(File pathname) {
            final String name = pathname.getName();
            if (name.contains(fileName)){
                return true;
            }
            return false;
        }
    }


    public static void main(String[] args) throws ParseException {
        final EmsFile emsFile = new EmsFile();
        emsFile.getCommonFilesize();
        final int[] t1 = {11,26,41,56};
        Arrays.sort(t1);
        //printArray(t1);
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime tmp = now.plusHours(0);
//        System.out.println(tmp.toString());
//        tmp = tmp.plusHours(10);
//        System.out.println(now.toString());
//        System.out.println(tmp.toString());
        //emsFile.fixTime(LocalDateTime.now(),t1);
        emsFile.analyse(t1,13);
    }
}
