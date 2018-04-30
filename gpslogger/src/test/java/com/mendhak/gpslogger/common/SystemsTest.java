package com.mendhak.gpslogger.common;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.provider.Settings;
import android.test.suitebuilder.annotation.SmallTest;

import com.mendhak.gpslogger.GpsMainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ji Hoon on 2018-03-28.
 */
@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class SystemsTest {
    @Test
    public void testGetBatteryLevel() {
        Context context = mock(Context.class);
        Intent intent = mock(Intent.class);
        intent.putExtra(BatteryManager.EXTRA_LEVEL,10);
        intent.putExtra(BatteryManager.EXTRA_SCALE,10);

        when(context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)))
                .thenReturn(new Intent(context, GpsMainActivity.class));
        assertThat("getBatteryLevel Test",Systems.getBatteryLevel(context),is(0));
    }

    @Test
    public void testGetAndroidId() {
        Context context = mock(Context.class);
        assertThat("getAndroidId Test",Systems.getAndroidId(context),is(Settings.Secure.ANDROID_ID));
    }

    @Test
    public void testIsNetworkAvailable() {
        Context context = mock(Context.class);
        assertThat("isNetworkAvailable test",Systems.isNetworkAvailable(context),is(false));
    }



}