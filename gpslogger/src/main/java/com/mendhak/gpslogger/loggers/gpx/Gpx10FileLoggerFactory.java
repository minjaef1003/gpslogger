package com.mendhak.gpslogger.loggers.gpx;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryFileBool;

import java.io.File;

public class Gpx10FileLoggerFactory extends FileLoggerFactoryFileBool {
    @Override
    public FileLogger getFileLogger(File file, boolean setAddNewTrackSegment) {
        return new Gpx10FileLogger(file, setAddNewTrackSegment);
    }
}
