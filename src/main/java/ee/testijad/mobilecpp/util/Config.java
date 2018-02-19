package ee.testijad.mobilecpp.util;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final com.typesafe.config.Config CONFIG = getConfig();

    // Test parameters
    public static final String DATA_FILES_DIRECTORY = CONFIG.getString("testParameters.dataFilesDirectory");
    public static final String IGNORED_FILES_FILE_NAME = getStringIfHasPath("testParameters.ignoredFilesFileName");
    public static final String RESULT_FILES_DIRECTORY = CONFIG.getString("testParameters.resultFilesDirectory");
    public static final String TEST_SUITE_FILE_DIRECTORY = CONFIG.getString("testSuiteFileDirectory");
    public static final String VALIDATION_WARNING_FILE_NAME =
            DATA_FILES_DIRECTORY + "/" + CONFIG.getString("testParameters.validationWarningFileName");
    public static final String VALIDATION_ERROR_FILE_NAME =
            DATA_FILES_DIRECTORY + "/" + CONFIG.getString("testParameters.validationErrorFileName");
    public static final String ZIP_FILE_DIRECTORY = CONFIG.getString("testParameters.zipFilePath");

    // public static final String LIB_LOG_FILE = CONFIG.getString("testParameters.libLogFileName");
    // public static final String RESULTS_FILE = CONFIG.getString("testParameters.resultsFileName");
    public static final int VALIDATION_TIMEOUT = CONFIG.getInt("testParameters.validationTimeOut");
    public static final int POLLING_START_DELAY = CONFIG.getInt("testParameters.pollingStartDelay");
    public static final String HTTP_SERVER_HOST = getStringIfHasPath("testParameters.httpServerHostName");

    // Android parameters
    public static final String ANDROID_APP_ID = CONFIG.getString("appium.android.appId");
    public static final String ANDROID_APP_FILE = CONFIG.getString("appium.android.appFileName");
    public static final String ANDROID_MAIN_ACTIVITY = CONFIG.getString("appium.android.mainActivity");
    public static final String ANDROID_AUTOMATION_NAME = CONFIG.getString("appium.android.automationName");

    // Ios parameters
    public static final String IOS_BUNDLE_ID = CONFIG.getString("appium.ios.bundleId");
    public static final String IOS_APP_FILE = getStringIfHasPath("appium.ios.appFileName");

    public static final String IOS_AUTOMATION_NAME = CONFIG.getString("appium.ios.automationName");
    public static final String IOS_UDID = getStringIfHasPath("appium.ios.udid");
    public static final String IOS_XCODE_ORG_ID = getStringIfHasPath("appium.ios.xcode.orgID");
    public static final boolean IOS_XCODE_SHOW_LOG = getBooleanIfHasPath("appium.ios.xcode.showLog");
    public static final String IOS_XCODE_KEYCHAIN_PATH = getStringIfHasPath("appium.ios.xcode.keychainPath");
    public static final String IOS_XCODE_KEYCHAIN_PASSWORD = getStringIfHasPath("appium.ios.xcode.keychainPassword");
    public static final String IOS_WDA_BUNDLE_ID = getStringIfHasPath("appium.ios.updateWdaBundleID");

    public static final String APPIUM_LOG_LEVEL = getStringIfHasPath("appium.logLevel");

    private static com.typesafe.config.Config getConfig() {
        File configFile = new File("properties.conf");
        com.typesafe.config.Config config = ConfigFactory.parseFile(configFile);
        if (config.isEmpty()) {
            try {
                Log.info("Config not set up in \"properties.conf\". Copying from \"properties.conf.sample\"");
                FileUtils.copyFile(new File("properties.conf.sample"), configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    private static String getStringIfHasPath(String path) {
        if (CONFIG.hasPath(path)) {
            return CONFIG.getString(path);
        }
        return null;
    }

    private static boolean getBooleanIfHasPath(String path) {
        if (CONFIG.hasPath(path)) {
            return CONFIG.getBoolean(path);
        }
        return false;
    }
}
