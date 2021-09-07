package com.justsafe.libview.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 时间工具类
 * Created by kang on 2017/10/26.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";

    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_FORMAT_DATE_DAY = "yyyy-MM-dd";
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 取得相对时间,如:14小时前
     *
     * @param created 相对时间起点
     * @return 相对时间
     */
    public static CharSequence getTimeSpanString(String created) {
        try {
            return android.text.format.DateUtils.getRelativeTimeSpanString(TIME_FORMAT.parse(created).getTime(),
                    new Date().getTime(), android.text.format.DateUtils.MINUTE_IN_MILLIS,
                    android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE);
        } catch (ParseException e) {
            return created;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date, String formatStr) {
        return (new SimpleDateFormat((formatStr == null ? DEFAULT_FORMAT_DATE
                : formatStr))).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateDay(Date date, String formatStr) {
        return (new SimpleDateFormat((formatStr == null ? DEFAULT_FORMAT_DATE_DAY
                : formatStr))).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date formatDate(String dateStr, String formatStr)
            throws ParseException {
        return (new SimpleDateFormat((formatStr == null ? DEFAULT_FORMAT_DATE
                : formatStr))).parse(dateStr);
    }

    /**
     * 未来时间
     *
     * @return
     */
    public static boolean isFutureTime(Date date) {
        Date nowDate = new Date();
        if (date.after(nowDate)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取得现在时间的年月日字符串
     *
     * @return 时间的年月日字符串
     */
    public static String getNowTimeString() {
        Date nowDate = new Date();
        return TIME_FORMAT.format(nowDate);
    }

    /**
     * 取得现在时间的年月日字符串
     *
     * @return 时间的年月日字符串
     */
    public static String getNowTimeDataString() {
        Date nowDate = new Date();
        return DATE_FORMAT.format(nowDate);
    }

    public static String getDateString(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(time);
        return date;
    }

    public static String getTimeString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }


    public static String getTimeString(Date time) {
        return getTimeString(time, "yyyy-MM-dd hh:mm:ss");
    }

    public static long getTimeLong(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = TIME_FORMAT.parse(str);
        long time = date.getTime();
        return time;
    }

    public static Date getZeroTimeDate(Date fecha) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 解析时间字符串
     *
     * @param source 时间字符串
     * @return Date
     * @throws ParseException 解析异常
     */
    public static Date parse(String source) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.parse(source);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    static long dateTime;

    public static long timeLong(String sDate) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d = df.parse(sDate);
            dateTime = d.getTime();
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
        return dateTime;
    }

    /**
     * 将毫秒转换为字符串
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60 + ((totalSeconds == 14 && totalSeconds % 1000 > 0) ? 1 : 0);
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    /**
     * 根据日期获得星期
     *
     * @param dateString
     * @return
     */

    private static Date dataWeek;

    public static String getWeekOfDate(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            dataWeek = df.parse(dateString);
        } catch (Exception e) {

        }

        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataWeek);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
