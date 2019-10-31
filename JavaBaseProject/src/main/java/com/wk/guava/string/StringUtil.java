package com.wk.guava.string;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static void StrUtil(){
        List<Integer> lists = Arrays.asList(1, 2, 3, 4, 5, 6);
        String join = Joiner.on(".").skipNulls().join(lists.toArray());
        System.out.println(join);
        String s = Joiner.on("..").appendTo(new StringBuilder(), lists.toArray()).append(100).append(200).toString();
        System.out.println(s);
    }

    public static void Spliterutil(){
        Iterable<String> split = Splitter.on(",")
                .trimResults()      // 去除左右的空格
                .omitEmptyStrings() //  消除空字符串
                .split("the ,quick, , brown         , fox,              jumps, over, the, lazy, little dog");
        split.forEach(val -> {
            System.out.print(" " + val);
        });
    }

    public static void MatcherUtil(){
        System.out.println(CharMatcher.ascii().retainFrom("mahesh123"));
        System.out.println(CharMatcher.whitespace().trimAndCollapseFrom("     Mahesh     Parashar ", ' '));

        System.out.println(CharMatcher.anyOf("123456789").retainFrom("test123"));
        System.out.println(CharMatcher.noneOf("123").retainFrom("test123"));
        System.out.println(CharMatcher.none().retainFrom("tesdt123"));
        System.out.println(CharMatcher.anyOf("123").or(CharMatcher.anyOf("789")).retainFrom("test789"));
    }

    /** 格式              example
     * LOWER_CAMEL      lowerCamel
     * LOWER_HYPHEN     lower-hyphen
     * LOWER_UNDERSCORE     lower_underscore
     * UPPER_CAMEL          UpperCamel
     * UPER_UNDERSCORE      UPPER_UNDERSCORE
     */
    // 格式转换
    public static void CaseFormatUtil(){
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL,"test-data"));
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,"test_data"));
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE,"test_data"));
    }

    public static void main(String[] args) {
//        StrUtil();
//        Spliterutil();
//        MatcherUtil();
        CaseFormatUtil();
    }
}
