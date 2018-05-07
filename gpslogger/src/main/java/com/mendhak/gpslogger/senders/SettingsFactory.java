package com.mendhak.gpslogger.senders;

import com.mendhak.gpslogger.senders.opengts.OpenGTSSettings;
import com.mendhak.gpslogger.senders.owncloud.OwnCloudSettings;
import com.mendhak.gpslogger.senders.sftp.SFTPSettings;
import com.mendhak.gpslogger.common.PreferenceHelper;

public class SettingsFactory {
    public static OpenGTSSettings getOpenGTSSettings(PreferenceHelper preferenceHelper) {
        OpenGTSSettings openGTSSettings = new OpenGTSSettings();

        openGTSSettings.setServer(preferenceHelper.getOpenGTSServer());
        openGTSSettings.setPort(preferenceHelper.getOpenGTSServerPort());
        openGTSSettings.setServerPath(preferenceHelper.getOpenGTSServerPath());
        openGTSSettings.setDeviceID(preferenceHelper.getOpenGTSDeviceId());
        openGTSSettings.setAccountName(preferenceHelper.getOpenGTSAccountName());
        openGTSSettings.setCommunicationMethod(preferenceHelper.getOpenGTSServerCommunicationMethod());

        return openGTSSettings;
    }

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
