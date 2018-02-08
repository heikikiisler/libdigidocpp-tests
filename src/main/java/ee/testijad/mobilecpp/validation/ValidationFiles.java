package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ValidationFiles {

    private static final List<TestFile> TEST_FILES = new ArrayList<>();
    private static final String SEPARATOR = "\t";
    private static final String WARNINGS_SEPARATOR = "/n";

    static {
        ValidationFiles.addValidationFile(Config.VALIDATION_WARNING_FILE_NAME, ResultType.OK);
        ValidationFiles.addValidationFile(Config.VALIDATION_ERROR_FILE_NAME, ResultType.NOT);
    }

    /**
     * Gets test file with expected results.
     *
     * If the fileName is not found, it is assumed that the result is OK.
     */
    public static TestFile getExpectedTestFile(String fileName) {
        for (TestFile testFile : TEST_FILES) {
            if (testFile.getFileName().equals(fileName)) {
                return testFile;
            }
        }
        return TestFile.getWithoutWarnings(fileName);
    }

    private static void addValidationFile(String fileName, ResultType resultType) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains(SEPARATOR)) {
                    String[] row = line.split(SEPARATOR);
                    if (row.length >= 3) {
                        String testFileName = row[0].trim();
                        TEST_FILES.add(new TestFile(
                                testFileName,
                                resultType,
                                Utils.getWarningSetFromString(row[2], WARNINGS_SEPARATOR)
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find validation text file " + fileName);
        }
    }

}
