package com.easy.android.example;

import android.app.Application;

import com.easy.android.example.config.Config;
import com.easy.android.log.XLog;
import com.easy.android.log.box.LogBox;
import com.easy.android.log.callback.LogFileCallback;

import java.io.File;

/**
 * Created by kevin on 2017/1/10.
 */

public class App extends Application {

    public static final String TAG = "App;";

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.logOn(true, true);
        LogBox.init(
                getApplicationContext().getDir(Config.FileCacheConfig.LOG_CACHE_DIR, MODE_PRIVATE),
                new LogFileCallback() {
                    @Override
                    public boolean onFile(File logFile) {
                        //异步回调  执行文件处理
                        XLog.d(TAG, "--onFile--  " + logFile.getAbsolutePath());
                        return true;
                    }
                });
        LogBox.setCacheSizePerFile(10);
    }
}
