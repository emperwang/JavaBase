package com.wk.demo;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取一个网站中所有的网站，并保存在一个本地文件中，可以进行离线分析
 */
public class SpiderDemo1 {
    public static void main(String[] args) {
        URL url = null;
        URLConnection urlConnection = null;
        BufferedReader reader = null;
        PrintWriter pw = null;
        // \w 等效于[A-Za-z0-9_],\\w 表示就是w,加号-表示一个或多个,
        //url匹配规则
        String regix = "http://[\\w+\\.?/?]+\\.[A-Za-z]";
        Pattern pattern = Pattern.compile(regix);
        try{
            //要爬取的网站
            url = new URL("https://www.rndsystems.com/cn");
            //打开连接
            urlConnection = url.openConnection();
            //要写入到哪里
            pw = new PrintWriter(new FileWriter("D:/SiteUrl.txt"),true);
            //读取网站的内容
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String buf = null;
            while ((buf = reader.readLine())!=null){
                Matcher matcher = pattern.matcher(buf);
                while (matcher.find()){
                    //把匹配的写入到文件
                    pw.println(matcher.group());
                }
            }
            System.out.println("爬取成功__");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }

    }
}
