package com.common.log;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;


/**
 * create by 2022/6/30
 * log本地输出日志
 *
 * @author zt
 */
public class MyDailyRollingFileAppender extends FileAppender {
    static final int TOP_OF_TROUBLE = -1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR = 1;
    static final int HALF_DAY = 2;
    static final int TOP_OF_DAY = 3;
    static final int TOP_OF_WEEK = 4;
    static final int TOP_OF_MONTH = 5;

    /**
     * 默认设置："'.'yyyy-MM-dd"
     * 设置说明：按天循环打印日志
     */
    private String datePattern = "'.'yyyy-MM-dd";
    private int maxBackupIndex = 1;
    private String scheduledFilename;
    /**
     * 下一次Rolling日志文件的时间
     */
    private long nextCheck = System.currentTimeMillis() - 1;
    Date now = new Date();
    SimpleDateFormat sdf;
    RollingCalendar rc = new RollingCalendar();
    int checkPeriod = TOP_OF_TROUBLE;
    /**
     * 获取当前环境所处的时区
     * 仅供computeCheckPeriod方法使用
     */
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    public MyDailyRollingFileAppender() {
    }

    public MyDailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String pattern) {
        this.datePattern = pattern;
    }

    public int getMaxBackupIndex() {
        return maxBackupIndex;
    }

    public void setMaxBackupIndex(int maxBackupIndex) {
        this.maxBackupIndex = maxBackupIndex;
    }

    /**
     * activateOptions译名为激活操作
     * 意思是按照配置的参数进行初始化
     * scheduledFilename为log的最后一次修改时间
     */
    @Override
    public void activateOptions() {
        super.activateOptions();

        if (datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod();
            printPeriodicity(type);
            rc.setType(type);
            File file = new File(fileName);
            scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));
        } else {
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }
    }

    //  This method computes the roll over period by looping over the
