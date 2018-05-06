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

package com.mendhak.gpslogger.senders.dropbox;




import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import de.greenrobot.event.EventBus;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class DropboxJob extends Job {


    private static final Logger LOG = Logs.of(DropboxJob.class);
    private static PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
    private String fileName;


    protected DropboxJob(String fileName) {
        super(new Params(1).requireNetwork().persist().addTags(getJobTag(fileName)));

        this.setFileName(fileName);
    }

    private static Logger getLOG() {
        return LOG;
    }

    private static PreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }

    private static void setPreferenceHelper(PreferenceHelper preferenceHelper) {
        DropboxJob.preferenceHelper = preferenceHelper;
    }

    @Override
    public void onAdded() {
        getLOG().debug("Dropbox job added");
    }

    @Override
    public void onRun() throws Throwable {
        File gpsDir = new File(getPreferenceHelper().getGpsLoggerFolder());
        File gpxFile = new File(gpsDir, getFileName());

        try {
            getLOG().debug("Beginning upload to dropbox...");
            InputStream inputStream = new FileInputStream(gpxFile);
            DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("GPSLogger").build();
            DbxClientV2 mDbxClient = new DbxClientV2(requestConfig, PreferenceHelper.getInstance().getDropBoxAccessKeyName());
            mDbxClient.files().uploadBuilder("/" + getFileName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(inputStream);
            EventBus.getDefault().post(new UploadEvents.Dropbox().succeeded());
        } catch (Exception e) {
            getLOG().error("Could not upload to Dropbox" , e);
            EventBus.getDefault().post(new UploadEvents.Dropbox().failed(e.getMessage(), e));
        }

    }


    @Override
    protected void onCancel() {
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        EventBus.getDefault().post(new UploadEvents.Dropbox().failed("Could not upload to Dropbox", throwable));
        getLOG().error("Could not upload to Dropbox", throwable);
        return false;
    }

    public static String getJobTag(String fileName) {
        return "DROPBOX" + fileName;
    }

    private String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
