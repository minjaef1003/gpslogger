package com.mendhak.gpslogger.senders.owncloud;

import android.test.suitebuilder.annotation.SmallTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import de.greenrobot.event.EventBus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class OwnCloudJobTest {
    @Test
    public void shouldReRunOnThrowable_Test(){
        OwnCloudJob job = new OwnCloudJob("server", "user",
                "pw", "directory",
                new File("abc"), "filename");
        assertThat(job.shouldReRunOnThrowable(new Throwable()), is(false));
    }

    @Test
    public void getJobTag_Test(){
        OwnCloudJob job = new OwnCloudJob("server", "user",
                "pw", "directory",
                new File("abc"), "filename");
        assertThat(job.getJobTag(new File("file")), is("OWNCLOUDfile"));
    }
}