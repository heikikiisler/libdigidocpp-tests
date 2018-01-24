package ee.testijad.mobilecpp.drivers;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileDrivers {

    public static AppiumDriver getAndroidDriver(int port) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
        capabilities.setCapability(MobileCapabilityType.APP, Config.ANDROID_APP_FILE);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Config.ANDROID_MAIN_ACTIVITY);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Config.ANDROID_AUTOMATION_NAME);
        URL url = getServiceUrl(port);
        return new AndroidDriver(capabilities);
    }

    public static AppiumDriver getIosDriver(int port) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        capabilities.setCapability(MobileCapabilityType.UDID, "0BE05697-763C-4611-87E5-DC488FC3CC26");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "IOS device");
        //  capabilities.setCapability(MobileCapabilityType.APP, Config.IOS_APP_FILE);
        capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, Config.IOS_BUNDLE_ID);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);

        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, Config.IOS_AUTOMATION_NAME);
        capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, Utils.getFreePort());
        URL url = getServiceUrl(port);
        return new IOSDriver(capabilities);
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
