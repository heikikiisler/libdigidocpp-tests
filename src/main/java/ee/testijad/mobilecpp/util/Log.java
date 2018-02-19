package ee.testijad.mobilecpp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger LOGGER = LoggerFactory.getLogger("");

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        System.setProperty("org.slf4j.simpleLogger.levelInBrackets", "true");
        System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "dd.MM.yyyy HH:mm:ss.SSS");
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

}
