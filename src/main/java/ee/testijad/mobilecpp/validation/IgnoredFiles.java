package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IgnoredFiles {

    private static final List<String> IGNORED_TEST_FILES = getIgnoredFiles();

    private static List<String> getIgnoredFiles() {
        List<String> ignoredFileNames = new ArrayList<>();
        if (Config.IGNORED_FILES_FILE_NAME == null) {
            System.out.println("Ignored files text file not defined in config, continuing without ignoring");
            return ignoredFileNames;
        }
        try {
            Files.readAllLines(Paths.get(Config.IGNORED_FILES_FILE_NAME))
                    .forEach(fileName -> ignoredFileNames.add(fileName.trim()));
        } catch (IOException e) {
            System.out.println("Did not find ignored files text file, continuing without ignoring");
        }
        return ignoredFileNames;
    }

    public static boolean isIgnored(String fileName) {
        return IGNORED_TEST_FILES.contains(fileName.trim());
    }

}
