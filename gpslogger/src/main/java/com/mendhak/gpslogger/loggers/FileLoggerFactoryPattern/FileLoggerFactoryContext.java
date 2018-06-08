package com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern;

import android.content.Context;

import com.mendhak.gpslogger.loggers.FileLogger;

public abstract class FileLoggerFactoryContext {
    public abstract FileLogger getFileLogger(Context context);
}
