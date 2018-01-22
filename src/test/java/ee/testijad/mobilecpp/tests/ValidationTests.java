package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.action.Action;
import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ValidationTests {

    private Server appiumServer;
    private int communicationPort = Utils.getFreePort();

    @BeforeClass
    public void setUp() {
        System.out.println("Before class");
        // Start server
        appiumServer = new Server(communicationPort);
    }

    @Test
    public void androidValidation() {
        System.out.println("Action");
        Utils.deleteFileFromAndroid(Config.RESULTS_FILE);
        Utils.deleteFileFromAndroid(Config.LIB_LOG_FILE);
        Utils.copyAllDataFiles(Config.DATA_FILES_DIRECTORY);
        Instant start, end;
        double gap = 0;
        start = Instant.now();
        AppiumDriver driver = MobileDrivers.getAndroidDriver(communicationPort);
        Action.waitForResult(driver, Config.VALIDATION_TIMEOUT);
        end = Instant.now();
        if (start != null) {
            gap = ((double) ChronoUnit.MILLIS.between(start, end))/1000;
        }
        System.out.println(String.format("Working time: %.3f seconds ", gap));
        Utils.downloadFileFromAndroid(Config.RESULTS_FILE);
        Utils.downloadFileFromAndroid(Config.LIB_LOG_FILE);
    }

    @AfterClass
    public void teardown() {
        System.out.println("After");
        // Stop server
        if (appiumServer != null) {
            appiumServer.stopServer();
        }
    }
}
