package com.zjwh.sw.customize.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * create by zy on 2017/11/2
 * </p>
 */

@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static final String HHMM = "HH:mm";
    public static final String YYYYMMDD = "yyyy.MM.dd";
    public static final String YYYYMMDDHHMM = "yyyy.MM.dd HH:mm";
    public static final String YYYYMMDDHHMMSS = "yyyy.MM.dd HH:mm:ss";
    public static final String MMDD = "MM.dd";
    public static final String MMSSMM = "mm:ss.SS";

    /***
     * 格式化时间，返回格式化时间
     * @param time 时间数据
     * @param stringMat 转化后的时间格式
     * @return 将时间转化为格式字符串
     */
    public static String formatDate(long time, String stringMat) {
        if (time <= 0)
            return "";
        SimpleDateFormat format = new SimpleDateFormat(stringMat);
        return format.format(time);
    }

    /***
     * 格式化时间
     * @param date 时间数据
     * @param stringMat 转化后的时间格式
     * @return 将时间转化为格式字符串
     */
    public static String formatDate(Date date, String stringMat) {
        return formatDate(date.getTime(), stringMat);
    }

    /***
     * 将小于10的数前面加上0
     * @param number 格式化数字
     * @return 格式化后的字符串
     */
    public static String add0(int number) {
        String result = String.valueOf(number);
        if (number < 10) {
            result = "0" + result;
        }
        return result;
    }

    public static long getCurrentDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(getCurYYMMDD()).getTime();
    }

    public static String getCurYYMMDD() {
        return getYYMMDD(getCurrentYear(), getCurrentMonth(), getCurrentDay());
    }

    public static String getYYMMDD(int year, int month, int day) {
        return year + "-" + add0(month) + "-" + add0(day);
    }

    public static String getHHSS(int startHour, int startMinute, int endHour, int endMinute) {
        return add0(startHour) + ":" + add0(startMinute) + "-" + add0(endHour) + ":" + add0(endMinute);
    }

    /***
     * 获取手机当前年份
     */
    public static int getCurrentYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date(System.currentTimeMillis());
        return Integer.valueOf(format.format(date));
    }

    /***
     * 获取手机当前月份
     */
    public static int getCurrentMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date(System.currentTimeMillis());
        return Integer.valueOf(format.format(date));
    }

    /***
     * 获取手机当前日
     */
    public static int getCurrentDay() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date date = new Date(System.currentTimeMillis());
        return Integer.valueOf(format.format(date));
    }

    /***
     * 获取手机当前小时
     */
    public static int getCurrentHour() {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        Date date = new Date(System.currentTimeMillis());
        return Integer.valueOf(format.format(date));
    }

    /***
     * 获取手机当前分钟
     */
    public static int getCurrentMinute() {
        SimpleDateFormat format = new SimpleDateFormat("mm");
        Date date = new Date(System.currentTimeMillis());
        return Integer.valueOf(format.format(date));
    }

    /**
     * 星期几
     *
     * @param time long 系统时间的long类型
     * @return 星期一到星期日
     */
    public static String getWeekOfDate(long time) {

        Date date = new Date(time);
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获取格式化时间
     * return 01:10:01
     */
    public static String getTime(long timeSeconds) {
        if (timeSeconds <= 0)
            return "00:00:00";
        StringBuilder sb = new StringBuilder();
        long hh = timeSeconds / 3600;
        long mm = timeSeconds % 3600 / 60;
        long ss = timeSeconds % 3600 % 60;
        if (hh < 10) {
            sb.append("0").append(hh).append(":");
        } else {
            sb.append(hh).append(":");
        }

        if (mm < 10) {
            sb.append("0").append(mm).append(":");
        } else {
            sb.append(mm).append(":");
        }
        if (ss < 10) {
            sb.append("0").append(ss);
        } else {
            sb.append(ss);
        }
        return sb.toString();
    }

}
