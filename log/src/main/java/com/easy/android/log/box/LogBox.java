package com.easy.android.log.box;

import com.easy.android.log.callback.LogFileCallback;
import com.easy.android.log.util.LogFileHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kevin on 2016/12/28.
 */

public class LogBox {
    private static final String FORMAT = "\t%s\t%s\t%s";
    public static final SimpleDateFormat TIME_LABEL_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public static final SimpleDateFormat FILE_NAME_TAG = new SimpleDateFormat("_yyyyMMdd_HHmmss_SSS");

    private static final long KB = 1024l;
    private static final StringBuffer stringBuffer = new StringBuffer();
    private static long MAX_SIZE = 10 * KB; // KB

    public static final void setCacheSizePerFile(long maxSizeKB) {
        MAX_SIZE = maxSizeKB * KB;
    }

    private static final Executor saveFileService = Executors.newCachedThreadPool();
    public static String LOG_CACHE_DIR;

    static {
        TIME_LABEL_FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        LogFileHandler.start();
    }

    private static Runnable appendLogRunnable;

    public static void init(File cacheFileDir, LogFileCallback callback) {
        LOG_CACHE_DIR = cacheFileDir.getAbsolutePath() + File.separator;
        LogFileHandler.setLogHandler(callback);

        File file = new File(LOG_CACHE_DIR);

        if (file != null) {
            File[] files = file.listFiles();
            if (files != null || files.length > 0) {
                List<File> fileList = Arrays.asList(files);
                LogFileHandler.addLogFiles(fileList);
            }
        }
    }

    public static void log(final String level, final String tag, final String log) {
        appendLogRunnable = new Runnable() {
            @Override
            public void run() {
                String format = String.format(FORMAT, level, tag, log);
                synchronized (stringBuffer) {
                    stringBuffer.append(getTimeLabel());
                    stringBuffer.append(format);
                    stringBuffer.append("\n");
                    checkBufferSize();
                }
            }
        };
        saveFileService.execute(appendLogRunnable);
    }

    private static synchronized void checkBufferSize() {
        if (stringBuffer.length() < MAX_SIZE) {
            return;
        }
        String sss = stringBuffer.toString();
        stringBuffer.setLength(0);
        saveBufferedLog(sss);
    }

    private static void saveLogFile(final String content) {
        saveFileService.execute(new Runnable() {
            @Override
            public void run() {
                String fileName = "Log" + FILE_NAME_TAG.format(new Date(System.currentTimeMillis()));
                File file = new File(LOG_CACHE_DIR + fileName + ".log");
                FileWriter fw = null;
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    fw = new FileWriter(file);
                    fw.write(content);
                    fw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } finally {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LogFileHandler.addLogFile(file);
            }
        });
    }

    public static void saveBufferedLog(final String content) {
        saveFileService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (stringBuffer) {
                    saveLogFile(content);
                }
            }
        });
    }

    private static String getTimeLabel() {
        String format = TIME_LABEL_FORMATTER.format(new Date());
        return format;
    }
}
