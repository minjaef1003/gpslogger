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

package com.mendhak.gpslogger.senders.owncloud;

import com.mendhak.gpslogger.common.AppSettings;
import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.mendhak.gpslogger.loggers.Files;
import com.mendhak.gpslogger.senders.FileSender;
import com.mendhak.gpslogger.senders.SettingsFactory;
import com.mendhak.gpslogger.ui.fragments.settings.OwnCloudSettingsFragment;
import com.path.android.jobqueue.CancelResult;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.TagConstraint;
import de.greenrobot.event.EventBus;
import org.slf4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class OwnCloudManager extends FileSender
{
    private static final Logger LOG = Logs.of(OwnCloudSettingsFragment.class);
    private PreferenceHelper preferenceHelper;

    public OwnCloudManager(PreferenceHelper preferenceHelper) {
        this.preferenceHelper = preferenceHelper;
    }

    @Override
    public void uploadFile(List<File> files)
    {
        OwnCloudSettings settings = SettingsFactory.getOwnCloudSettings(preferenceHelper);
        for (File f : files) {
            uploadFile(f, settings);
        }
    }

    public void uploadFile(final File f, final OwnCloudSettings settings)
    {
        final JobManager jobManager = AppSettings.getJobManager();
        jobManager.cancelJobsInBackground(new CancelResult.AsyncCancelCallback() {
            @Override
            public void onCancelled(CancelResult cancelResult) {
                jobManager.addJobInBackground(new OwnCloudJob(settings, f, f.getName()));
            }
        }, TagConstraint.ANY, OwnCloudJob.getJobTag(f));
    }

    public void testOwnCloud(final OwnCloudSettings settings) {
        try {
            final File testFile = Files.createTestFile();
            uploadFile(testFile, settings);
        } catch (Exception ex) {
            EventBus.getDefault().post(new UploadEvents.Ftp().failed());
            LOG.error("Error while testing ownCloud upload: " + ex.getMessage());
        }

        LOG.debug("Added background ownCloud upload job");
    }

    @Override
    public boolean isAvailable() {
        OwnCloudSettings ownCloudSettings = SettingsFactory.getOwnCloudSettings(preferenceHelper);
        return ownCloudSettings.validSettings();
    }

    @Override
    public boolean hasUserAllowedAutoSending() {
        return preferenceHelper.isOwnCloudAutoSendEnabled();
    }

    @Override
    public boolean accept(File dir, String name) {
        return true;
    }


}