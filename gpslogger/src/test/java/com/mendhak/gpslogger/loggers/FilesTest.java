package com.mendhak.gpslogger.loggers;

import android.test.suitebuilder.annotation.SmallTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;



@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class FilesTest {



    @Test
    public void GetFilesInFolder_WhenNullOrEmpty_ReturnEmptyList() {
        assertThat("Null File object should return empty list", Files.fromFolder(null), notNullValue());

        assertThat("Empty folder should return empty list", Files.fromFolder(new File("/")), notNullValue());

    }
    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType(null)
     * Expected : return ""
     */
    @Test
    public void getMimeTypeTestInputNull() {
        Files testFiles = new Files();
        String testStr = "";
        assertEquals("", testFiles.getMimeType(testStr));
    }

    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType("filename.gpx")
     * Expected : return "application/gpx+xml"
     */
    @Test
    public void getMimeTypeTestInputGpx() {
        Files testFiles = new Files();
        String testStr = "filename.gpx";
        assertEquals("application/gpx+xml", testFiles.getMimeType(testStr));
    }
    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType("filename.kml")
     * Expected : return "application/vnd.google-earth.kml+xml"
     */
    @Test
    public void getMimeTypeTestInputKml() {
        Files testFiles = new Files();
        String testStr = "filename.kml";
        assertEquals("application/vnd.google-earth.kml+xml", testFiles.getMimeType(testStr));
    }
    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType("filename.zip")
     * Expected : return "application/zip"
     */
    @Test
    public void getMimeTypeTestInputZip() {
        Files testFiles = new Files();
        String testStr = "filename.zip";
        assertEquals("application/zip", testFiles.getMimeType(testStr));
    }
    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType("filename")
     * Expected : return "application/octet-stream"
     */
    @Test
    public void getMimeTypeTestInputNotExt() {
        Files testFiles = new Files();
        String testStr = "filename";
        assertEquals("application/octet-stream", testFiles.getMimeType(testStr));
    }
    /**
     * Purpose : check getMimeType() function
     * Input : getMimeType("filename.anotherExtention")
     * Expected : return "application/octet-stream"
     */
    @Test
    public void getMimeTypeTestInputAnotherType() {
        Files testFiles = new Files();
        String testStr = "filename.html";
        assertEquals("application/octet-stream", testFiles.getMimeType(testStr));
    }
    /**
     * Purpose : check isAllowedToWriteTo() function
     * Input : isAllowedToWriteTo("")
     * Expected : true
     */
    @Test
    public void isAllowedToWriteToTest() {
        Files testFiles = new Files();
        String testStr = "";
        assertEquals(false, testFiles.isAllowedToWriteTo(testStr));
    }
    /**
     * Purpose : check reallyExists() function
     * Input : reallyExists()
     * Expected : false
     */
    @Test
    public void reallyExistsTest() {
        Files testFiles = new Files();
        File testFile = new File("", "gpslogger_test.xml");
        assertEquals(false, testFiles.reallyExists(testFile));
    }
}