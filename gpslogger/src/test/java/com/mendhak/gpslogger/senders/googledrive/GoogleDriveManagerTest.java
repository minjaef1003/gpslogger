package com.mendhak.gpslogger.senders.googledrive;


import android.test.suitebuilder.annotation.SmallTest;
import com.mendhak.gpslogger.common.PreferenceHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class GoogleDriveManagerTest {
    @Test
    public void getOauth2ScopeTest(){
        PreferenceHelper pm = mock(PreferenceHelper.class);
        GoogleDriveManager gdm = new GoogleDriveManager(pm);

        assertThat(gdm.getOauth2Scope(), is("oauth2:https://www.googleapis.com/auth/drive.file"));
    }

    @Test
    public void Accept_FileFilter_AcceptsAllFileTypes(){
        PreferenceHelper pm = mock(PreferenceHelper.class);
        GoogleDriveManager gdm = new GoogleDriveManager(pm);

        assertThat("Any file type", gdm.accept(null, null), is(true));
        assertThat("Any file type", gdm.accept(new File("/"), "abc.xyz"), is(true));
    }

    @Test
    public void IsAvailable_AccountAndToken_IsAvailable(){
        PreferenceHelper pm = mock(PreferenceHelper.class);
        when(pm.getGoogleDriveAccountName()).thenReturn("XXXXXXX");
        when(pm.getGoogleDriveAuthToken()).thenReturn("YYYYYYYYYYY");
        when(pm.isGDocsAutoSendEnabled()).thenReturn(false);

        GoogleDriveManager gdm = new GoogleDriveManager(pm);
        assertThat("Account and token indicate availability", gdm.isAvailable(), is(true));
    }

    @Test
    public void IsAutoSendAvailable_UserCheckedAutosend_IsAvailable(){
        PreferenceHelper pm = mock(PreferenceHelper.class);
        when(pm.getGoogleDriveAccountName()).thenReturn("XXXXXXX");
        when(pm.getGoogleDriveAuthToken()).thenReturn("YYYYYYYYYYY");
        when(pm.isGDocsAutoSendEnabled()).thenReturn(true);

        GoogleDriveManager gdm = new GoogleDriveManager(pm);
        assertThat("Account and token indicate availability", gdm.isAutoSendAvailable(), is(true));
    }




}