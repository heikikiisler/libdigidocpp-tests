package ee.testijad.mobilecpp.main;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.action.Action;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;


public class Main {
    public static void main(String[] args) {

        Utils.deleteFileFromAndroid("result.json");
        Utils.deleteFileFromAndroid("digidocpp.log");
        Utils.copyAllDataFiles("dataFiles");
        int communicationPort = Utils.getFreePort();
        Server appiumServer = new Server(communicationPort);
        AppiumDriver driver = MobileDrivers.getAndroidDriver(communicationPort);
        Action.waitForResult(driver, 100);
      //  System.out.println(driver.getPageSource());
        Utils.downloadFileFromAndroid("result.json");
        Utils.downloadFileFromAndroid("digidocpp.log");
        appiumServer.stopServer();
    }
}
