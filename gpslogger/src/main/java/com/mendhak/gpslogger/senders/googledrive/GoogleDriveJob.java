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

package com.mendhak.gpslogger.senders.googledrive;

import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.mendhak.gpslogger.loggers.Streams;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import de.greenrobot.event.EventBus;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleDriveJob extends Job {
    private static final Logger LOG = Logs.of(GoogleDriveJob.class);
    private String token;
    private File gpxFile;
    private String googleDriveFolderName;

    protected GoogleDriveJob(File gpxFile, String googleDriveFolderName) {
        super(new Params(1).requireNetwork().persist().addTags(getJobTag(gpxFile)));
        this.setGpxFile(gpxFile);
        this.setGoogleDriveFolderName(googleDriveFolderName);

    }

    public static String getJobTag(File gpxFile){
        return "GOOGLEDRIVE" + gpxFile.getName();
    }

    private static Logger getLOG() {
        return LOG;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        GoogleDriveManager manager = new GoogleDriveManager(PreferenceHelper.getInstance());
        setToken(manager.getToken());

        FileInputStream fis = new FileInputStream(getGpxFile());
        String fileName = getGpxFile().getName();

        String gpsLoggerFolderId = PreferenceHelper.getInstance().getGoogleDriveFolderId();
        getLOG().debug("GPSLogger folder ID - " + gpsLoggerFolderId);

        if(Strings.isNullOrEmpty(gpsLoggerFolderId) || !folderExists(getToken(), gpsLoggerFolderId)){
            getLOG().debug("GPSLogger folder not found, searching by name");
            gpsLoggerFolderId = getFileIdFromFileName(getToken(), getGoogleDriveFolderName(), null);

            if(Strings.isNullOrEmpty(gpsLoggerFolderId)){
                getLOG().debug("GPSLogger folder still not found, will create");
                gpsLoggerFolderId = createEmptyFile(getToken(), getGoogleDriveFolderName(), "application/vnd.google-apps.folder", "root");
            }

            if (Strings.isNullOrEmpty(gpsLoggerFolderId)) {
                EventBus.getDefault().post(new UploadEvents.GDrive().failed("Could not create folder"));
                return;
            }

            PreferenceHelper.getInstance().setGoogleDriveFolderId(gpsLoggerFolderId);
        }

        //Now search for the file
        String gpxFileId = getFileIdFromFileName(getToken(), fileName, gpsLoggerFolderId);

        if (Strings.isNullOrEmpty(gpxFileId)) {
            //Create empty file first
            gpxFileId = createEmptyFile(getToken(), fileName, getMimeTypeFromFileName(fileName), gpsLoggerFolderId);

            if (Strings.isNullOrEmpty(gpxFileId)) {
                EventBus.getDefault().post(new UploadEvents.GDrive().failed("Could not create file"));
                return;
            }
        }

        if (!Strings.isNullOrEmpty(gpxFileId)) {
            //Set file's contents
            updateFileContents(getToken(), gpxFileId, Streams.getByteArrayFromInputStream(fis), fileName);
        }
        EventBus.getDefault().post(new UploadEvents.GDrive().succeeded());
    }

    private HttpURLConnection connConfigWhenUpdatingFileContents(HttpURLConnection conn, byte[] fileContents, String fileName, String fileUpdateUrl, String authToken)
    {
        try {
        setCommonConnProperty(conn, fileUpdateUrl, authToken);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", getMimeTypeFromFileName(fileName));
        conn.setRequestProperty("Content-Length", String.valueOf(fileContents.length));

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        }

        return conn;
    }

    private String updateFileContents(String authToken, String gpxFileId, byte[] fileContents, String fileName) {
        HttpURLConnection conn = null;
        String fileId = null;

        String fileUpdateUrl = "https://www.googleapis.com/upload/drive/v2/files/" + gpxFileId + "?uploadType=media";

        try {
            conn = connConfigWhenUpdatingFileContents(conn, fileContents, fileName, fileUpdateUrl, authToken);

            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.write(fileContents);
            wr.flush();
            wr.close();

            String fileMetadata = Streams.getStringFromInputStream(conn.getInputStream());

            JSONObject fileMetadataJson = new JSONObject(fileMetadata);
            fileId = fileMetadataJson.getString("id");
            getLOG().debug("File updated : " + fileId);

        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return fileId;
    }

    private void setCommonConnProperty(HttpURLConnection conn, String createFileUrl, String authToken)
    {
        try {
        URL url = new URL(createFileUrl);
        conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("User-Agent", "GPSLogger for Android");
        conn.setRequestProperty("Authorization", "Bearer " + authToken);

        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        }
    }

    private void connConfigWhenCreatingEmptyFile(HttpURLConnection conn, String createFileUrl, String authToken)
    {
        try{
        setCommonConnProperty(conn, createFileUrl, authToken);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        }
    }

    private String createEmptyFile(String authToken, String fileName, String mimeType, String parentFolderId) {
        HttpURLConnection conn = null;
        String fileId = null;

        String createFileUrl = "https://www.googleapis.com/drive/v2/files";

        String createFilePayload = "   {\n" +
                "             \"title\": \"" + fileName + "\",\n" +
                "             \"mimeType\": \"" + mimeType + "\",\n" +
                "             \"parents\": [\n" +
                "              {\n" +
                "               \"id\": \"" + parentFolderId + "\"\n" +
                "              }\n" +
                "             ]\n" +
                "            }";

        try {
            connConfigWhenCreatingEmptyFile(conn, createFileUrl, authToken);

            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.writeBytes(createFilePayload);
            wr.flush();
            wr.close();

            fileId = null;

            String fileMetadata = Streams.getStringFromInputStream(conn.getInputStream());

            JSONObject fileMetadataJson = new JSONObject(fileMetadata);
            fileId = fileMetadataJson.getString("id");
            getLOG().debug("File created with ID " + fileId + " of type " + mimeType);

        } catch (Exception e) {
            getLOG().error("Could not create file", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return fileId;
    }

    private void connConfigWhenGettingFileIdFromFileName(HttpURLConnection conn, String searchUrl, String authToken)
    {
        try{
        setCommonConnProperty(conn, searchUrl, authToken);
        conn.setRequestMethod("GET");
        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        }
    }

    private String getFileIdFromFileName(String authToken, String fileName, String inFolderId) {
        HttpURLConnection conn = null;
        String fileId = "";

        try {

            fileName = URLEncoder.encode(fileName, "UTF-8");

            String inFolderParam = "";
            if(!Strings.isNullOrEmpty(inFolderId)){
                inFolderParam = "+and+'" + inFolderId + "'+in+parents";
            }

            //To search in a folder:
            String searchUrl = "https://www.googleapis.com/drive/v2/files?q=title%20%3D%20%27" + fileName + "%27%20and%20trashed%20%3D%20false" + inFolderParam;

            connConfigWhenGettingFileIdFromFileName(conn, searchUrl, authToken);

            String fileMetadata = Streams.getStringFromInputStream(conn.getInputStream());


            JSONObject fileMetadataJson = new JSONObject(fileMetadata);
            if (fileMetadataJson.getJSONArray("items") != null && fileMetadataJson.getJSONArray("items").length() > 0) {
                fileId = fileMetadataJson.getJSONArray("items").getJSONObject(0).get("id").toString();
                getLOG().debug("Found file with ID " + fileId);
            }

        } catch (Exception e) {
            getLOG().error("SearchForGPSLoggerFile", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return fileId;
    }

    private void connConfigWhenCheckFolderExists(HttpURLConnection conn, String searchUrl, String authToken)
    {
        try{
        setCommonConnProperty(conn, searchUrl, authToken);
        conn.setRequestMethod("GET");
        } catch (Exception e) {
            getLOG().error("Could not update contents", e);
        }
    }

    private boolean folderExists(String authToken, String gpsLoggerFolderId) {
        HttpURLConnection conn = null;
        try{
            String searchUrl = "https://www.googleapis.com/drive/v2/files/" + gpsLoggerFolderId;

            connConfigWhenCheckFolderExists(conn, searchUrl, authToken);

            conn.connect();

            return conn.getResponseCode() == 200;
        }
        catch(Exception e){
            getLOG().error("folderExists", e);
        }

        return false;
    }


    private String getMimeTypeFromFileName(String fileName) {
        if (fileName.endsWith("kml")) {
            return "application/vnd.google-earth.kml+xml";
        }

        if (fileName.endsWith("gpx")) {
            return "application/gpx+xml";
        }

        if (fileName.endsWith("zip")) {
            return "application/zip";
        }

        if (fileName.endsWith("xml")) {
            return "application/xml";
        }

        if (fileName.endsWith("nmea")) {
            return "text/plain";
        }

        if (fileName.endsWith("geojson")){
            return "application/vnd.geo+json";
        }

        return "application/vnd.google-apps.spreadsheet";
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        getLOG().error("Could not upload to Google Drive", throwable);
        EventBus.getDefault().post(new UploadEvents.GDrive().failed("Could not upload to Google Drive", throwable));
        return false;
    }

    private String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    private File getGpxFile() {
        return gpxFile;
    }

    private void setGpxFile(File gpxFile) {
        this.gpxFile = gpxFile;
    }

    private String getGoogleDriveFolderName() {
        return googleDriveFolderName;
    }

    private void setGoogleDriveFolderName(String googleDriveFolderName) {
        this.googleDriveFolderName = googleDriveFolderName;
    }
}