//  periods, starting with the shortest, and stopping when the r0 is
//  different from from r1, where r0 is the epoch formatted according
//  the datePattern (supplied by the user) and r1 is the
//  epoch+nextMillis(i) formatted according to datePattern. All date
//  formatting is done in GMT and not local format because the test
//  logic is based on comparisons relative to 1970-01-01 00:00:00
//  GMT (the epoch).
    int computeCheckPeriod() {
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
        //设置初始时间为格林尼治时间：1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);
        if (datePattern != null) {
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                //将所示的时间格式化为当前时区
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(gmtTimeZone);

                String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                String r1 = simpleDateFormat.format(next);
                //System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
     * 按照周期将当前日志文件转存为日期文件
     *
     * @throws IOException
     */
    void rollOver() throws IOException {

        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        String datedFilename = fileName + sdf.format(now);

        //如果最后一次的修改时间为当前时间 ，则不做任何任何操作
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        // 关闭当前文件，重命名为日期文件
        this.closeFile();

        File target = new File(scheduledFilename);
        if (target.exists()) {
            target.delete();
        }

        File file = new File(fileName);
        boolean result = file.renameTo(target);
        if (result) {
            LogLog.debug(fileName + " -> " + scheduledFilename);
        } else {
            LogLog.error("Failed to rename [" + fileName + "] to [" + scheduledFilename + "].");
        }

        //获取日志文件列表，控制数量，实现清理策略
        if (file.getParentFile().exists()) {
            File[] files = file.getParentFile().listFiles(new LogFileFilter(file.getName()));
            Long[] dateArray = new Long[files.length];
            for (int i = 0; i < files.length; i++) {
                File fileItem = files[i];
                String fileDateStr = fileItem.getName().replace(file.getName(), "");
                Date filedate = null;
                try {
                    filedate = sdf.parse(fileDateStr);
                    long fileDateLong = filedate.getTime();
                    dateArray[i] = fileDateLong;
                } catch (ParseException e) {
                    LogLog.error("Parse File Date Throw Exception : " + e.getMessage());
                }
            }
            Arrays.sort(dateArray);
            if (dateArray.length > maxBackupIndex) {//需要清理多余日志文件了
                for (int i = 0; i < dateArray.length - maxBackupIndex; i++) {
                    String dateFileName = file.getPath() + sdf.format(dateArray[i]);
                    File dateFile = new File(dateFileName);
                    if (dateFile.exists()) {
                        dateFile.delete();
                    }
                }
            }
        }

        try {
            // This will also close the file. This is OK since multiple close operations are safe.
            this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
        } catch (IOException e) {
            errorHandler.error("setFile(" + fileName + ", true) call failed.");
        }
        scheduledFilename = datedFilename;
    }

    /**
     * 写入日志之前判断是否需要新起一个日志来记录
     */
    @Override
    protected void subAppend(LoggingEvent event) {
        long n = System.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            try {
                rollOver();
            } catch (IOException ioe) {
                if (ioe instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        super.subAppend(event);
    }

    /**
     * 文件过滤器
     */
    class LogFileFilter implements FileFilter {
        private String logName;

        public LogFileFilter(String logName) {
            this.logName = logName;
        }

        @Override
        public boolean accept(File file) {
            if (logName == null || file.isDirectory()) {
                return false;
            } else {
                LogLog.debug(file.getName());
                return file.getName().startsWith(logName);
            }
        }
    }

    /**
     * 根据type打印做出日志打印
     *
     * @param type
     */
    void printPeriodicity(int type) {
        switch (type) {
            case TOP_OF_MINUTE:
                LogLog.debug("Appender [" + name + "] to be rolled every minute.");
                break;
            case TOP_OF_HOUR:
                LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
                break;
            case HALF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");
                break;
            case TOP_OF_DAY:
                LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
                break;
            case TOP_OF_WEEK:
                LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
                break;
            case TOP_OF_MONTH:
                LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
                break;
            default:
                LogLog.warn("Unknown periodicity for appender [" + name + "].");
        }
    }

    /**
     * 内部类
     *
     * @author 日志Rolling日历, 获取下一次日志Rolling时间
     */
    class RollingCalendar extends GregorianCalendar {
        private static final long serialVersionUID = -3560331770601814177L;

        int type = MyDailyRollingFileAppender.TOP_OF_TROUBLE;

        /**
         * RollingCalendar默认构造器
         */
        RollingCalendar() {
            super();
        }

        /**
         * RollingCalendar构造器
         * 根据地点时区 ，获取对应的日历Calendar
         *
         * @param tz
         * @param locale
         */
        RollingCalendar(TimeZone tz, Locale locale) {
            super(tz, locale);
        }

        void setType(int type) {
            this.type = type;
        }

        public long getNextCheckMillis(Date now) {
            return getNextCheckDate(now).getTime();
        }


        /**
         * 根据所传入的时间以及时间类型获取下一个时间
         *
         * @param now
         * @return
         */
        public Date getNextCheckDate(Date now) {
            this.setTime(now);

            switch (type) {
                case MyDailyRollingFileAppender.TOP_OF_MINUTE:
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    this.add(Calendar.MINUTE, 1);
                    break;
                case MyDailyRollingFileAppender.TOP_OF_HOUR:
                    this.set(Calendar.MINUTE, 0);
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    this.add(Calendar.HOUR_OF_DAY, 1);
                    break;
                case MyDailyRollingFileAppender.HALF_DAY:
                    this.set(Calendar.MINUTE, 0);
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    int hour = get(Calendar.HOUR_OF_DAY);
                    if (hour < 12) {
                        this.set(Calendar.HOUR_OF_DAY, 12);
                    } else {
                        this.set(Calendar.HOUR_OF_DAY, 0);
                        this.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    break;
                case MyDailyRollingFileAppender.TOP_OF_DAY:
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.set(Calendar.MINUTE, 0);
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    this.add(Calendar.DATE, 1);
                    break;
                case MyDailyRollingFileAppender.TOP_OF_WEEK:
                    this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.set(Calendar.MINUTE, 0);
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    this.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case MyDailyRollingFileAppender.TOP_OF_MONTH:
                    this.set(Calendar.DATE, 1);
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.set(Calendar.MINUTE, 0);
                    this.set(Calendar.SECOND, 0);
                    this.set(Calendar.MILLISECOND, 0);
                    this.add(Calendar.MONTH, 1);
                    break;
                default:
                    throw new IllegalStateException("Unknown periodicity type.");
            }
            return getTime();
        }
    }
}
