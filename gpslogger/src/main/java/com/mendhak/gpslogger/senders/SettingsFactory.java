package com.mendhak.gpslogger.senders;

import com.mendhak.gpslogger.senders.owncloud.OwnCloudSettings;
import com.mendhak.gpslogger.senders.sftp.SFTPSettings;
import com.mendhak.gpslogger.common.PreferenceHelper;

public class SettingsFactory {

    public static OwnCloudSettings getOwnCloudSettings(PreferenceHelper preferenceHelper) {
        OwnCloudSettings ownCloudSettings = new OwnCloudSettings();

        ownCloudSettings.setServername(preferenceHelper.getOwnCloudServerName());
        ownCloudSettings.setUsername(preferenceHelper.getOwnCloudUsername());
        ownCloudSettings.setPassword(preferenceHelper.getOwnCloudPassword());
        ownCloudSettings.setDirectory(preferenceHelper.getOwnCloudDirectory());

        return ownCloudSettings;
    }

    public static SFTPSettings getSFTPSettings(PreferenceHelper preferenceHelper) {
        SFTPSettings sftpSettings = new SFTPSettings();

        sftpSettings.setRemoteServerPath(preferenceHelper.getSFTPRemoteServerPath());
        sftpSettings.setHost(preferenceHelper.getSFTPHost());
        sftpSettings.setPort(preferenceHelper.getSFTPPort());
        sftpSettings.setPrivateKeyFilePath(preferenceHelper.getSFTPPrivateKeyFilePath());
        sftpSettings.setPrivateKeyPassphrase(preferenceHelper.getSFTPPrivateKeyPassphrase());
        sftpSettings.setUser(preferenceHelper.getSFTPUser());
        sftpSettings.setPassword(preferenceHelper.getSFTPPassword());
        sftpSettings.setKnownHostKey(preferenceHelper.getSFTPKnownHostKey());

        return sftpSettings;
    }
}
