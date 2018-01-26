package ee.testijad.mobilecpp.app;

import ee.testijad.mobilecpp.action.Action;
import ee.testijad.mobilecpp.appium.AppiumServer;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.server.HttpServer;
import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class RunApps {

    private static AppiumServer appiumServer;
    private static int communicationPort = Utils.getFreePort();

    public static void main(String[] args) {
        setUp();
        switch (System.getProperty("mobilecpp.os").toLowerCase()) {
            case "android":
                runAndroidApp();
                break;
            case "ios":
                runIosApp();
                break;
            default:
                throw new RuntimeException("System property \"mobilecpp.os\" not set!");
        }
        teardown();
    }

    public static void setUp() {
        appiumServer = new AppiumServer(communicationPort);
    }

    private static void runAndroidApp() {
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
            gap = ((double) ChronoUnit.MILLIS.between(start, end)) / 1000;
        }
        System.out.println(String.format("Working time: %.3f seconds ", gap));
        Utils.downloadFileFromAndroid(Config.RESULTS_FILE);
        Utils.downloadFileFromAndroid(Config.LIB_LOG_FILE);
    }

    private static void runIosApp() {
        System.out.println("Action");
        Instant start, end;
        double gap = 0;
        start = Instant.now();
        AppiumDriver driver = MobileDrivers.getIosDriver(communicationPort);
       // int httpServerPort = Utils.getFreePort();
        int httpServerPort = 60974;
        HttpServer server = new HttpServer(httpServerPort);
        Thread thread = new Thread(server);
        thread.start();
        System.out.println("Started");
        System.out.println(driver.getPageSource());
        // TODO paste URL & Start button
        Action.pasteHttpServerUrlAndRunValidation("URL");
        end = Instant.now();
        if (start != null) {
            gap = ((double) ChronoUnit.MILLIS.between(start, end)) / 1000;
        }
        System.out.println(String.format("Working time: %.3f seconds ", gap));
        server.stop();
    }

    private static void teardown() {
        System.out.println("After");
        // Stop server
        if (appiumServer != null) {
            appiumServer.stopServer();
        }
    }
}
