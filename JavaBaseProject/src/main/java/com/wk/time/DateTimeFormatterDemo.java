package com.wk.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 17:58 2019/12/18
 * @modifier:
 */
public class DateTimeFormatterDemo {

    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String time = "2012-09-12 12:12:50";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate parse = LocalDate.parse(time, formatter);
        String s = parse.toString();
        System.out.println(s);

        String format = LocalDateTime.now().format(formatter);
        System.out.println(format);

        LocalDateTime parse1 = LocalDateTime.parse(time, formatter);
        System.out.println(parse1.toString());
    }
}
