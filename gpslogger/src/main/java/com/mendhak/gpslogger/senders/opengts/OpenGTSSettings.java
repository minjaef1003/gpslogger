package com.mendhak.gpslogger.senders.opengts;

import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.senders.SenderSettings;

public class OpenGTSSettings extends SenderSettings {
    private String server;
    private int port;
    private String serverPath;
    private String deviceID;
    private String accountName;
    private String communicationMethod;

    public OpenGTSSettings() {
        port = 0;
    }

    public OpenGTSSettings(String server, int port, String serverPath, String deviceID, String accountName, String communicationMethod) {
        this.server = server;
        this.port = port;
        this.serverPath = serverPath;
        this.deviceID = deviceID;
        this.accountName = accountName;
        this.communicationMethod = communicationMethod;
    }

    public OpenGTSSettings(OpenGTSSettings settings) {
        this.server = settings.getServer();
        this.port = settings.getPort();
        this.serverPath = settings.getServerPath();
        this.deviceID = settings.getDeviceID();
        this.accountName = settings.getAccountName();
        this.communicationMethod = settings.getCommunicationMethod();
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(String portString) {
        if(!Strings.isNullOrEmpty(portString)) {
            setPort(Strings.toInt(portString, 0));
        }
    }

    public int getPort() {
        return port;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setCommunicationMethod(String communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    public String getCommunicationMethod() {
        return communicationMethod;
    }

    public boolean validSettings() {
        if(!Strings.isNullOrEmpty(server)
                && port != 0
                && !Strings.isNullOrEmpty(communicationMethod)
                && !Strings.isNullOrEmpty(deviceID))
            return true;
        return false;
    }
}