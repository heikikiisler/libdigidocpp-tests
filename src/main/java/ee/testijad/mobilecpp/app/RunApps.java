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

public class RunApps {

    private static AppiumServer appiumServer;
    private static HttpServer httpServer;
    private static int communicationPort = Utils.getFreePort();
    private static int httpServerPort = Utils.getFreePort();
    private static String baseUrl = Utils.getBaseUrl(httpServerPort) + "sync";

    public static void main(String[] args) {
        System.out.println(String.format("Starting %s validation app", System.getProperty("mobilecpp.os")));
        switch (System.getProperty("mobilecpp.os").toLowerCase()) {
            case "android":
                setUp();
                runAndroidApp();
                break;
            case "ios":
                setUp();
                runIosApp();
                break;
            default:
                throw new RuntimeException("System property \"mobilecpp.os\" not set!");
        }
        teardown();
    }

    public static void setUp() {
        appiumServer = new AppiumServer(communicationPort);
        httpServer = new HttpServer(httpServerPort);
        System.out.println(String.format("[Http server] path %s", baseUrl));
        Thread thread = new Thread(httpServer);
        thread.start();
        Utils.createResultsFolderIfNotExists();
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
        AppiumDriver driver = MobileDrivers.getIosDriver(communicationPort);
        Action.pasteHttpServerUrlAndRunValidation(driver, baseUrl);
        start = Instant.now();
        Action.waitForIosResult(driver, Config.VALIDATION_TIMEOUT);
        end = Instant.now();
        if (start != null) {
            gap = ((double) ChronoUnit.MILLIS.between(start, end)) / 1000;
        }
        System.out.println(String.format("Working time: %.3f seconds ", gap));
    }

    private static void teardown() {
        System.out.println("After");
        // Stop server
        if (appiumServer != null) {
            appiumServer.stopServer();
        }
        if (httpServer != null) {
            // Add some time to writing files down
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            httpServer.stopServer();
        }

    }
}
