package com.mendhak.gpslogger.senders;

import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.senders.dropbox.DropBoxManager;
import com.mendhak.gpslogger.senders.email.AutoEmailManager;
import com.mendhak.gpslogger.senders.ftp.FtpManager;
import com.mendhak.gpslogger.senders.googledrive.GoogleDriveManager;
import com.mendhak.gpslogger.senders.opengts.OpenGTSManager;
import com.mendhak.gpslogger.senders.osm.OpenStreetMapManager;
import com.mendhak.gpslogger.senders.owncloud.OwnCloudManager;
import com.mendhak.gpslogger.senders.sftp.SFTPManager;

import java.util.ArrayList;
import java.util.List;

public class FileSenderContext {

    public FileSender getOsmSender() {
        return new OpenStreetMapManager(PreferenceHelper.getInstance());
    }

    public FileSender getDropBoxSender() {
        return new DropBoxManager(PreferenceHelper.getInstance());
    }

    public FileSender getGoogleDriveSender() {
        return new GoogleDriveManager(PreferenceHelper.getInstance());
    }

    public FileSender getEmailSender() {
        return new AutoEmailManager(PreferenceHelper.getInstance());
    }

    public FileSender getOpenGTSSender() {
        return new OpenGTSManager(PreferenceHelper.getInstance());
    }

    public FileSender getFtpSender() {
        return new FtpManager(PreferenceHelper.getInstance());
    }

    public FileSender getOwnCloudSender() {
        return new OwnCloudManager(PreferenceHelper.getInstance());
    }

    public FileSender getSFTPSender() {
        return new SFTPManager(PreferenceHelper.getInstance());
    }

}
