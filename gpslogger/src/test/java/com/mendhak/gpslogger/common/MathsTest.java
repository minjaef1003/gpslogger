package com.mendhak.gpslogger.common;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.test.suitebuilder.annotation.SmallTest;

import com.mendhak.gpslogger.loggers.MockLocations;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ji Hoon on 2018-03-27.
 */

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class MathsTest {
    @Test
    public void testCalculateDistance() {
        double result = Maths.calculateDistance(4,5,6,7);
        assertThat("calculate distance test",result, is(313900.7392720313));
    }

    @Test
    public void testMpsToknots() {
        double result = Maths.mpsToKnots(2.5);
        assertThat("testMpsToknots test",result,is(2.5*1.94384449));
    }

    @Test
    public void testGetBundledSatelliteCount() {
        Location loc = MockLocations.builder("MOCK", 12.222,14.151)
                .withAltitude(9001d)
                .withBearing(91.88f)
                .withSpeed(188.44f)
                .build();

        Bundle b = mock(Bundle.class);

        b.putInt("satellites",50 );
        loc.setExtras(b);
        when(loc.getExtras()).thenReturn(b);

        int result = Maths.getBundledSatelliteCount(loc);
        assertThat("getBundledSatelliteCount test",result,is(0));

    }
}