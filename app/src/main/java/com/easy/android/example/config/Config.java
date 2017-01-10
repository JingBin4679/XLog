package com.easy.android.example.config;

import java.util.TimeZone;

/**
 * Created by kevin on 2016/12/27.
 */

public class Config {

    public static TimeZone getCurrentTimeZone() {
        return TimeZone.getTimeZone("GMT+08:00");
    }


    public static class FileCacheConfig {
        public static final String LOG_CACHE_DIR = "log_cache";
    }

}
