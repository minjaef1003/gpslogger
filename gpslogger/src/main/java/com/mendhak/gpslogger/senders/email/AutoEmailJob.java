/*
 * Copyright (C) 2016 mendhak
 *
 * This file is part of GPSLogger for Android.
 *
 * GPSLogger for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * GPSLogger for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mendhak.gpslogger.senders.email;

import android.util.Base64;
import com.mendhak.gpslogger.common.AppSettings;
import com.mendhak.gpslogger.common.network.Networks;
import com.mendhak.gpslogger.common.Strings;
import com.mendhak.gpslogger.common.events.UploadEvents;
import com.mendhak.gpslogger.common.slf4j.Logs;
import com.mendhak.gpslogger.loggers.Files;
import com.mendhak.gpslogger.loggers.Streams;
import com.mendhak.gpslogger.common.network.LocalX509TrustManager;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import de.greenrobot.event.EventBus;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.smtp.*;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class AutoEmailJob extends Job {

    private static final Logger LOG = Logs.of(AutoEmailJob.class);
    private String smtpServer;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private boolean smtpUseSsl;
    private String csvEmailTargets;
    private String fromAddress;
    private String subject;
    private String body;
    private File[] files;
    static ArrayList<String> smtpServerResponses;
    static UploadEvents.AutoEmail smtpFailureEvent;



    protected AutoEmailJob(String smtpServer,
                           String smtpPort, String smtpUsername, String smtpPassword,
                           boolean smtpUseSsl, String csvEmailTargets, String fromAddress,
                            String subject, String body, File[] files) {
        super(new Params(1).requireNetwork().persist().addTags(getJobTag(files)));
        this.setSmtpServer(smtpServer);
        this.setSmtpPort(smtpPort);
        this.setSmtpPassword(smtpPassword);
        this.setSmtpUsername(smtpUsername);
        this.setSmtpUseSsl(smtpUseSsl);
        this.setCsvEmailTargets(csvEmailTargets);
        this.setFromAddress(fromAddress);
        this.setSubject(subject);
        this.setBody(body);
        this.setFiles(files);

        smtpServerResponses = new ArrayList<>();
        smtpFailureEvent = null;
    }

    private static Logger getLOG() {
        return LOG;
    }

    @Override
    public void onAdded() {

    }

    private void appendHeaderWhenEmailMultipleTargets(String target, SimpleSMTPHeader header) throws Throwable
    {
        for (String ccTarget : getCsvEmailTargets().split(",")) {
            if (!ccTarget.equalsIgnoreCase(target)) {
                header.addCC(ccTarget);
            }
        }
    }
    private void emailWithBody(Writer writer, SimpleSMTPHeader header) throws Throwable
    {
        header.addHeaderField("Content-Type", "text/plain; charset=UTF-8");
        writer.write(header.toString());
        writer.write(getBody());
    }
    private void emailWithFiles(Writer writer, SimpleSMTPHeader header) throws Throwable
    {
        String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
        header.addHeaderField("Content-Type", "multipart/mixed; boundary=" + boundary);
        writer.write(header.toString());

        writer.write("--" + boundary + "\n");
        writer.write("Content-Type: text/plain; charset=UTF-8" + "\n\n");
        writer.write(getBody());
        writer.write("\n");

        attachFilesToWriter(writer, boundary, getFiles());
        writer.write("--" + boundary + "--\n\n");
    }

    @Override
    public void onRun() throws Throwable {

        int port = Strings.toInt(getSmtpPort(),25);

        if (Strings.isNullOrEmpty(getFromAddress())) {
            setFromAddress(getSmtpUsername());
        }

        AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();

        try {

            client.addProtocolCommandListener(new ProtocolCommandListener() {
                @Override
                public void protocolCommandSent(ProtocolCommandEvent event) {
                    getLOG().debug(event.getMessage());
                    smtpServerResponses.add(event.getMessage());
                }

                @Override
                public void protocolReplyReceived(ProtocolCommandEvent event) {
                    getLOG().debug(event.getMessage());
                    smtpServerResponses.add(event.getMessage());
                }
            });

            if(isSmtpUseSsl()){
                client = new AuthenticatingSMTPClient("TLS",true);
            }


            // optionally set a timeout to have a faster feedback on errors
            client.setDefaultTimeout(10*1000);
            checkReply(client);
            getLOG().debug("Connecting to SMTP Server");
            client.connect(getSmtpServer(), port);
            checkReply(client);
            // you say ehlo  and you specify the host you are connecting from, could be anything
            client.ehlo("localhost");
            checkReply(client);
            // if your host accepts STARTTLS, we're good everything will be encrypted, otherwise we're done here
            getLOG().debug("Checking TLS...");

            client.setTrustManager(new LocalX509TrustManager(Networks.getKnownServersStore(AppSettings.getInstance())));
            if(!isSmtpUseSsl() && client.execTLS()){
                client.ehlo("localhost");
            }

            client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, getSmtpUsername(), getSmtpPassword());
            checkReply(client);

            client.setSender(getFromAddress());
            checkReply(client);

            String target = getCsvEmailTargets().split(",")[0];
            client.addRecipient(target);

            checkReply(client);

            Writer writer = client.sendMessageData();

            if (writer != null) {


                SimpleSMTPHeader header = new SimpleSMTPHeader(getFromAddress(), target, getSubject());

                //Multiple email targets?
                appendHeaderWhenEmailMultipleTargets(target, header); // extract method



                // Regular email with just a body
                if (getFiles() == null || getFiles().length == 0)
                {
                    emailWithBody(writer, header);// extract method
                }
                // Attach files in a multipart way
                else
                {
                    emailWithFiles(writer, header);// extract method
                }


                writer.close();
                if (!client.completePendingCommand()) {// failure
                    smtpFailureEvent = new UploadEvents.AutoEmail().failed("Failure to send the email");
                }
                else {
                    EventBus.getDefault().post(new UploadEvents.AutoEmail().succeeded());
                }
            }
            else {
                smtpFailureEvent = new UploadEvents.AutoEmail().failed("Failure to send the email");
            }

        }
        catch (Exception e) {
            getLOG().error("Could not send email ", e);
            smtpFailureEvent = new UploadEvents.AutoEmail().failed("Could not send email " + e.getMessage() , e);
        }
        finally {
            try{
                client.logout();
                client.disconnect();
            }
            catch (Exception ignored) {
            }

            if(smtpFailureEvent != null){
                smtpFailureEvent.smtpMessages = smtpServerResponses;
                if(smtpFailureEvent.smtpMessages.isEmpty()){
                    smtpFailureEvent.smtpMessages = new ArrayList<>(Arrays.asList(client.getReplyStrings()));
                }
                EventBus.getDefault().post(smtpFailureEvent);
            }
        }

    }



    /**
     * Append the given attachments to the message which is being written by the given writer.
     *
     * @param boundary separates each file attachment
     */
    private static void attachFilesToWriter(Writer writer, String boundary, File[] files) throws IOException {
        for (File f : files) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) f.length());
            FileInputStream inputStream = new FileInputStream(f);
            Streams.copyIntoStream(inputStream, outputStream);

            writer.write("--" + boundary + "\n");
            writer.write("Content-Type: application/" + Files.getMimeType(f.getName()) + "; name=\"" + f.getName() + "\"\n");
            writer.write("Content-Disposition: attachment; filename=\"" + f.getName() + "\"\n");
            writer.write("Content-Transfer-Encoding: base64\n\n");
            String encodedFile = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            writer.write(encodedFile);
            writer.write("\n");
        }
    }


    private static void checkReply(SMTPClient sc) throws Exception {
        if (SMTPReply.isNegativeTransient(sc.getReplyCode())) {
            throw new Exception("Transient SMTP error " +  sc.getReplyString());
        } else if (SMTPReply.isNegativePermanent(sc.getReplyCode())) {
            throw new Exception("Permanent SMTP error " +  sc.getReplyString());
        }
    }

    @Override
    protected void onCancel() {
        getLOG().debug("Email job cancelled");
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        getLOG().error("Could not send email", throwable);
        return false;
    }

    public static String getJobTag(File[] files) {
        StringBuilder sb = new StringBuilder();
        for(File f : files){
            sb.append(f.getName()).append(".");
        }
        return "EMAIL" + sb.toString();

    }

    private String getSmtpServer() {
        return smtpServer;
    }

    private void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    private String getSmtpPort() {
        return smtpPort;
    }

    private void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    private String getSmtpUsername() {
        return smtpUsername;
    }

    private void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    private String getSmtpPassword() {
        return smtpPassword;
    }

    private void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    private boolean isSmtpUseSsl() {
        return smtpUseSsl;
    }

    private void setSmtpUseSsl(boolean smtpUseSsl) {
        this.smtpUseSsl = smtpUseSsl;
    }

    private String getCsvEmailTargets() {
        return csvEmailTargets;
    }

    private void setCsvEmailTargets(String csvEmailTargets) {
        this.csvEmailTargets = csvEmailTargets;
    }

    private String getFromAddress() {
        return fromAddress;
    }

    private void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    private String getSubject() {
        return subject;
    }

    private void setSubject(String subject) {
        this.subject = subject;
    }

    private String getBody() {
        return body;
    }

    private void setBody(String body) {
        this.body = body;
    }

    private File[] getFiles() {
        return files;
    }

    private void setFiles(File[] files) {
        this.files = files;
    }
}
