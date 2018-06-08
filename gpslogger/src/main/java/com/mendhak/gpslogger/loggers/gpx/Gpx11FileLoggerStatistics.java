package com.mendhak.gpslogger.loggers.gpx;

import android.location.Location;
import com.mendhak.gpslogger.BuildConfig;
import com.mendhak.gpslogger.common.BundleConstants;
import com.mendhak.gpslogger.common.Maths;
import com.mendhak.gpslogger.common.RejectionHandler;
import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.mendhak.gpslogger.loggers.FileLogger;
import com.mendhak.gpslogger.loggers.Files;
import org.slf4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class Gpx11FileLoggerStatistics implements Statistics {

	public Gpx11FileLoggerStatistics(File setGpxFile, boolean setAddNewTrackSegment) {
        super();
    }

    public Runnable getWriteHandler(String dateTimeString, File gpxFile, Location loc, boolean addNewTrackSegment)
    {
        return new Gpx11WriteHandler(dateTimeString, gpxFile, loc, addNewTrackSegment);
    }

}

class Gpx11WriteHandler implements Runnable {

	private static final Logger LOG = Logs.of(Gpx10WriteHandler.class);
    String dateTimeString;
    Location loc;
    private File gpxFile = null;
    private boolean addNewTrackSegment;

    public Gpx11WriteHandler(String dateTimeString, File gpxFile, Location loc, boolean addNewTrackSegment) {
        this.dateTimeString = dateTimeString;
        this.addNewTrackSegment = addNewTrackSegment;
        this.gpxFile = gpxFile;
        this.loc = loc;
    }

    String getBeginningXml(String dateTimeString){
        // Use GPX 1.1 namespaces and put <time> inside a <metadata> element
        StringBuilder initialXml = new StringBuilder();
        initialXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        initialXml.append("<gpx version=\"1.1\" creator=\"GPSLogger " + BuildConfig.VERSION_CODE + " - http://gpslogger.mendhak.com/\" ");
        initialXml.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        initialXml.append("xmlns=\"http://www.topografix.com/GPX/1/1\" ");
        initialXml.append("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 ");
        initialXml.append("http://www.topografix.com/GPX/1/1/gpx.xsd\">");
        initialXml.append("<metadata><time>").append(dateTimeString).append("</time></metadata>");
        return initialXml.toString();
    }

    public void appendCourseAndSpeed(StringBuilder track, Location loc)
    {
        // no-op
        // course and speed are not part of the GPX 1.1 specification
        // We could put them in an extensions such if we really wanted.
    }
}
