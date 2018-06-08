package com.mendhak.gpslogger.senders.sftp;

import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.senders.SenderSettings;
import com.mendhak.gpslogger.senders.SenderSettingsFactory;

public class SFTPSettingsFactory extends SenderSettingsFactory {
    @Override
    public SenderSettings getSettings(PreferenceHelper preferenceHelper) {
        return getSFTPSettings(preferenceHelper);
    }

    protected static SFTPSettings getSFTPSettings(PreferenceHelper preferenceHelper) {
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
