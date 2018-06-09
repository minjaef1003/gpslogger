package com.mendhak.gpslogger.loggers.kml;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryFileBool;

import java.io.File;

public class Kml22FileLoggerFactory extends FileLoggerFactoryFileBool {

    @Override
    public FileLogger getFileLogger(File file, boolean setAddNewTrackSegment) {
        return new Kml22FileLogger(file, setAddNewTrackSegment);
    }
}