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

public abstract class GpxFileLogger implements FileLogger {
	protected final static Object lock = new Object();

    private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10), new RejectionHandler());
    private File gpxFile = null;
    private final boolean addNewTrackSegment;
    protected final String name = "GPX";
	private Statistics statistics;
	
	public GpxFileLogger(Statistics setStatistics, File setGpxFile, boolean setAddNewTrackSegment) {
		this.statistics = setStatistics;
        this.gpxFile = setGpxFile;
        this.addNewTrackSegment = setAddNewTrackSegment;
    }
	
	abstract public Runnable getWriteHandler();
	abstract public String getBeginningXml();
	abstract public void appendCourseAndSpeed();
}

