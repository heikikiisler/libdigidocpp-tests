package ee.testijad.mobilecpp.appium;

import ee.testijad.mobilecpp.util.Utils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumServer {

    private AppiumDriverLocalService appiumService;

    public AppiumServer(int port) {
        startServer(port);
    }

    private void startServer(int port) {
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(port)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, String.valueOf(Utils.getFreePort()))
                .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE);
        appiumService = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumService.start();
    }

    public void stopServer() {
        appiumService.stop();
    }
}
