package com.cnksi.android.utils;

import com.cnksi.android.log.KLog;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化工具类
 */
public class DateUtil {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String yyyy_MM_dd_HH_mm_ss = DEFAULT_PATTERN;
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static final String yyyy_MM_dd_HH_mm_ss2 = "yyyy/MM/dd HH:mm:ss";
    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    /**
     * yyyyMMdd_HHmmss
     */
    public static final String yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String MM_dd_HH_mm = "MM-dd HH:mm";
    /**
     * ssSSS
     */
    public static final String ssSSS = "ssSSS";

    private DateUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将毫秒数转换为String
     *
     * @param time 毫秒数
     * @return String yyyy/MM/dd HH:mm:ss
     */
    public static String longTimeToDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss2, Locale.CHINA);
        return formatter.format(new Date(time));
    }

    /**
     * 将毫秒数转换为String
     *
     * @param time
     * @param format String yyyy/MM/dd HH:mm:ss
     */
    public static String longTimeToDate(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(new Date(time));
    }

    /**
     * 得到当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentLongTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
        return formatter.format(new Date());
    }

    /**
     * 得到当前时间
     *
     * @return yyyy-MM-dd
     */
    public static String getCurrentShortTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd, Locale.CHINA);
        return formatter.format(new Date());
    }

    /**
     * 得到当前时间
     *
     * @param format 时间格式
     * @return String 时间字符串
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(new Date());
    }

    /**
     * 格式化时间
     *
     * @param timeStr
     * @return "yyyy-MM-dd"
     */
    public static String getFormatterTime(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd, Locale.CHINA);
        try {
            timeStr = formatter.format(formatter.parse(timeStr));
        } catch (Exception e) {
            timeStr = "";
        }
        return timeStr;
    }

    /**
     * 字符串转时间 将 yyyy-MM-dd HH:mm:ss 转换为其他格式的时间
     *
     * @param dateStr 需要转换的时间字符串
     * @param format  转出的时间格式
     * @return
     */
    public static String getFormatterTime(String dateStr, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat formatter2 = new SimpleDateFormat(format, Locale.CHINA);
            dateStr = formatter2.format(formatter.parse(dateStr, pos));
        } catch (Exception e) {
            dateStr = "";
        }
        return dateStr;
    }

    /**
     * 将时间转换为其他格式的时间
     *
     * @param date   需要转换的时间
     * @param format 转出的时间格式
     * @return
     */
    public static String getFormatterTime(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 计算两个时间的差
     *
     * @param startTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @param format       时间格式
     * @return 时间差  long
     */
    public static long getTimeDifferenceDays(String startTimeStr, String endTimeStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        long differenceTime = 0L;
        try {
            differenceTime = getTimeDifferenceDays(formatter.parse(startTimeStr), formatter.parse(endTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceTime;
    }

    /**
     * 计算两个时间的差
     *
     * @param startTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @return 时间差  long
     */
    public static long getTimeDifferenceDays(String startTimeStr, String endTimeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
        long differenceTime = 0L;
        try {
            differenceTime = getTimeDifferenceDays(formatter.parse(startTimeStr), formatter.parse(endTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceTime;
    }


    /**
     * 得到当前时间是星期几
     *
     * @return 星期X
     */
    public static String getCurrentWeekDay() {
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE", Locale.CHINA);
        return dateFm.format(new Date());
    }

    /**
     * 计算两个时间的差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差  long
     */
    public static long getTimeDifferenceDays(Date startTime, Date endTime) {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 计算两个时间的差
     *
     * @param startTimeStr 开始时间字符串   yyyy-MM-dd HH:mm:ss
     * @param endTimeStr   结束时间字符串   yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTimeDifference(String startTimeStr, String endTimeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
        String differenceTime = "";
        try {
            differenceTime = getTimeDifference(formatter.parse(startTimeStr), formatter.parse(endTimeStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return differenceTime;
    }

    /**
     * 计算两个时间的差
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static String getTimeDifference(Date startTime, Date endTime) {
        long l = endTime.getTime() - startTime.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        // long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuilder sbTime = new StringBuilder();
        if (day > 0) {
            sbTime.append(day).append("天");
        }
        if (hour > 0) {
            sbTime.append(hour).append("小时");
        }
        sbTime.append(min).append("分钟");
        return sbTime.toString();
    }

    /**
     * 判断 时间1 是否大于 时间2 如果大于 返回true
     *
     * @param dataFirst  时间1
     * @param dateSecond 时间2
     * @param format     时间格式
     * @return 如果大于 返回true
     */
    public static boolean compareDate(String dataFirst, String dateSecond, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
            Date dt1 = df.parse(dataFirst);
            Date dt2 = df.parse(dateSecond);
            return compareDate(dt1, dt2);
        } catch (Exception e) {
            KLog.e(e);
            return false;
        }
    }

    /**
     * 判断 时间1 是否大于 时间2 如果大于 返回true
     *
     * @param dateFirst  时间1
     * @param dateSecond 时间2
     * @return 如果大于 返回true
     */
    public static boolean compareDate(Date dateFirst, Date dateSecond) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(dateFirst);
        calendar2.setTime(dateSecond);
        int result = calendar1.compareTo(calendar2);
        return result >= 0;
    }

    /**
     * 得到当前时间多少天之前的时间
     *
     * @param days 天数
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getPreTime(int days) {
        return getPreTime(days, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 得到当前时间多少天之前的时间
     *
     * @param days      天数
     * @param formatter 时间格式
     * @return
     */
    public static String getPreTime(int days, String formatter) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return getFormatterTime(calendar.getTime(), formatter);
    }

    /**
     * 得到当前时间多少天之后的时间
     *
     * @param days 天数
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getAfterTime(int days) {
        return getAfterTime(days, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 得到当前时间多少天之后的时间
     *
     * @param days      天数
     * @param formatter 时间格式
     * @return 时间字符串
     */
    public static String getAfterTime(int days, String formatter) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return getFormatterTime(calendar.getTime(), formatter);
    }

    /**
     * 得到某天时间之后的多少天
     *
     * @param time      某天时间
     * @param days      天数
     * @param formatter 时间格式
     * @return 时间字符串
     */
    public static String getAfterTime(String time, int days, String formatter) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatter, Locale.CHINA);
            Date date = df.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, days);
            return getFormatterTime(calendar.getTime(), formatter);
        } catch (Exception e) {
            KLog.e(e);
        }
        return "";
    }

    /**
     * 得到上周一的时间 yyyy-MM-dd HH:mm:ss
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getFirstOfLastWeek() {
        SimpleDateFormat df = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        //设置每周第一天为星期一 (默认是星期日)
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        return df.format(cal.getTime());
    }


    /**
     * 判断是否闰年
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(String time) {
        return isLeapYear(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 判断是否闰年
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(String time, String pattern) {
        return isLeapYear(string2Date(time, pattern));
    }

    /**
     * 判断是否闰年
     *
     * @param date Date类型时间
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断是否闰年
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(long millis) {
        return isLeapYear(millis2Date(millis));
    }

    /**
     * 判断是否闰年
     *
     * @param year 年份
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获取星期
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 星期
     */
    public static String getWeek(String time) {
        return getWeek(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取星期
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 星期
     */
    public static String getWeek(String time, String pattern) {
        return getWeek(string2Date(time, pattern));
    }

    /**
     * 获取星期
     *
     * @param date Date类型时间
     * @return 星期
     */
    public static String getWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }

    /**
     * 获取星期
     *
     * @param millis 毫秒时间戳
     * @return 星期
     */
    public static String getWeek(long millis) {
        return getWeek(new Date(millis));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...5
     */
    public static int getWeekIndex(String time) {
        return getWeekIndex(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 1...7
     */
    public static int getWeekIndex(String time, String pattern) {
        return getWeekIndex(string2Date(time, pattern));
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     *
     * @param date Date类型时间
     * @return 1...7
     */
    public static int getWeekIndex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期
     * <p>注意：周日的Index才是1，周六为7</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...7
     */
    public static int getWeekIndex(long millis) {
        return getWeekIndex(millis2Date(millis));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...5
     */
    public static int getWeekOfMonth(String time) {
        return getWeekOfMonth(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 1...5
     */
    public static int getWeekOfMonth(String time, String pattern) {
        return getWeekOfMonth(string2Date(time, pattern));
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param date Date类型时间
     * @return 1...5
     */
    public static int getWeekOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...5
     */
    public static int getWeekOfMonth(long millis) {
        return getWeekOfMonth(millis2Date(millis));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 1...54
     */
    public static int getWeekOfYear(String time) {
        return getWeekOfYear(string2Date(time, DEFAULT_PATTERN));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 1...54
     */
    public static int getWeekOfYear(String time, String pattern) {
        return getWeekOfYear(string2Date(time, pattern));
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param date Date类型时间
     * @return 1...54
     */
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param millis 毫秒时间戳
     * @return 1...54
     */
    public static int getWeekOfYear(long millis) {
        return getWeekOfYear(millis2Date(millis));
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param millis  毫秒时间戳
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String millis2String(long millis, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time) {
        return string2Millis(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public static Date string2Date(String time) {
        return string2Date(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return Date类型
     */
    public static Date string2Date(String time, String pattern) {
        return new Date(string2Millis(time, pattern));
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param date Date类型时间
     * @return 时间字符串
     */
    public static String date2String(Date date) {
        return date2String(date, DEFAULT_PATTERN);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param date    Date类型时间
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String date2String(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param date Date类型时间
     * @return 毫秒时间戳
     */
    public static long date2Millis(Date date) {
        return date.getTime();
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param millis 毫秒时间戳
     * @return Date类型时间
     */
    public static Date millis2Date(long millis) {
        return new Date(millis);
    }
}
