package ee.testijad.mobilecpp.drivers;

import ee.testijad.mobilecpp.util.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileDrivers {
    public static AppiumDriver getAndroidDriver(int port) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
        capabilities.setCapability(MobileCapabilityType.APP, Config.ANDROID_APP_FILE);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Config.ANDROID_MAIN_ACTIVITY);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Config.ANDROID_AUTOMATION_NAME);
        URL url = getServiceUrl(port);
        return new AndroidDriver(capabilities);
    }

    private static URL getServiceUrl(int port) {
        URL url = null;
        try {
            url = new URL(String.format("http://127.0.0.1:%d/wd/hub", port));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
