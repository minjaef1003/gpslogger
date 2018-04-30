package com.mendhak.gpslogger.loggers.gpx;

import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import com.mendhak.gpslogger.loggers.MockLocations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertNotNull;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class Gpx10FileLoggerTest {
    @Test
    /**
     * Purpose : check getWriteHandler() function
     * Input : getWriteHandler()
     * Expected : Gpx10WriteHandler(null, null, null, true);
     * */
    public void getWriteHandler(){
        Gpx10FileLogger gpx10FileLogger = new Gpx10FileLogger(null, true);
        Runnable  gpx10WriteHandler = gpx10FileLogger.getWriteHandler(null, null, null, true);
        assertNotNull(gpx10WriteHandler);
    }

    @Test
    /**
     * 진행중.
     * Purpose : check write(Location loc) function
     * Input : write(Location loc)
     * Expected :
     * */
    public void write() throws Exception {
        File fileGPX10Write = new File("GPX10Write.csv");
        Gpx10FileLogger gpx10FileLogger = new Gpx10FileLogger(fileGPX10Write, true);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).build();
        gpx10FileLogger.write(loc);

//        String expected = "<trkpt lat=\"12.193\" lon=\"19.111\"><ele>9001.0</ele><time>2011-09-17T18:45:33Z</time>" +
//                "<course>91.88</course><speed>188.44</speed><src>MOCK</src></trkpt>\n</trkseg></trk></gpx>";
//        byte [] bytes = new byte[expected.getBytes().length];
//        RandomAccessFile raf = new RandomAccessFile(fileGPX10Write, "rw");
//        raf.seek((fileGPX10Write.length() -  "</trk></gpx>".length()));
//        raf.read(bytes);
//        raf.close();
//        String result = new String(bytes);
//        assertEquals(expected, result);
    }
}