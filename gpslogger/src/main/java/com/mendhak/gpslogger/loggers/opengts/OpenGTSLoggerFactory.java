package com.mendhak.gpslogger.loggers.opengts;

import android.content.Context;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryContext;

public class OpenGTSLoggerFactory extends FileLoggerFactoryContext{

    @Override
    public FileLogger getFileLogger(Context context) {
        return new OpenGTSLogger(context);
    }
}
