package com.common.log;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * create by 2022/6/30
 * log本地输出日志
 *
 * @author zt
 */
public class ThinkerLogger {
    private static ThinkerLogger instance;

    public static ThinkerLogger getInstance() {
        if (instance == null)
            instance = new ThinkerLogger();
        return instance;
    }

    public void init(String logDir) {
        try {
            File srcFolder = new File(logDir);
            if (!srcFolder.exists()) {
                srcFolder.mkdirs();
            }
            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(srcFolder + File.separator + "log");
            logConfigurator.setRootLevel(Level.DEBUG);     //日志开
//           logConfigurator.setRootLevel(Level.OFF);     //日志关
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");

            //按照文件的大小分割log（互斥）
//        logConfigurator.setUseFileAppender(true);
//        logConfigurator.setMaxFileSize(1024 * 1024 * 10);
            //按照文件的日期分割log
            logConfigurator.setUseFileAppender(false);

            //本地缓存文件个数
            logConfigurator.setMaxBackupSize(3);
            //设置所有消息是否被立刻输出 默认为true,false 不输出
            logConfigurator.setImmediateFlush(true);
            logConfigurator.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger(String className) {
        return Logger.getLogger(className);
    }
}
