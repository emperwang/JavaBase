package com.wk.fcaps.socket.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangbqa
 */
public class DateUtil {
	
	final protected static ConcurrentHashMap<String, SimpleDateFormat> formatPatterns 
	    = new ConcurrentHashMap<String, SimpleDateFormat>();
	
	final public static Date parse(String dateStr, String pattern) {
		try {
			return dateFormatOf(pattern).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	final public static String format(Date date, String pattern) {
		return dateFormatOf(pattern).format(date);
	}
	
	final static protected SimpleDateFormat dateFormatOf(String pattern){
		SimpleDateFormat dateformat = null;
		if (!formatPatterns.containsKey(pattern)) {
			dateformat = new SimpleDateFormat(pattern);
			formatPatterns.put(pattern, dateformat);
		} else {
			dateformat = formatPatterns.get(pattern);
		}
		return dateformat; 
	}

	public static int compareTime(Date time1,Date time2){
		long diff = time1.getTime() - time2.getTime();
		int count = diff >= 0 ? (int) (diff / 1000) : (int) (Math.abs(diff) / 1000);
		return count;
	}

	public static Date getDateByFormat(String dateStr, String formatStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
		return localDateTime2Date(localDateTime);
	}

	public static String getFormatDateString(Date date,String formatStr) {
		LocalDateTime localDateTime = date2LocalDateTime(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		return localDateTime.format(formatter);
	}

	/**
	 * Date转换为LocalDateTime
	 * @param date
	 */
	public static LocalDateTime date2LocalDateTime(Date date){
		Instant instant = date.toInstant();
		return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * LocalDateTime转换为Date
	 * @param localDateTime
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime){
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	/**
	 * 根据传入得本地时间获得 获得 这个对应得UTC 时间
	 * @return
	 */
	public static Date date2UTCDate(Date date){
		Calendar cal = Calendar.getInstance();
		//获得时区和 GMT-0 的时间差,偏移量
		int offset = cal.get(Calendar.ZONE_OFFSET);
		//获得夏令时  时差
		int dstoff = cal.get(Calendar.DST_OFFSET);
		//(offset + dstoff):当前是UTC时区的时间毫秒值
		Date utcDate = new Date(date.getTime() - (offset + dstoff));
		return utcDate;
	}

}
