package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.ee.testijad.mobileccp.action.Action;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.*;

public class ValidationTests {
    Server appiumServer;
    int communicationPort = Utils.getFreePort();
    @BeforeClass
    public void setUp() {
        System.out.println("Before class");
        // Start server
        appiumServer = new Server(communicationPort);
    }

    @Test
    public void androidValidation() {
        System.out.println("Action");
        Utils.deleteFileFromAndroid("result.json");
        Utils.deleteFileFromAndroid("digidocpp.log");
        Utils.copyAllDataFiles("dataFiles");
        AppiumDriver driver = MobileDrivers.getAndroidDriver(communicationPort);
        Action.waitForResult(driver, 100);
        Utils.downloadFileFromAndroid("result.json");
        Utils.downloadFileFromAndroid("digidocpp.log");
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
