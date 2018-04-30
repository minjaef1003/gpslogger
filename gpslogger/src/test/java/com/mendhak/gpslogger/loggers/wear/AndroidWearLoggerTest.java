package com.mendhak.gpslogger.loggers.wear;

import android.test.suitebuilder.annotation.SmallTest;
import android.content.Context;
import android.location.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static junit.framework.Assert.assertEquals;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.mendhak.gpslogger.loggers.MockLocations;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class AndroidWearLoggerTest {
    /**
     * Purpose : make constructor test
     * Input : AndroidWearLogger(Context)
     * Expected :
     */
    @Test
    public void makeConstructorTest() {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
    }
    /**
     * Purpose : check write() function
     * Input : write(Location)
     * Expected : don't working test
     */
    @Test
    public void writeTest() throws Exception {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        Location loc = null;
        testWearLogger.write(loc);
    }
    /**
     * Purpose : check annotate() function
     * Input : annotate(String, Location)
     * Expected :
     */
    @Test
    public void annotateTest() throws Exception {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        Location loc = null;
        String testStr = "";
        testWearLogger.annotate(testStr, loc);
    }

    /**
     * Purpose : check getName() function
     * Input : getName()
     * Expected : return ""
     */
    @Test
    public void getNameTest(){
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        String testStr = "";
        assertEquals(testStr, testWearLogger.getName());
    }
    /**
     * Purpose : check onConnected() function
     * Input : onConnected(Null)
     * Expected :
     */
    @Test
    public void onConnectedTest() {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        testWearLogger.onConnected(null);
    }

    /**
     * Purpose : check onConnected() function
     * Input : onConnected(Bundle)
     * Expected :
     */
    @Test
    public void onConnectedTestwithBundle() {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        Bundle bundle = new Bundle();
        testWearLogger.onConnected(bundle);
    }

    /**
     * Purpose : check onConnectionSuspended() function
     * Input : onConnectionSuspended(int)
     * Expected :
     */
    @Test
    public void onConnectionSuspendedTest() {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        testWearLogger.onConnectionSuspended(1);
    }
    /**
     * Purpose : check onConnectionFailed() function
     * Input : onConnectionFailed()
     * Expected :
     */
    @Test
    public void onConnectionFailedTest() {
        Context context = null;
        AndroidWearLogger testWearLogger = new AndroidWearLogger(context);
        ConnectionResult connectionResult = new ConnectionResult(1);
        connectionResult.isSuccess();
        testWearLogger.onConnectionFailed(connectionResult);
    }
}