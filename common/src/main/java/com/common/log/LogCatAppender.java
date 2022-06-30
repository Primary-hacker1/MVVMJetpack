package com.common.log;

import android.util.Log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * create by 2022/6/30
 * log本地输出日志
 *
 * @author zt
 */
public class LogCatAppender extends AppenderSkeleton {

    protected Layout tagLayout;

    public LogCatAppender(final Layout messageLayout, final Layout tagLayout) {
        this.tagLayout = tagLayout;
        setLayout(messageLayout);
    }

    public LogCatAppender(final Layout messageLayout) {
        this(messageLayout, new PatternLayout("%c"));
    }

    public LogCatAppender() {
        this(new PatternLayout("%m%n"));
    }

    @Override
    protected void append(final LoggingEvent le) {
        switch (le.getLevel().toInt()) {
            case Level.TRACE_INT:
                if (le.getThrowableInformation() != null) {
                    Log.v(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.v(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.DEBUG_INT:
                if (le.getThrowableInformation() != null) {
                    Log.d(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.d(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.INFO_INT:
                if (le.getThrowableInformation() != null) {
                    Log.i(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.i(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.WARN_INT:
                if (le.getThrowableInformation() != null) {
                    Log.w(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.w(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.ERROR_INT:
                if (le.getThrowableInformation() != null) {
                    Log.e(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.e(getTagLayout().format(le), getLayout().format(le));
                }
                break;
            case Level.FATAL_INT:
                if (le.getThrowableInformation() != null) {
                    Log.wtf(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                }
                else {
                    Log.wtf(getTagLayout().format(le), getLayout().format(le));
                }
                break;
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    public Layout getTagLayout() {
        return tagLayout;
    }

    public void setTagLayout(final Layout tagLayout) {
        this.tagLayout = tagLayout;
    }
}
