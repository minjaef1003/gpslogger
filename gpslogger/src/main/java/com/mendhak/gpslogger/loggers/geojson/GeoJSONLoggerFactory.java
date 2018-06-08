package com.mendhak.gpslogger.loggers.geojson;

import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.FileLoggerFactory;
import com.mendhak.gpslogger.loggers.FileLoggerFactoryPattern.FileLoggerFactoryFileBool;

import java.io.File;

public class GeoJSONLoggerFactory extends FileLoggerFactoryFileBool {

    @Override
    public FileLogger getFileLogger(File file, boolean setAddNewTrackSegment) {
        return new GeoJSONLogger(file, setAddNewTrackSegment);
    }
}
