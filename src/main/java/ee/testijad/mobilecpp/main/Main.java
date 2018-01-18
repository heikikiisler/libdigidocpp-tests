package ee.testijad.mobilecpp.main;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;


public class Main {
    public static void main(String[] args) {

   //     Utils.downloadFileFromAndroid("result.json");

        Server appiumServer = new Server();
        AppiumDriver driver = MobileDrivers.getAndroidDriver();
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(driver.getPageSource());

        appiumServer.stopServer();

    }
}
