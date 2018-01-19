package ee.testijad.mobilecpp.action;

import ee.testijad.mobilecpp.util.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Action {
    static String locator = String.format("%s:id/content", Config.ANDROID_APP_ID);
    static By by = MobileBy.xpath(String.format("//android.widget.TextView[@resource-id='%s']", locator));

    public static void waitForResult(AppiumDriver driver, int timeoutInSeconds) {
        try {
            Thread.sleep(25000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (timeoutInSeconds > 25) {
            timeoutInSeconds = timeoutInSeconds - 25;
        }
        try {
            new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
        }
        MobileElement element = (MobileElement) driver.findElement(by);
        if(element.getText().contains("DONE")) {
            System.out.println("All files processed");
        } else {
            System.out.println(String.format("Not all files processed. Actual result: %s", element.getText()));
        }
    }

}
