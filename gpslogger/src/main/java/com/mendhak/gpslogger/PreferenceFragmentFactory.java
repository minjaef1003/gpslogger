package com.mendhak.gpslogger;

import android.content.Context;
import android.preference.PreferenceFragment;

import com.mendhak.gpslogger.ui.fragments.settings.AutoEmailFragment;
import com.mendhak.gpslogger.ui.fragments.settings.CustomUrlFragment;
import com.mendhak.gpslogger.ui.fragments.settings.DropboxAuthorizationFragment;
import com.mendhak.gpslogger.ui.fragments.settings.FtpFragment;
import com.mendhak.gpslogger.ui.fragments.settings.GeneralSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.GoogleDriveSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.LoggingSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.OSMAuthorizationFragment;
import com.mendhak.gpslogger.ui.fragments.settings.OpenGTSFragment;
import com.mendhak.gpslogger.ui.fragments.settings.OwnCloudSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.PerformanceSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.SFTPSettingsFragment;
import com.mendhak.gpslogger.ui.fragments.settings.UploadSettingsFragment;

/**
 * Created by Ji Hoon on 2018-06-09.
 */

public class PreferenceFragmentFactory {
    private int titleStringId;
    public PreferenceFragment getInstance(String whichFragment) {
        switch(whichFragment){
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.GENERAL:
                titleStringId = (R.string.settings_screen_name);
                return new GeneralSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.LOGGING:
                titleStringId = (R.string.pref_logging_title);
                return new LoggingSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.PERFORMANCE:
                titleStringId = (R.string.pref_performance_title);
                return new PerformanceSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.UPLOAD:
                titleStringId = (R.string.title_drawer_uploadsettings);
               return new UploadSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.FTP:
                titleStringId = (R.string.autoftp_setup_title);
                return new FtpFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.EMAIL:
                titleStringId = (R.string.autoemail_title);
                return new AutoEmailFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.OPENGTS:
                titleStringId = (R.string.opengts_setup_title);
                return new OpenGTSFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.CUSTOMURL:
                titleStringId = (R.string.log_customurl_title);
                return new CustomUrlFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.GDOCS:
                titleStringId = (R.string.gdocs_setup_title);
                return new GoogleDriveSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.DROPBOX:
                titleStringId = (R.string.dropbox_setup_title);
                return new DropboxAuthorizationFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.OSM:
                titleStringId = (R.string.osm_setup_title);
                return new OSMAuthorizationFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.OWNCLOUD:
                titleStringId = (R.string.owncloud_setup_title);
                return new OwnCloudSettingsFragment();
            case MainPreferenceActivity.PREFERENCE_FRAGMENTS.SFTP:
                titleStringId = (R.string.sftp_setup_title);
                return new SFTPSettingsFragment();
        }
        return null;
    }
    public int getTitleStringId() {
        return titleStringId;
    }
}
