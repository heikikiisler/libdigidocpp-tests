package ee.testijad.mobilecpp.app;

import ee.testijad.mobilecpp.action.Action;
import ee.testijad.mobilecpp.appium.AppiumServer;
import ee.testijad.mobilecpp.drivers.MobileDrivers;
import ee.testijad.mobilecpp.server.HttpServer;
import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Log;
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
        Log.info(String.format("Starting %s validation app", System.getProperty("mobilecpp.os")));
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
        httpServer = new HttpServer(httpServerPort);
        Log.info(String.format("[Http server] path %s", baseUrl));
        Thread thread = new Thread(httpServer);
        thread.start();
        Utils.createResultsFolderIfNotExists();
    }

    private static void runAndroidApp() {
        Instant start, end;
        AppiumDriver driver = MobileDrivers.getAndroidDriver(communicationPort);
        Action.pasteHttpServerUrlAndRunValidationAndroid(driver, baseUrl);
        start = Instant.now();
        try {
            Action.waitForAndroidResult(driver, Config.VALIDATION_TIMEOUT);
        } catch (Exception e) {
            teardown();
            System.exit(1);
            e.printStackTrace();
        }
        end = Instant.now();
        double gap = ((double) ChronoUnit.MILLIS.between(start, end)) / 1000;
        Log.info(String.format("Working time: %.3f seconds ", gap));
    }

    private static void runIosApp() {
        Log.info("Action");
        Instant start, end;
        AppiumDriver driver = MobileDrivers.getIosDriver(communicationPort);
        Action.pasteHttpServerUrlAndRunValidation(driver, baseUrl);
        start = Instant.now();
        Action.waitForIosResult(driver, Config.VALIDATION_TIMEOUT);
        end = Instant.now();
        double gap = ((double) ChronoUnit.MILLIS.between(start, end)) / 1000;
        Log.info(String.format("Working time: %.3f seconds ", gap));
    }

    private static void teardown() {
        if (appiumServer != null) {
            appiumServer.stopServer();
        }
        if (httpServer != null) {
            httpServer.stopServer();
        }

    }
}
