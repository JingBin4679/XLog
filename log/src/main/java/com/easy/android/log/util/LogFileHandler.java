package com.easy.android.log.util;


import com.easy.android.log.callback.LogFileCallback;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kevin on 2017/1/3.
 */

public class LogFileHandler {
    public static final String TAG = "LogFileHandler";

    private static Executor uploadService = Executors.newSingleThreadExecutor();
    private static Executor addTaskService = Executors.newCachedThreadPool();
    private static boolean _running = false;
    private static boolean _exitFlag = false;
    private static LinkedList<File> fileList = new LinkedList<>();
    private static LogFileCallback _callback = new LogFileCallback() {
    };

    public static void setLogHandler(LogFileCallback callback) {
        if (callback == null) {
            throw new NullPointerException("callback cannot be null");
        }
        _callback = callback;
    }

    public static synchronized void start() {
        if (_running) {
            return;
        }
        _running = true;
        uploadService.execute(new Runnable() {
            @Override
            public void run() {
                while (!_exitFlag) {
                    File file = null;
                    try {
                        file = getTempFile();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    executeUploadFile(file);
                }
            }
        });
    }

    private static void executeUploadFile(File file) {
        if (checkValid(file)) return;
        try {
            if (!_callback.onFile(file)) {
                return;
            }
            file.delete();
            synchronized (fileList) {
                fileList.remove(file);
            }
        } catch (Exception e) {
        }
    }

    private static boolean checkValid(File file) {
        if (file == null) {
            return true;
        }
        if (!file.exists()) {
            synchronized (fileList) {
                fileList.remove(file);
            }
            return true;
        }
        return false;
    }

    private static File getTempFile() throws InterruptedException {
        File file = null;
        synchronized (fileList) {
            try {
                file = fileList.get(0);
            } catch (NoSuchElementException e) {
            } catch (IndexOutOfBoundsException e) {
            }
            if (file == null) {
                fileList.wait();
                try {
                    file = fileList.get(0);
                } catch (NoSuchElementException e) {
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        return file;
    }

    public static void addLogFile(final File file) {
        addTaskService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (fileList) {
                    fileList.add(file);
                    fileList.notify();
                }
            }
        });
    }

    public static void addLogFiles(final List<File> files) {
        addTaskService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (fileList) {
                    fileList.addAll(files);
                    fileList.notify();
                }
            }
        });
    }
}
