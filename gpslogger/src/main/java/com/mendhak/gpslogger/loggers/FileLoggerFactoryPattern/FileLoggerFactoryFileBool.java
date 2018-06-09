package com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern;

import com.mendhak.gpslogger.loggers.FileLogger;

import java.io.File;

public abstract class FileLoggerFactoryFileBool {
    public abstract FileLogger getFileLogger(File file, boolean setAddNewTrackSegment);
}
