package ee.testijad.mobilecpp.util;

import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {

    private static final com.typesafe.config.Config CONFIG = getConfig();

    // Test parameters
    public static final String DATA_FILES_DIRECTORY = CONFIG.getString("testParameters.dataFilesDirectory");
    public static final String RESULT_FILES_DIRECTORY = CONFIG.getString("testParameters.resultFilesDirectory");
    public static final String TEST_SUITE_FILE_DIRECTORY = CONFIG.getString("testSuiteFileDirectory");
    public static final String VALIDATION_WARNING_FILE_PATH =
            DATA_FILES_DIRECTORY + "/" + CONFIG.getString("testParameters.validationWarningFileName");
    public static final String VALIDATION_ERROR_FILE_PATH =
            DATA_FILES_DIRECTORY + "/" + CONFIG.getString("testParameters.validationErrorFileName");
    public static final String TEST_RESULTS_DIRECTORY = CONFIG.getString("testParameters.testResultsDirectory");

    public static final String LIB_LOG_FILE = CONFIG.getString("testParameters.libLogFileName");
    public static final String RESULTS_FILE = CONFIG.getString("testParameters.resultsFileName");
    public static final int VALIDATION_TIMEOUT = CONFIG.getInt("testParameters.validationTimeOut");

    // Android parameters
    public static final String ANDROID_APP_ID = CONFIG.getString("appium.android.appId");
    public static final String ANDROID_APP_FILE = CONFIG.getString("appium.android.appFileName");
    public static final String ANDROID_MAIN_ACTIVITY = CONFIG.getString("appium.android.mainActivity");
    public static final String ANDROID_AUTOMATION_NAME = CONFIG.getString("appium.android.automationName");

    // Ios parameters
    public static final String IOS_BUNDLE_ID = CONFIG.getString("appium.ios.bundleId");
    public static final String IOS_APP_FILE = CONFIG.getString("appium.ios.appFileName");

    public static final String IOS_AUTOMATION_NAME = CONFIG.getString("appium.ios.automationName");

    private static com.typesafe.config.Config getConfig() {
        File configFile = new File("properties.conf");
        com.typesafe.config.Config config = ConfigFactory.parseFile(configFile);
        if (config.isEmpty()) {
            try {
                System.out.println("Config not set up in \"properties.conf\". Copying from \"properties.conf.sample\"");
                FileUtils.copyFile(new File("properties.conf.sample"), configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}
