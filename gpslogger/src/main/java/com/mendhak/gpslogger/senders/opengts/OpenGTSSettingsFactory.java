package com.mendhak.gpslogger.senders.opengts;

import com.mendhak.gpslogger.common.PreferenceHelper;
import com.mendhak.gpslogger.senders.SenderSettings;
import com.mendhak.gpslogger.senders.SenderSettingsFactory;

public class OpenGTSSettingsFactory extends SenderSettingsFactory{
    @Override
    public SenderSettings getSettings(PreferenceHelper preferenceHelper) {
        return getOpenGTSSettings(preferenceHelper);
    }

    protected static OpenGTSSettings getOpenGTSSettings(PreferenceHelper preferenceHelper) {
        OpenGTSSettings openGTSSettings = new OpenGTSSettings();

        openGTSSettings.setServer(preferenceHelper.getOpenGTSServer());
        openGTSSettings.setPort(preferenceHelper.getOpenGTSServerPort());
        openGTSSettings.setServerPath(preferenceHelper.getOpenGTSServerPath());
        openGTSSettings.setDeviceID(preferenceHelper.getOpenGTSDeviceId());
        openGTSSettings.setAccountName(preferenceHelper.getOpenGTSAccountName());
        openGTSSettings.setCommunicationMethod(preferenceHelper.getOpenGTSServerCommunicationMethod());

        return openGTSSettings;
    }
}
