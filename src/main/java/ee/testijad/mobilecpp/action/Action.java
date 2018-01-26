package ee.testijad.mobilecpp.action;

import ee.testijad.mobilecpp.util.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Instant;

public class Action {

    private static final String LOCATOR = String.format("%s:id/content", Config.ANDROID_APP_ID);
    private static final By TEXT_VIEW = MobileBy.xpath(String.format("//android.widget.TextView[@resource-id='%s']", LOCATOR));

    public static void waitForResult(AppiumDriver driver, int timeoutInSeconds) {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plusSeconds(timeoutInSeconds);
        while (true) {
            try {
                new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(TEXT_VIEW));
                // Can not catch or avoid UiAutomator2Exception of 10000 ms
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

}
