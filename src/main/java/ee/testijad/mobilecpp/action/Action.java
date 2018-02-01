package ee.testijad.mobilecpp.action;

import ee.testijad.mobilecpp.util.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Instant;

public class Action {

    // Android locators
    private static final String LOCATOR = String.format("%s:id/content", Config.ANDROID_APP_ID);
    private static final By TEXT_VIEW = MobileBy.xpath(String.format("//android.widget.TextView[@resource-id='%s']", LOCATOR));
    private static final By RUN_BUTTON_ANDROID = MobileBy.xpath(String.format("//android.widget.Button[@resource-id='%s:id/run']", Config.ANDROID_APP_ID));
    private static final By URL_FIELD_ANDROID = MobileBy.xpath(String.format("//android.widget.EditText[@resource-id='%s:id/search']", Config.ANDROID_APP_ID));

    // iOS locators
    private static final By RUN_BUTTON_IOS = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeButton'");
    private static final By URL_FIELD_IOS = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeTextField'");
    private static final By IOS_APP_FIELD = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeApplication' AND name == 'libdigidocpp-ios'");
    private static final By IOS_APP_DONE = MobileBy.name("DONE");

    public static void waitForResult(AppiumDriver driver, int timeoutInSeconds) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(timeoutInSeconds);
        while (true) {
            try {
                Thread.sleep(15000L);
                new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(TEXT_VIEW));
                // Can not catch or avoid UiAutomator2Exception of 10000 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (WebDriverException ignored) {
                if (Instant.now().isBefore(endTime)) {
                    System.err.println("##########################################################");
                    System.err.println("#### Ignore above exception, still polling for result ####");
                    System.err.println("##########################################################");
                    continue;
                } else {
                    throw new RuntimeException(String.format("Reached defined timeout of %s seconds while waiting for result", timeoutInSeconds));
                }
            }
            break;
        }
        MobileElement element = (MobileElement) driver.findElement(TEXT_VIEW);
        if (element.getText().contains("DONE")) {
            System.out.println("All files processed");
        } else {
            System.out.println(String.format("Not all files processed. Actual result: %s", element.getText()));
        }
    }

    public static void pasteHttpServerUrlAndRunValidation(AppiumDriver driver, String url) {
        driver.findElement(URL_FIELD_IOS).sendKeys(url);
        driver.findElement(RUN_BUTTON_IOS).click();
    }

    public static void pasteHttpServerUrlAndRunValidationAndroid(AppiumDriver driver, String url) {
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(URL_FIELD_ANDROID));
        driver.findElement(URL_FIELD_ANDROID).sendKeys(url);
        driver.findElement(RUN_BUTTON_ANDROID).click();
    }

    public static void waitForIosResult(AppiumDriver driver, int timeoutInSeconds) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(timeoutInSeconds);
        while (Instant.now().isBefore(endTime)) {
            try {
                Thread.sleep(15000L);
                WebElement waiter = new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(IOS_APP_DONE));
                if (waiter != null) {
                    System.out.println(" ******************************************** DONE **********************************");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void waitForAndroidResult(AppiumDriver driver, int timeoutInSeconds) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(timeoutInSeconds);
        while (Instant.now().isBefore(endTime)) {
            try {
                Thread.sleep(15000L);
                WebElement waiter = new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(TEXT_VIEW));
                if (waiter != null) {
                    System.out.println(" ******************************************** DONE **********************************");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
