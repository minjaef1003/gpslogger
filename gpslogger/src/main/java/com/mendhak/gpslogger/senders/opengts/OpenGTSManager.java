/*
 * Copyright (C) 2016 mendhak
 *
 * This file is part of GPSLogger for Android.
 *
 * GPSLogger for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * GPSLogger for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mendhak.gpslogger.senders.opengts;

import com.mendhak.gpslogger.common.*;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.mendhak.gpslogger.loggers.customurl.CustomUrlJob;
import com.mendhak.gpslogger.loggers.customurl.CustomUrlRequest;
import com.mendhak.gpslogger.loggers.opengts.OpenGtsUdpJob;
import com.mendhak.gpslogger.senders.FileSender;
import com.mendhak.gpslogger.senders.GpxReader;
import com.mendhak.gpslogger.senders.SenderSettingsFactory;
import com.path.android.jobqueue.JobManager;
import org.slf4j.Logger;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpenGTSManager extends FileSender {

    private static final Logger LOG = Logs.of(OpenGTSManager.class);
    private PreferenceHelper preferenceHelper;

    public OpenGTSManager(PreferenceHelper preferenceHelper) {
        this.preferenceHelper = preferenceHelper;
    }

    @Override
    public void uploadFile(List<File> files) {
        // Use only gpx
        for (File f : files) {
            if (f.getName().endsWith(".gpx")) {
                List<SerializableLocation> locations = getLocationsFromGPX(f);
                LOG.debug(locations.size() + " points were read from " + f.getName());

                sendLocations(locations.toArray(new SerializableLocation[locations.size()]));

            }
        }
    }

    public void sendLocations(SerializableLocation[] locations){
        if (locations.length > 0) {
            OpenGTSSettings openGTSSettings = OpenGTSSettingsFactory.getOpenGTSSettings(preferenceHelper);

            if(openGTSSettings.getCommunicationMethod().equalsIgnoreCase("udp")){
                JobManager jobManager = AppSettings.getJobManager();
                jobManager.addJobInBackground(new OpenGtsUdpJob(openGTSSettings.getServer(),
                                                                openGTSSettings.getPort(),
                                                                openGTSSettings.getAccountName(),
                                                                openGTSSettings.getServerPath(),
                                                                openGTSSettings.getDeviceID(),
                                                                openGTSSettings.getCommunicationMethod(),
                                                                locations));
            }
            else {
                sendByHttp(openGTSSettings, locations);
            }

        }
    }

    void sendByHttp(OpenGTSSettings openGTSSettings, SerializableLocation[] locations) {
        for(SerializableLocation loc:locations){
            String finalUrl = getUrl(openGTSSettings, loc);

            JobManager jobManager = AppSettings.getJobManager();
            jobManager.addJobInBackground(new CustomUrlJob(new CustomUrlRequest(finalUrl), new UploadEvents.OpenGTS()));
        }
    }

    /**
     * Encode a location as GPRMC string data.
     * <p/>
     * For details check org.opengts.util.Nmea0183#_parse_GPRMC(String)
     * (OpenGTS source)
     *
     * @param loc location
     * @return GPRMC data
     */
    public static String gprmcEncode(SerializableLocation loc) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        DecimalFormat f = new DecimalFormat("0.000000", dfs);

        String gprmc = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,,",
                "$GPRMC",
                getNmeaGprmcTime(new Date(loc.getTime())),
                "A",
                getNmeaGprmcCoordinates(Math.abs(loc.getLatitude())),
                (loc.getLatitude() >= 0) ? "N" : "S",
                getNmeaGprmcCoordinates(Math.abs(loc.getLongitude())),
                (loc.getLongitude() >= 0) ? "E" : "W",
                f.format(Maths.mpsToKnots(loc.getSpeed())),
                f.format(loc.getBearing()),
                getNmeaGprmcDate(new Date(loc.getTime()))
        );

        gprmc += "*" + getNmeaChecksum(gprmc);

        return gprmc;
    }


    public static String getNmeaGprmcTime(Date dateToFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(dateToFormat);
    }

    public static String getNmeaGprmcDate(Date dateToFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(dateToFormat);
    }

    public static String getNmeaGprmcCoordinates(double coord) {
        // “DDDMM.MMMMM”
        int degrees = (int) coord;
        double minutes = (coord - degrees) * 60;

        DecimalFormat df = new DecimalFormat("00.00000", new DecimalFormatSymbols(Locale.US));
        StringBuilder rCoord = new StringBuilder();
        rCoord.append(degrees);
        rCoord.append(df.format(minutes));

        return rCoord.toString();
    }


    public static String getNmeaChecksum(String msg) {
        int chk = 0;
        for (int i = 1; i < msg.length(); i++) {
            chk ^= msg.charAt(i);
        }
        String chk_s = Integer.toHexString(chk).toUpperCase();
        while (chk_s.length() < 2) {
            chk_s = "0" + chk_s;
        }
        return chk_s;
    }

    public static String getUrl(OpenGTSSettings settings, SerializableLocation loc) {
        List<AbstractMap.SimpleEntry<String,String>> qparams = new ArrayList<>();
        qparams.add(new AbstractMap.SimpleEntry<>("id", settings.getDeviceID()));
        qparams.add(new AbstractMap.SimpleEntry<>("dev", settings.getDeviceID()));
        if (!Strings.isNullOrEmpty(settings.getAccountName())) {
            qparams.add(new AbstractMap.SimpleEntry<>("acct", settings.getAccountName()));
        } else {
            qparams.add(new AbstractMap.SimpleEntry<>("acct", settings.getDeviceID()));
        }

        //OpenGTS 2.5.5 requires batt param or it throws exception...
        qparams.add(new AbstractMap.SimpleEntry<>("batt", "0"));
        qparams.add(new AbstractMap.SimpleEntry<>("code", "0xF020"));
        qparams.add(new AbstractMap.SimpleEntry<>("alt", String.valueOf(loc.getAltitude())));
        qparams.add(new AbstractMap.SimpleEntry<>("gprmc", OpenGTSManager.gprmcEncode(loc)));

        if(settings.getServerPath().startsWith("/")){
            settings.setServerPath(settings.getServerPath().replaceFirst("/",""));
        }

        return String.format("%s://%s:%d/%s?%s",settings.getCommunicationMethod().toLowerCase(),
                                settings.getServer(),settings.getPort(),settings.getServerPath(),getQuery(qparams));
    }

    private static String getQuery(List<AbstractMap.SimpleEntry<String, String>> params)
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (AbstractMap.SimpleEntry<String, String> pair : params)
        {
            if (first) {
                first = false;
            }
            else {
                result.append("&");
            }

            result.append(pair.getKey());
            result.append("=");
            result.append(pair.getValue());
        }

        return result.toString();
    }

    @Override
    public boolean isAvailable() {
        SenderSettingsFactory factory = new OpenGTSSettingsFactory();
        return factory.getSettings(preferenceHelper).validSettings();
    }

    @Override
    public boolean hasUserAllowedAutoSending() {
        return preferenceHelper.isOpenGtsAutoSendEnabled();
    }

    private List<SerializableLocation> getLocationsFromGPX(File f) {
        List<SerializableLocation> locations = Collections.emptyList();
        try {
            locations = GpxReader.getPoints(f);
        } catch (Exception e) {
            LOG.error("OpenGTSManager.getLocationsFromGPX", e);
        }
        return locations;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().contains(".gpx");
    }
}