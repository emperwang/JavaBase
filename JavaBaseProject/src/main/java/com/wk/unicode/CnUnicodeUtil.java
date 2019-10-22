package com.wk.unicode;

public class CnUnicodeUtil {
    /**
     *  unicode转中文
     * @return
     */
    public static String unicodeToCn(String unicode){
        String[] split = unicode.split("\\\\u");
        String result = "";
        for (int i = 1; i < split.length;i++){
            System.out.println(Integer.valueOf(split[i],16).intValue());
            System.out.println(Integer.valueOf(split[i],16));
            result += (char)Integer.valueOf(split[i],16).intValue();
        }

        return result;
    }

    /**
     * 中文转unicode
     */
    public static String cnToUnicode(String cn){
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0;i < chars.length; i++){
            returnStr += "\\u" + Integer.toString(chars[i],16);
        }
        return returnStr;
    }


    public static void main(String[] args) {
        String cn = "我爱中国";
        String str = cnToUnicode(cn);
        System.out.println(str);

        String s = unicodeToCn(str);
        System.out.println(s);
    }
}
