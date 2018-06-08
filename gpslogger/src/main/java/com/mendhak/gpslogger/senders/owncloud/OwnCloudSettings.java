package com.mendhak.gpslogger.senders.owncloud;

import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.senders.SenderSettings;

public class OwnCloudSettings extends SenderSettings {
    private String servername;
    private String username;
    private String password;
    private String directory;

    public OwnCloudSettings() {}

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getServername() {
        return servername;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public boolean validSettings(){
        return !Strings.isNullOrEmpty(servername);
    }
}
