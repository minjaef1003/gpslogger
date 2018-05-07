package com.mendhak.gpslogger.loggers.opengts;

import com.mendhak.gpslogger.common.SerializableLocation;

public class OpenGtsUdpJobData {
    private String server;
    private int port ;
    private String accountName ;
    private String path ;
    private String deviceId ;
    private String communication;
    private SerializableLocation[] locations;

    public OpenGtsUdpJobData(String server, int port, String accountName, String path, String deviceId, String communication, SerializableLocation[] locations) {
        this.server = server;
        this.port = port;
        this.accountName = accountName;
        this.path = path;
        this.deviceId = deviceId;
        this.communication = communication;
        this.locations = locations;
    }
    public OpenGtsUdpJobData(OpenGtsUdpJobData  openGtsUdpJobData) {
        this.server = openGtsUdpJobData.getServer();
        this.port = openGtsUdpJobData.getPort();
        this.accountName = openGtsUdpJobData.getAccountName();
        this.path = openGtsUdpJobData.getPath();
        this.deviceId = openGtsUdpJobData.getDeviceId();
        this.communication = openGtsUdpJobData.getCommunication();
        this.locations = openGtsUdpJobData.getLocations();
    }

    public String getServer() {
        return server;
    }
    public int getPort() {
        return port;
    }
    public String getAccountName() {
        return accountName;
    }
    public String getPath() {
        return path;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public String getCommunication() {
        return communication;
    }
    public SerializableLocation[] getLocations() {
        return locations;
    }

    public void setServer(String server) {
        this.server = server;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public void setCommunication(String communication) {
        this.communication = communication;
    }
    public void setLocations(SerializableLocation[] locations) {
        this.locations = locations;
    }

    public void setGpsUdpJobData(OpenGtsUdpJobData openGtsUdpJobData) {
        this.server = openGtsUdpJobData.getServer();
        this.port = openGtsUdpJobData.getPort();
        this.accountName = openGtsUdpJobData.getAccountName();
        this.path = openGtsUdpJobData.getPath();
        this.deviceId = openGtsUdpJobData.getDeviceId();
        this.communication = openGtsUdpJobData.getCommunication();
        this.locations = openGtsUdpJobData.getLocations();
    }
    public void setGpsUdpJobData(String server, int port, String accountName, String path, String deviceId, String communication, SerializableLocation[] locations) {
        this.server = server;
        this.port = port;
        this.accountName = accountName;
        this.path = path;
        this.deviceId = deviceId;
        this.communication = communication;
        this.locations = locations;
    }
}
