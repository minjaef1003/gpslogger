package com.mendhak.gpslogger.loggers.gpx;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryFileBool;

import java.io.File;

public class Gpx11FileLoggerFactory extends FileLoggerFactoryFileBool {

    @Override
    public FileLogger getFileLogger(File file, boolean setAddNewTrackSegment) {
        return new Gpx11FileLogger(file, setAddNewTrackSegment);
    }
}
