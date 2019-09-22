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
