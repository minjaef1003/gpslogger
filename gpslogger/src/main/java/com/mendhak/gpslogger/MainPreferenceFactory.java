package com.mendhak.gpslogger;

import android.content.Intent;

import com.mendhak.gpslogger.common.events.CommandEvents;

import de.greenrobot.event.EventBus;

/**
 * Created by Ji Hoon on 2018-06-09.
 */

public class MainPreferenceFactory {
    public String setPreferenceFragment(int identifier) {
        switch (identifier) {
            case 1000:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.GENERAL);
            case 1001:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.LOGGING);
            case 1002:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.PERFORMANCE);
            case 1003:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.UPLOAD);
            case 1004:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.GDOCS);
            case 1005:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.DROPBOX);
            case 1006:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.EMAIL);
            case 1007:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.FTP);
            case 1008:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.OPENGTS);
            case 1009:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.OSM);
            case 1010:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.OWNCLOUD);
            case 1015:
                return (MainPreferenceActivity.PREFERENCE_FRAGMENTS.SFTP);


        }
        return null;
    }
}
