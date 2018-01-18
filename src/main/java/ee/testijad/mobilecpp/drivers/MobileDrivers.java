package ee.testijad.mobilecpp.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MobileDrivers {
    // TODO Add config parameters from properties file
    public static AppiumDriver getAndroidDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
        capabilities.setCapability(MobileCapabilityType.APP, "ee.ria.libdigidocpp3.apk");

        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "ee.ria.libdigidocpp.MainActivity");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");

        return new AndroidDriver(capabilities);
    }
}
