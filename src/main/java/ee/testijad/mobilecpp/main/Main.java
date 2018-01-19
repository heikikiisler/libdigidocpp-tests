package ee.testijad.mobilecpp.main;

import ee.testijad.mobilecpp.appium.Server;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.action.Action;
import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;


public class Main {
    public static void main(String[] args) {

        Utils.deleteFileFromAndroid(Config.RESULTS_FILE);
        Utils.deleteFileFromAndroid(Config.LIB_LOG_FILE);
        Utils.copyAllDataFiles(Config.DATA_FILES_DIRECTORY);
        int communicationPort = Utils.getFreePort();
        Server appiumServer = new Server(communicationPort);
        AppiumDriver driver = MobileDrivers.getAndroidDriver(communicationPort);
        Action.waitForResult(driver, Config.VALIDATION_TIMEOUT);
      //  System.out.println(driver.getPageSource());
        Utils.downloadFileFromAndroid(Config.RESULTS_FILE);
        Utils.downloadFileFromAndroid(Config.LIB_LOG_FILE);
        appiumServer.stopServer();
    }
}
