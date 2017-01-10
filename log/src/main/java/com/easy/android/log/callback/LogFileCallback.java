package com.easy.android.log.callback;

import java.io.File;

/**
 * Created by kevin on 2017/1/10.
 */

public abstract class LogFileCallback {

    public boolean onFile(File logFile) {
        return true;
    }
}
