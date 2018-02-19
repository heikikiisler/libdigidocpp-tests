package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class IgnoredFiles {

    private static final Map<String, String> IGNORED_TEST_FILES = getIgnoredFiles();
    private static final String IGNORE_MESSAGE = ", Ignore message: ";

    private static Map<String, String> getIgnoredFiles() {
        Map<String, String> ignoredFiles = new HashMap<>();
        if (Config.IGNORED_FILES_FILE_NAME == null) {
            Log.info("Ignored files text file not defined in config, continuing without ignoring");
            return ignoredFiles;
        }
        Log.info("Reading ignored files list from " + Config.IGNORED_FILES_FILE_NAME);
        try {
            Files.readAllLines(Paths.get(Config.IGNORED_FILES_FILE_NAME))
                    .stream()
                    .filter(line -> !line.startsWith("#"))
                    .forEach(line -> {
                        if (!line.contains(",")) {
                            Log.error("Found faulty ignored files row: " + line);
                            Log.error("Comments must start with #");
                            throw new RuntimeException("Ignored files format error - must be separated by comma: file name,ignore message");
                        } else {
                            String[] ignoredFile = line.split(",");
                            ignoredFiles.put(ignoredFile[0], ignoredFile[1]);
                        }
                    });
        } catch (IOException e) {
            Log.info("Did not find ignored files text file, continuing without ignoring");
        }
        return ignoredFiles;
    }

    public static boolean isIgnored(String fileName) {
        // Ignore message stored in fileName TestNG parameter
        return fileName.contains(IGNORE_MESSAGE) || IGNORED_TEST_FILES.keySet().contains(fileName);
    }

    public static String getIgnoreMessage(String fileName) {
        if (fileName.contains(IGNORE_MESSAGE)) {
            return fileName.split(IGNORE_MESSAGE)[0];
        }
        return IGNORE_MESSAGE + IGNORED_TEST_FILES.get(fileName);
    }

}
