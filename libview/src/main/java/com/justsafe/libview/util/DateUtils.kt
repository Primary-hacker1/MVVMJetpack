package com.justsafe.libview.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 * Created by kang on 2017/10/26.
 */
object DateUtils {
    private const val TAG = "DateUtils"
    const val DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss"
    const val DEFAULT_FORMAT_DATE_DAY = "yyyy-MM-dd"

    @SuppressLint("SimpleDateFormat")
    val TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

    private var dataWeek: Date? = null

    /**
     * 取得相对时间,如:14小时前
     *
     * @param created 相对时间起点
     * @return 相对时间
     */
    fun getTimeSpanString(created: String): CharSequence {
        return try {
            DateUtils.getRelativeTimeSpanString(
                TIME_FORMAT.parse(created).time,
                Date().time, DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )
        } catch (e: ParseException) {
            created
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?, formatStr: String?): String {
        return SimpleDateFormat(formatStr ?: DEFAULT_FORMAT_DATE)
            .format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateDay(date: Date?, formatStr: String?): String {
        return SimpleDateFormat(formatStr ?: DEFAULT_FORMAT_DATE_DAY)
            .format(date)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun formatDate(dateStr: String?, formatStr: String?): Date {
        return SimpleDateFormat(formatStr ?: DEFAULT_FORMAT_DATE)
            .parse(dateStr)
    }

    /**
     * 未来时间
     *
     * @return
     */
    fun isFutureTime(date: Date): Boolean {
        val nowDate = Date()
        return date.after(nowDate)
    }

    /**
     * 取得现在时间的年月日字符串
     *
     * @return 时间的年月日字符串
     */
    val nowTimeString: String
        get() {
            val nowDate = Date()
            return TIME_FORMAT.format(nowDate)
        }

    /**
     * 取得现在时间的年月日字符串
     *
     * @return 时间的年月日字符串
     */
    val nowTimeDataString: String
        get() {
            val nowDate = Date()
            return DATE_FORMAT.format(nowDate)
        }

    @SuppressLint("SimpleDateFormat")
    fun getDateString(time: Date?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeString(time: Date?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(time)
    }

    fun getTimeString(time: Date?): String {
        return getTimeString(time, "yyyy-MM-dd hh:mm:ss")
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun getTimeLong(str: String?): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = TIME_FORMAT.parse(str)
        return date.time
    }

    fun getZeroTimeDate(fecha: Date?): Date {
        val calendar = Calendar.getInstance()
        calendar.time = fecha
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    val tomorrowDate: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            return calendar.time
        }

    /**
     * 解析时间字符串
     *
     * @param source 时间字符串
     * @return Date
     * @throws ParseException 解析异常
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parse(source: String?): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return formatter.parse(source)
    }

    fun addDays(date: Date?, days: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, days) //minus number would decrement the days
        return cal.time
    }

    var dateTime: Long = 0
    @SuppressLint("SimpleDateFormat")
    fun timeLong(sDate: String?): Long {
        val df = SimpleDateFormat("HH:mm:ss")
        try {
            val d = df.parse(sDate)
            dateTime = d.time
        } catch (pe: ParseException) {
            println(pe.message)
        }
        return dateTime
    }

    /**
     * 将毫秒转换为字符串
     *
     * @param timeMs
     * @return
     */
    fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds =
            totalSeconds % 60 + if (totalSeconds == 14 && totalSeconds % 1000 > 0) 1 else 0
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    /**
     * 根据日期获得星期
     *
     * @param dateString -
     * @return -
     */
    @SuppressLint("SimpleDateFormat")
    fun getWeekOfDate(dateString: String?): String {
        val df = SimpleDateFormat("yyyy年MM月dd日")
        try {
            dataWeek = df.parse(dateString)
        } catch (e: Exception) {
        }
        val weekDaysName = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val weekDaysCode = arrayOf("0", "1", "2", "3", "4", "5", "6")
        val calendar = Calendar.getInstance()
        calendar.time = dataWeek
        val intWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        return weekDaysName[intWeek]
    }
}