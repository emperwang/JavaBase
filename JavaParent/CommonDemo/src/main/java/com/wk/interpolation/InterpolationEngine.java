package com.wk.interpolation;

import com.wk.interpolation.test.Config;
import com.wk.json.JsonUtil;
import com.wk.resource.ResourceUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterpolationEngine {
    private Pattern pattern;
    private String variable = "var";
    private String format = "fmt";

    private InterpolationEngine(){
        this.pattern = Pattern.compile("\\$\\{(?<" + variable + ">[A-Za-z][0-9A-Za-z_]*?)(?::(?<" + format + ">.+?))?}");
    }

    /**
     *  解析字符
     * @param template  要解析的字符串
     * @param context  配置文件
     */
    public StringCombiner compile(String template,Object context){
        StringCombinerBuilder builder = StringCombinerBuilder.create();
        Matcher matcher = pattern.matcher(template);
        int start = 0;
        int end = template.length();
        while (matcher.find()){
            int s = matcher.start();   //上一个匹配的起始索引
            //before matched
            if(start < s){
                String substring = template.substring(start, s);
                builder.addConstantPart(substring);
                System.out.println("substring == "+substring);
            }
            String variableTmp = matcher.group(variable);
            String  formatTmp = matcher.group(format);
            //matched
            builder.addPart(context,variableTmp,formatTmp);
            start = matcher.end();
            System.out.println("variableTmp == "+variableTmp);
            System.out.println("formatTmp == "+formatTmp);
        }
        if(start < end){
            builder.addConstantPart(template.substring(start,end));
        }
        StringCombiner build = builder.build();
        return build;
    }

    public static InterpolationEngine create(){
        return new InterpolationEngine();
    }

   /* public static StringCombiner createCominer(String template,Object context){
        return create().
    }*/

    public static void main(String[] args) {
        String string = ResourceUtil.getUTF8String("smart-sms.json");
        Config config = JsonUtil.json2Bean(string, Config.class);
        InterpolationEngine interpolationEngine = InterpolationEngine.create();
        StringCombiner compile = interpolationEngine.compile("/hdfs/ica/test/zqdj/${dt:yyyyMMdd/HH}/${prefix}_${dt:yyyyMMddHHmm00}_${label}${suffix}",
                config.getHdfsSinkerConfig());
        compile.bind("dt",new Date());
        String combine = compile.combine();
        System.out.println(combine);
    }
}
