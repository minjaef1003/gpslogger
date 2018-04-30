package com.mendhak.gpslogger.loggers.csv;

import android.location.Location;
import android.test.suitebuilder.annotation.SmallTest;

import com.mendhak.gpslogger.common.BundleConstants;
import com.mendhak.gpslogger.loggers.Files;
import com.mendhak.gpslogger.loggers.MockLocations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class CSVFileLoggerTest {

    @Test
    public void getCsvLine_BasicLocation_ReturnsBasicCSV(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,,,,,0,MOCK,,,,,,,,,\n";
        assertThat("Basic CSV line", actual, is(expected));

    }

    @Test
    public void getCsvLine_LocationWithAltitudeAccuracyBearing_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,,0,MOCK,,,,,,,,,\n";
        assertThat("CSV line with altitude, accuracy, bearing", actual, is(expected));
    }

    @Test
    public void getCsvLine_LocationWithSpeedProvider_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("BRAINS", 12.193, 19.111).withAltitude(101).withBearing(119).withSpeed(9).build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,,119.0,9.0,0,BRAINS,,,,,,,,,\n";
        assertThat("CSV line with speed and provider", actual, is(expected));
    }


    @Test
    public void getCsvLine_LocationWithSatellites_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.SATELLITES_FIX,22)
                .build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,,,,,,,,,\n";
        assertThat("CSV line with satellites or SATELLITES_FIX", actual, is(expected));
    }

    @Test
    public void getCsvLine_LocationWithHdopPdopVdop_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.HDOP, "LOOKATTHISHDOP!")
                .build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,LOOKATTHISHDOP!,,,,,,,,\n";
        assertThat("CSV line with HDOP", actual, is(expected));

        Location loc2 = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.HDOP, "LOOKATTHISHDOP!")
                .putExtra(BundleConstants.VDOP, "392.13")
                .putExtra(BundleConstants.PDOP, "Papepeodpe")
                .build();

        actual = plain.getCsvLine(loc2,"2011-09-17T18:45:33Z");
        expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,LOOKATTHISHDOP!,392.13,Papepeodpe,,,,,,\n";
        assertThat("CSV line with HDOP PDOP VDOP", actual, is(expected));
    }

    @Test
    public void getCsvLine_LocationWithGeoIdDgps_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.GEOIDHEIGHT,"tall")
                .putExtra(BundleConstants.AGEOFDGPSDATA, "oldddd")
                .putExtra(BundleConstants.DGPSID,"777")
                .build();


        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,,,,tall,oldddd,777,,,\n";
        assertThat("CSV line with geoid and dgps", actual, is(expected));

        Location loc2 = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.HDOP, "LOOKATTHISHDOP!")
                .putExtra(BundleConstants.VDOP, "392.13")
                .putExtra(BundleConstants.PDOP, "Papepeodpe")
                .putExtra(BundleConstants.GEOIDHEIGHT,"tall")
                .putExtra(BundleConstants.AGEOFDGPSDATA, "oldddd")
                .putExtra(BundleConstants.DGPSID,"777")
                .build();

        actual = plain.getCsvLine(loc2,"2011-09-17T18:45:33Z");
        expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,LOOKATTHISHDOP!,392.13,Papepeodpe,tall,oldddd,777,,,\n";
        assertThat("CSV line with all extras", actual, is(expected));
    }

    @Test
    public void getCsvLine_LocationWithActivity_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,null);
        Location loc = MockLocations.builder("MOCK", 12.222,14.151).putExtra(BundleConstants.DETECTED_ACTIVITY,"WALKING").build();

        String actual = plain.getCsvLine(loc,"2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,WALKING,,\n";

        assertThat("CSV with activity", actual, is(expected));

        Location loc2 = MockLocations.builder("MOCK", 12.193, 19.111).withAltitude(101).withAccuracy(41).withBearing(119).withSpeed(9)
                .putExtra("satellites",7)
                .putExtra(BundleConstants.HDOP, "LOOKATTHISHDOP!")
                .putExtra(BundleConstants.VDOP, "392.13")
                .putExtra(BundleConstants.PDOP, "Papepeodpe")
                .putExtra(BundleConstants.GEOIDHEIGHT,"tall")
                .putExtra(BundleConstants.AGEOFDGPSDATA, "oldddd")
                .putExtra(BundleConstants.DGPSID,"777")
                .putExtra(BundleConstants.DETECTED_ACTIVITY,"UNKNOWN")
                .build();

        actual = plain.getCsvLine(loc2,"2011-09-17T18:45:33Z");
        expected = "2011-09-17T18:45:33Z,12.193000,19.111000,101.0,41.0,119.0,9.0,7,MOCK,LOOKATTHISHDOP!,392.13,Papepeodpe,tall,oldddd,777,UNKNOWN,,\n";
        assertThat("CSV with activity", actual, is(expected));

    }


    @Test
    public void getCsvLine_LocationWithBattery_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,47);
        Location loc = MockLocations.builder("MOCK", 12.222,14.151).build();

        String actual = plain.getCsvLine(loc, "2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,,47,\n";

        assertThat("CSV With battery", actual, is(expected));

    }


    @Test
    public void getCsvLine_LocationWithAnnotation_ReturnsCSVLine(){
        CSVFileLogger plain = new CSVFileLogger(null,47);
        Location loc = MockLocations.builder("MOCK", 12.222,14.151).build();

        String actual = plain.getCsvLine("はい", loc, "2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,,47,\"はい\"\n";

        assertThat("CSV With annotation", actual, is(expected));

    }

    @Test
    public void getCsvLine_DescriptionContainsDoubleQuote_ReturnsCSVLineWithEscapedQuote(){
        CSVFileLogger plain = new CSVFileLogger(null,47);
        Location loc = MockLocations.builder("MOCK", 12.222,14.151).build();

        String actual = plain.getCsvLine("a..\"b\"..'c'", loc, "2011-09-17T18:45:33Z");
        String expected = "2011-09-17T18:45:33Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,,47,\"a..\"\"b\"\"..'c'\"\n";

        assertThat("CSV With annotation", actual, is(expected));

    }

    @Test
    /**
     * Purpose : check getName() function
     * Input : Null
     * Expected : return "TXT"
     */
    public void getName_ReturnName(){
        CSVFileLogger plain = new CSVFileLogger(null,null);

        String name = plain.getName();
        assertEquals("TXT", name);
    }

    @Test
    /**
     * Purpose : check annotate(String description, Location loc) function
     * Input : put  CSVFileLogger(fileAnnotate,47),   MockLocations.builder("MOCK", 12.222,14.151) -> print to fileAnnotate
     * Expected : "time,lat,lon,elevation,accuracy,bearing,speed,satellites,provider,hdop,vdop,pdop,geoidheight,ageofdgpsdata,dgpsid,activity,battery,annotation1970-01-01T00:00:00.000Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,,47,"testAnnotate"
     */
    public void annotate_CreateFile() throws IOException {
        File fileAnnotate = new File("textAnnotate.csv");
        fileAnnotate.delete();
        CSVFileLogger plain = new CSVFileLogger(fileAnnotate,47);
        Location loc = MockLocations.builder("MOCK", 12.222,14.151).build();
        String description = "testAnnotate";

        try {
            plain.annotate(description, loc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Files.reallyExists(fileAnnotate)) {
            FileInputStream reader = new FileInputStream(fileAnnotate);
            BufferedInputStream input = new BufferedInputStream(reader);
            String header =  "time,lat,lon,elevation,accuracy,bearing,speed,satellites,provider,hdop,vdop,pdop,geoidheight,ageofdgpsdata,dgpsid,activity,battery,annotation\n";

            String content = "1970-01-01T00:00:00.000Z,12.222000,14.151000,,,,,0,MOCK,,,,,,,,47,\"testAnnotate\"";
            byte [] tempHeader = new byte[header.getBytes().length];
            byte [] tempContent = new byte[content.getBytes().length];

            input.read(tempHeader);
            input.read(tempContent);
            input.close();

            String compareHeader = new String(tempHeader);
            String compareContent = new String(tempContent);

            String expected = header + content;
            String actual = compareHeader + compareContent;

            assertEquals(expected,actual);

        }
        else {
            // return failure;
            assertEquals(0,1);
        }
    }



}
