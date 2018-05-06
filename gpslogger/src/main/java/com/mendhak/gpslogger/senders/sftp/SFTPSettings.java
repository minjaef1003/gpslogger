package com.mendhak.gpslogger.senders.sftp;

import com.mendhak.gpslogger.common.Strings;

public class SFTPSettings {
    private String RemoteServerPath;
    private String Host;
    private int Port;
    private String PrivateKeyFilePath;
    private String PrivateKeyPassphrase;
    private String User;
    private String Password;
    private String KnownHostKey;

    public SFTPSettings() {
    }

    public void setRemoteServerPath(String remoteServerPath) {
        RemoteServerPath = remoteServerPath;
    }

    public String getRemoteServerPath() {
        return RemoteServerPath;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getHost() {
        return Host;
    }

    public void setPort(int port) {
        Port = port;
    }

    public int getPort() {
        return Port;
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        PrivateKeyFilePath = privateKeyFilePath;
    }

    public String getPrivateKeyFilePath() {
        return PrivateKeyFilePath;
    }

    public void setPrivateKeyPassphrase(String privateKeyPassphrase){
        PrivateKeyPassphrase = privateKeyPassphrase;
    }

    public String getPrivateKeyPassphrase() {
        return PrivateKeyPassphrase;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getUser() {
        return User;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public void setKnownHostKey(String knownHostKey) {
        KnownHostKey = knownHostKey;
    }

    public String getKnownHostKey() {
        return KnownHostKey;
    }

    public boolean validSettings() {
        if (Strings.isNullOrEmpty(RemoteServerPath)
                || Strings.isNullOrEmpty(Host)
                || Port <= 0
                || (Strings.isNullOrEmpty(PrivateKeyFilePath) && Strings.isNullOrEmpty(Password) )){
            return false;
        }
        return true;
    }
}

