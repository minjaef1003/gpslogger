package com.mendhak.gpslogger.senders.owncloud;

import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.senders.SenderSettings;
import com.mendhak.gpslogger.senders.SenderSettingsFactory;

public class OwnCloudSettingsFactory extends SenderSettingsFactory {
    @Override
    public SenderSettings getSettings(PreferenceHelper preferenceHelper) {
        return getOwnCloudSettings(preferenceHelper);
    }

    protected static OwnCloudSettings getOwnCloudSettings(PreferenceHelper preferenceHelper) {
        OwnCloudSettings ownCloudSettings = new OwnCloudSettings();

        ownCloudSettings.setServername(preferenceHelper.getOwnCloudServerName());
        ownCloudSettings.setUsername(preferenceHelper.getOwnCloudUsername());
        ownCloudSettings.setPassword(preferenceHelper.getOwnCloudPassword());
        ownCloudSettings.setDirectory(preferenceHelper.getOwnCloudDirectory());

        return ownCloudSettings;
    }
}
