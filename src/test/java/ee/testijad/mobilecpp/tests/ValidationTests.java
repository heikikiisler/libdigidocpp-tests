package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.*;

public class ValidationTests {
    Server appiumServer;
    @BeforeClass
    public void setUp() {
        System.out.println("Before class");
        // Start server
        appiumServer = new Server();
    }

    @Test
    public void androidValidation() {
        System.out.println("Action");
        AppiumDriver driver = MobileDrivers.getAndroidDriver();
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
