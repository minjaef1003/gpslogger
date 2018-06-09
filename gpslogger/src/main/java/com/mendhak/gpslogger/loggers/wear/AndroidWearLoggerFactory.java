package com.mendhak.gpslogger.loggers.wear;

import android.content.Context;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryContext;

public class AndroidWearLoggerFactory extends FileLoggerFactoryContext {

    @Override
    public FileLogger getFileLogger(Context context) {
        return new AndroidWearLogger(context);
    }
}
