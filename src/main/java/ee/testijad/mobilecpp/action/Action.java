package ee.testijad.mobilecpp.action;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Action {

    // Android locators
    private static final By ANDROID_APP_DONE = MobileBy.xpath("//android.widget.TextView[contains(@text,'DONE')]");
    private static final By RUN_BUTTON_ANDROID = MobileBy.xpath(String.format("//android.widget.Button[@resource-id='%s:id/run']", Config.ANDROID_APP_ID));
    private static final By URL_FIELD_ANDROID = MobileBy.xpath(String.format("//android.widget.EditText[@resource-id='%s:id/search']", Config.ANDROID_APP_ID));

    // iOS locators
    private static final By RUN_BUTTON_IOS = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeButton'");
    private static final By URL_FIELD_IOS = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeTextField'");
    private static final By IOS_APP_FIELD = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeApplication' AND name == 'libdigidocpp-ios'");
    private static final By IOS_APP_DONE = MobileBy.name("DONE");

    public static void pasteHttpServerUrlAndRunValidation(AppiumDriver driver, String url) {
        driver.findElement(URL_FIELD_IOS).sendKeys(url);
        driver.findElement(RUN_BUTTON_IOS).click();
    }

    public static void pasteHttpServerUrlAndRunValidationAndroid(AppiumDriver driver, String url) {
        System.out.println(String.format("++++++++++++ Paste sleep started %s", Utils.getLocalTimeStamp()));
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("+++++++++++++ Paste sleep ended %s", Utils.getLocalTimeStamp()));
        new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(URL_FIELD_ANDROID));
        System.out.println(String.format("+++++++++++++ URL field waiting ended %s", Utils.getLocalTimeStamp()));
        driver.findElement(URL_FIELD_ANDROID).sendKeys(url);
        driver.findElement(RUN_BUTTON_ANDROID).click();
        System.out.println(String.format("+++++++++++++ Clicked %s", Utils.getLocalTimeStamp()));
    }

    public static void waitForIosResult(AppiumDriver driver, int timeoutInSeconds) {
        System.out.println(String.format("++++++++++++ Result waiting started %s", Utils.getLocalTimeStamp()));
        Instant startTime = Instant.now();
        long delay = 1000L * Config.POLLING_START_DELAY;
        Instant endTime = startTime.plusSeconds(timeoutInSeconds);
        while (Instant.now().isBefore(endTime)) {
            try {
                Thread.sleep(delay);
                WebElement waiter = new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(IOS_APP_DONE));
                if (waiter != null) {
                    System.out.println(String.format(" ******************************************** DONE ********************************** %s", Utils.getLocalTimeStamp()));
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println(String.format("++++++++++++ Error %s", Utils.getLocalTimeStamp()));
                e.printStackTrace();
            }
        }
    }

    public static void waitForAndroidResult(AppiumDriver driver, int timeoutInSeconds) {
        long delay = 1000L * Config.POLLING_START_DELAY;
        System.out.println(String.format("++++++++++++ Result waiting started %s", Utils.getLocalTimeStamp()));
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println(String.format("++++++++++++ Error %s", Utils.getLocalTimeStamp()));
                e.printStackTrace();
        }
        System.out.println(String.format("++++++++++++ \"DONE\" polling started %s", Utils.getLocalTimeStamp()));
        // Throws error and prints 2 stack stack traces that can not be caught
        boolean success = false;
        try {
            WebElement waiter = new WebDriverWait(driver, timeoutInSeconds)
                    .pollingEvery(10, TimeUnit.SECONDS)
                    .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(ANDROID_APP_DONE));
            if (waiter != null) {
                System.out.println(String.format(" ******************************************** DONE ********************************** %s", Utils.getLocalTimeStamp()));
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!success) {
            System.out.println(String.format("++++++++++++ \"DONE\" polling ended with error ************************** %s", Utils.getLocalTimeStamp()));
        }
    }
}
