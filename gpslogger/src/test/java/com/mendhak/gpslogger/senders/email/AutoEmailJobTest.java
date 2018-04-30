package com.mendhak.gpslogger.senders.email;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class AutoEmailJobTest {
    @Test
    public void shouldReRunOnThrowableTest(){
        File[] files = {new File("file1"), new File("file2")};
        AutoEmailJob job = new AutoEmailJob("server", "username", "password", "usessl", true, "csv", "fromaddress", "subject", "body", files);
        assertThat(job.shouldReRunOnThrowable(new Throwable()), is(false));
    }

    @Test
    public void getJobTagTest(){
        File[] files = {new File("file1"), new File("file2")};
        AutoEmailJob job = new AutoEmailJob("server", "username", "password", "usessl", true, "csv", "fromaddress", "subject", "body", files);

        assertThat(job.getJobTag(files), is("EMAILfile1.file2."));
    }
}