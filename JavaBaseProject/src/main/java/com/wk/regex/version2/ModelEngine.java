package com.wk.regex.version2;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelEngine {
    public static void main(String[] args) {
        String template = "Hi {name} ,your code is {code}";
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","zhangsan");
        map.put("code","12345345");
        engineOne(template,map);
        System.out.println(" ====================================================== ");
        String templates = "收到活动告警，告警标题：$alarmTitle，告警级别：$origSeverity，告警发生时间：$eventTime ," +
                "   please check ....";
        Map<String,Object> res = new HashMap<>();
        res.put("alarmTitle","alarmTitleVal");
        res.put("origSeverity","origSeverityVal");
        res.put("eventTime","eventTimeVal");
        enginTwo(templates,res);
    }

    public static void enginTwo(String strs,Map<String,Object> res){
        String regex = "\\$\\w+";
        Pattern engine = Pattern.compile(regex);
        Matcher matcher = engine.matcher(strs);
        StringBuffer builder = new StringBuffer();
        while (matcher.find()){
            String key = matcher.group();
            System.out.println("key = " + key);
            Object values = res.get(key.substring(1));
            String val = Matcher.quoteReplacement(values.toString());
            matcher.appendReplacement(builder,val);
            System.out.println(builder.toString());
        }
        matcher.appendTail(builder);
        System.out.println("last Result : " + builder.toString());
    }

    public static void engineOne(String template,Map<String,Object> params){
        String regex = "\\{(\\w+)\\}";
        StringBuffer builder= new StringBuffer();
        Pattern engine = Pattern.compile(regex);
        Matcher matcher = engine.matcher(template);

        while (matcher.find()){
            String key = matcher.group(1);
            System.out.println(key);
            Object obj = params.get(key);
            String value = Matcher.quoteReplacement(obj.toString());
            matcher.appendReplacement(builder,value);
        }

        matcher.appendTail(builder);
        System.out.println(builder.toString());

    }
}
