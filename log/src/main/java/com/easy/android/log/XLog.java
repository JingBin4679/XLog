package com.easy.android.log;


import com.easy.android.log.box.LogBox;

/**
 * Created by kevin on 2016/12/28.
 */
public class XLog {
    public static boolean _debug = true;
    public static boolean _log = true;

    public static final String TAG = "ILog";

    public static void logOn(boolean debug) {
        logOn(debug, false);
    }

    public static void logOn(boolean debug, boolean collectLog) {
        _debug = debug;
        _log = collectLog;
    }

    public static void i(String tag, String info) {
        if (_debug) {
            android.util.Log.i(tag, info);
        }
        if (_log) {
            LogBox.log(Level.LEVEL_INFO, tag, info);
        }
    }

    public static void i(String info) {
        i(TAG, info);
    }

    public static void d(String tag, String info) {
        if (_debug) {
            android.util.Log.d(tag, info);
        }
        if (_log) {
            LogBox.log(Level.LEVEL_DEBUG, tag, info);
        }
    }

    public static void d(String info) {
        d(TAG, info);
    }

    public static void w(String tag, String info) {
        if (_debug) {
            android.util.Log.w(tag, info);
        }
        if (_log) {
            LogBox.log(Level.LEVEL_WARN, tag, info);
        }
    }

    public static void w(String info) {
        w(TAG, info);
    }

    public static void e(String tag, String info) {
        if (_debug) {
            android.util.Log.e(tag, info);
        }
        if (_log) {
            LogBox.log(Level.LEVEL_ERROR, tag, info);
        }
    }

    public static void e(String info) {
        e(TAG, info);
    }
}
