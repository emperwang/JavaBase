package com.wk.regex.commonregex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonRegex {
    public static void main(String[] args) {
        //regexMail();
        //regexPhoneNum();
        regexTelNum();
    }

    public static void regexMail(){
        String regex1 = "[0-9]{6}"; // 此匹配会匹配连续6个数组，如果时电话号码，也会进行匹配
        // 添加边界约束
        String regex2 = "(?<![0-9])[0-9]{6}(?![0-9])";

        String str = "邮编 100013 ,电话 18612345678";
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            String group = matcher.group();
            System.out.println(group);
        }
    }

    public static void regexPhoneNum(){
        String regex1 = "[0-9]{11}";  // 匹配1位数字
        String regex2 = "1[3457][0-9]{11}"; // 锁定第一位是1，第二位为3457中一个

        // 如果手机号为 186-1234-5678
        String regex3 = "1[3457]-?[0-9]{4}-?[0-9]{4}";
        // 如果手机号为 018612345678
        // +86 18612345678
        // 0086 18612345678   为了表达此种类型号码，添加前缀((0|\+86|0086)\s?)?
        String regex4 = "((0|\\+86|0086)\\s?)?1[34578][0-9]-?[0-9]{4}-?[0-9]{4}";   // 此处双斜杠是因为斜杠在java字符转属于元字符
        // 和邮编一样，需要添加边界修饰: 否定逆序环视   否定顺序环视
        String regex5="(?<![0-9])"+regex4+"(?![0-9])";

        Pattern pattern = Pattern.compile(regex5);
        String str = "your num is :18600269778 and 018600269778 and +8618600269778, but he is :122-2222-2222";

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
    // 固定电话
    public static void regexTelNum(){
        // 区号0-3，中间可能有连接符 区号是可选的
        // 010-6225678  (010)6225678
        // (\(?0[0-9]{2,3}-?\)?)?  此表示区号可选
        String regex = "(\\(?0[0-9]{2,3}-?\\)?)?[0-9]{7,8}";
        // 在加上左右环视,左边不能有数字，右边不能有数字
        String regex2 = "(?<![0-9])"+regex+"(?![0-9])";
        String str = "省电话:(010)6225678 ,市电话:010-6225678";
        Pattern compile = Pattern.compile(regex2);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }

    public static void regexDate(){
        // 一般的日期 :2017-06-21   2016-11-1
        String regex = "\\d{4}-\\d{2}-\\d{1,2}";
        // 但是一般月只有1-12，日只有1-31
        // 月份
        String month = "(0?[1-9]|1[0-2])";
        // 日
        String day = "(0?[1-9]|1[0-9]|2[0-9]|3[0-1])";
        String regex2 = "\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[01])";
        // 添加环视 , 也就是左右不能再有其他数字
        String regix3 = "(?<![0-9])"+regex2+"(?![0-9])";
    }

    public static void regexTime(){
        // 10:59  24小时制
        String regex1 = "\\d{2}:\\d{2}";  // 都取两位数字
        String hour = "([0-1][0-9]|2[0-3])";
        String minute = "(0?[0-9]|[1-5][0-9])";
        String regex2 = hour+minute;
        // 加上环视
        String regexFinal="(?<![0-9])"+regex2+"(?![0-9])";
    }

    /**
     *  第一代身份证只有15位，二代身份证是18位，都不能是0开头。二代身份证最后一位可能是x或X，其他都是数字
     */
    public static void regexCard(){
        String regex = "[1-9][0-9]{14}";  // 一代
        String regex2 = "[1-9][0-9]{16}[0-9xX]";
        // 合并一起
        String regex3 = "[1-9][0-9]{14}([0-9]{2}[0-9xX])?";
        // 加上左右边界
        String regexFinal = "(?<![0-9])"+regex3+"(?![0-9])";
    }

    public static void regexIp(){
        // 192.168.111.222
        String regex1 = "(\\d{1,3}\\.){3}\\d{1,3}";      // regex1 和 regex表达式是相同的
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 没有满足是在 0-255 之间的数字
        // 一位数字表达
        String one = "0{0,2}[0-9]"; // 0出现0到2此，第三位数字没有限制
        String two = "0?[0-9]{2}";
        String three1 = "1[0-9]{2}";
        String three2 = "2[0-4][0-9]";
        String three25 = "25[0-5]";
        // \d{3} 更准确的表达是:
        String regex2 = "(0{0,2}[0-9]|0?[0-9]{2}|1[0-9]{2}|2[0-4][0-9]|25[0-5]";
        // 加上左右环视
        String regexFinal = "(?<![0-9])("+regex2+"){3}"+regex2+"(?![0-9])";

    }

    public static void regexEmail(){
        // 要求:4-16个字符，可使用 英文小写、数字、下划线，但下划线不能在首尾
        // 123@sina.com
        String userName = "[a-z0-9][a-z0-9_]{2,14}[a-z0-9]";
        String regex = userName+"@sina\\.com";
    }

    public static void regexChinese(){
        // 一般中文字符的Unicode编码一般位于 \u4e00 ~ \u9fff 之间
        String chineseRegex = "[\\u4e00-\\u9fff]";
    }
}
