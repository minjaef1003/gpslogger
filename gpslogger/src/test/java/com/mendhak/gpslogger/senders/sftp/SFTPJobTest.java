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
        SFTPJob job = new SFTPJob(new File("file"), "remoteDir","host"
                , 1, "path", "phrase"
                , "user", "pw", "key");
        assertThat(job.shouldReRunOnThrowable(new Throwable()), is(false));
    }

    @Test
    public void getJobTag_Test(){
        SFTPJob job = new SFTPJob(new File("file"), "remoteDir","host"
                , 1, "path", "phrase"
                , "user", "pw", "key");
        assertThat(job.getJobTag(new File("name")), is("SFTPname"));
    }
}