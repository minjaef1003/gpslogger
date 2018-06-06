package com.mendhak.gpslogger.senders;

import com.mendhak.gpslogger.common.PreferenceHelper;

public abstract class SenderSettingsFactory {
    public abstract SenderSettings getSettings(PreferenceHelper preferenceHelper);
}
