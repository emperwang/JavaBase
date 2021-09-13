package com.wk.MappedFile;

import com.google.inject.internal.cglib.core.$Customizer;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Sparks
 * @Date: 2021/9/13 18:52
 * @Description 通过 mappedFile 来读写文件
 *  1. 写文件格式为: 内容长度  内容
 *  2. 读文件的方式: 内容长度  内容
 *  文件名字:
 *      name_2021-09-06_0
 *  文件目录:
 *      通过BaseDataFolder来进行设置, 如果没有设置,则使用 user.dir 目录
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static String pattern="yyyy-MM-dd";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    // 默认文件大小64M, 大于64M, 文件滚动
    private static int size = 64 * 1024 * 1024;

    private static String BaseDataFolder = System.getProperty("BaseDataFolder",System.getProperty("user.dir"));

    private static String BASENAME = "%s_%s_%s";
    private static String FilterFormat = "%s_%s";

    public static String generateFileName(String filename){
        LocalDate now = LocalDate.now();
        String time = formatter.format(now);
        int res = getFileIndex(filename);
        String curName = String.format(BASENAME, filename,time, res);
        return curName;
    }

    private static int getFileIndex(String filename) {
        String time = formatter.format(LocalDate.now());
        String filterName = String.format(FilterFormat, filename, time);
        File file = new File(BaseDataFolder);
        File[] files = file.listFiles(new NameFilter(filterName));
        Optional<Integer> first = Arrays.stream(files).map(f -> f.getName()).map(name -> name.split("_")).filter(arr -> arr.length == 3).map(arr -> Integer.parseInt(arr[2]))
                .sorted(Comparator.reverseOrder()).findFirst();
        return first.isPresent()?first.get():0;
    }

    public static void unmap(MappedByteBuffer buf){
        Cleaner cleaner = ((DirectBuffer) buf).cleaner();
        if (cleaner != null){
            cleaner.clean();
        }
    }

    public static void deleteFiles(String fileName){
        String[] list = new File(BaseDataFolder).list();
        List<File> files = Arrays.stream(list).filter(name -> name.contains(fileName)).map(name -> new File(BaseDataFolder, name)).collect(Collectors.toList());
        files.forEach(file -> {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static String getFilterName(String name){
        String filterName = String.format(FilterFormat, name, formatter.format(LocalDate.now()));
        return filterName;
    }


    public static void write(String fileName,String content) {
        if (StringUtils.isEmpty(content)) return;
        String formatName = generateFileName(fileName);
        RandomAccessFile accessFile = null;
        MappedByteBuffer buffer= null;
        try {
            accessFile = new RandomAccessFile(new File(BaseDataFolder, formatName),"rw");
            buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE,0,size);
            int len = buffer.getInt();
            while (len > 0 && (len + buffer.position() < buffer.capacity())){
                buffer.position(buffer.position()+len);
                len = buffer.getInt();
            }
            buffer.position(buffer.position()-4);
            int remaining = buffer.remaining();
            if (remaining <= content.length()-4){
                // 文件滚动
                buffer = rollFile(accessFile,fileName, buffer);
            }
            buffer.putInt(content.length());
            buffer.put(content.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (buffer != null) {
                    buffer.force();
                    if (accessFile != null) {
                        accessFile.close();
                    }
                    unmap(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static MappedByteBuffer rollFile(RandomAccessFile accessFile, String fileName, MappedByteBuffer buffer) throws IOException {
        if (accessFile != null){
            buffer.force();
            accessFile.close();
            unmap(buffer);
        }
        int fileIndex = getFileIndex(fileName);
        String newName = String.format(BASENAME, fileName, formatter.format(LocalDate.now()), ++fileIndex);
        accessFile = new RandomAccessFile(new File(BaseDataFolder,newName),"rw");
        MappedByteBuffer buf = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);
        accessFile.close();
        return buf;
    }

    public static String read(String fileName, int index){
        String name = generateFileName(fileName);
        RandomAccessFile accessFile = null;
        MappedByteBuffer buffer = null;
        try {
            accessFile = new RandomAccessFile(new File(BaseDataFolder,name),"r");
            buffer= accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE,0, size);
            int idxlen = buffer.getInt();
            int idx = 0;
            while (idxlen > 0 && idx < index){
                idx++;
                buffer.position(buffer.position()+idxlen);
                idxlen = buffer.getInt();
            }
            if (idx < idx && idxlen == 0) return StringUtils.EMPTY;

            buffer.position(buffer.position() - 4);
            idxlen = buffer.getInt();
            ByteBuffer allocate = ByteBuffer.allocate(idxlen);
            buffer.get(allocate.array());
            return new String(allocate.array());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (accessFile != null){
                    accessFile.close();
                    if (buffer != buffer){
                        buffer.force();
                        unmap(buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.EMPTY;
    }

    public static List<String> read(String fileName){
        String name = generateFileName(fileName);
        File file = new File(BaseDataFolder, name);
        return read(file);
    }

    public static List<String> read(File file){
        List<String> lists = new ArrayList<>();
        RandomAccessFile accessFile = null;
        MappedByteBuffer buffer = null;
        try {
            accessFile = new RandomAccessFile(file,"r");
            buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_ONLY,0, size);
            int length = buffer.getInt();
            while (length > 0){
                ByteBuffer allocate = ByteBuffer.allocate(length);
                buffer.get(allocate.array());
                lists.add(new String(allocate.array()));
                length = buffer.getInt();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (accessFile != null) {
                    accessFile.close();
                    if (buffer != buffer){
                        buffer.force();
                        unmap(buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }


    public static List<String> readAll(String fileName){
        List<String> lists = new ArrayList<>();
        File file = new File(BaseDataFolder);
        if (file.exists() && file.isDirectory()){
            String[] list = file.list(new NameFileFilter(fileName));
            Arrays.stream(list).forEach(f -> lists.addAll(read(f)));
        }
        return lists;
    }

    public static Set<String> readDistinct(String fileName){
        Set<String> lists = new HashSet<>();
        lists.addAll(readAll(fileName));
        return lists;
    }


    static class NameFilter implements FileFilter{

        private String nameFilter;

        public NameFilter(String name){
            this.nameFilter = name;
        }

        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().contains(nameFilter);
        }
    }

}
