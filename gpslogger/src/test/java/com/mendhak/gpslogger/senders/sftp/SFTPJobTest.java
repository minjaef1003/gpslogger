package com.mendhak.gpslogger.senders.sftp;

import android.test.suitebuilder.annotation.SmallTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class SFTPJobTest {
    @Test
    public void shouldReRunOnThrowable_Test(){
        SFTPSettings settings = new SFTPSettings();
        settings.setRemoteServerPath("remoteDir");
        settings.setHost("host");
        settings.setPort(1);
        settings.setPrivateKeyFilePath("path");
        settings.setPrivateKeyPassphrase("phrase");
        settings.setUser("user");
        settings.setPassword("password");
        settings.setKnownHostKey("knownHostKey");
        SFTPJob job = new SFTPJob(new File("file"), settings);

        assertThat(job.shouldReRunOnThrowable(new Throwable()), is(false));
    }

    @Test
    public void getJobTag_Test(){
        SFTPSettings settings = new SFTPSettings();
        settings.setRemoteServerPath("remoteDir");
        settings.setHost("host");
        settings.setPort(1);
        settings.setPrivateKeyFilePath("path");
        settings.setPrivateKeyPassphrase("phrase");
        settings.setUser("user");
        settings.setPassword("password");
        settings.setKnownHostKey("knownHostKey");
        SFTPJob job = new SFTPJob(new File("file"), settings);

        assertThat(job.getJobTag(new File("name")), is("SFTPname"));
    }
}