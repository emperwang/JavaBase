package com.wk.util;

import java.util.StringJoiner;

/**
 * @author: Sparks
 * @Date: 2021/7/19 18:47
 * @Description
 */
public class StringUtil {
    public static String generateStr(String str){
        return new StringJoiner("---").add(str).add("success").toString();
    }
}
