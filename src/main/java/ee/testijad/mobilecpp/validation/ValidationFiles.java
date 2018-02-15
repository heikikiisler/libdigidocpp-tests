package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ValidationFiles {

    private static final List<TestFile> TEST_FILES = new ArrayList<>();
    private static final String SEPARATOR = "\t";
    private static final String WARNINGS_SEPARATOR = "/n";
    private static final int TEST_FILE_NAME_ROW = 0;
    private static final int LIBDIGIDOCPP_WARNING_ROW = 2;
    private static final int DDOC_WARNING_ROW = 1;

    static {
        ValidationFiles.addValidationFile(Config.VALIDATION_WARNING_FILE_NAME, ResultType.OK, LIBDIGIDOCPP_WARNING_ROW);
        ValidationFiles.addValidationFile(Config.VALIDATION_ERROR_FILE_NAME, ResultType.NOT, LIBDIGIDOCPP_WARNING_ROW);
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

    private static void addValidationFile(String fileName, ResultType resultType, int warningRow) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains(SEPARATOR)) {
                    String[] row = line.split(SEPARATOR);
                    if (row.length >= warningRow + 1) {
                        String testFileName = row[TEST_FILE_NAME_ROW].trim();
                        int fileWarningRow = warningRow;
                        if (testFileName.endsWith(".ddoc")) {
                            System.out.println("Filename: " + testFileName);
                            fileWarningRow = DDOC_WARNING_ROW;
                        }
                        TEST_FILES.add(new TestFile(
                                testFileName,
                                resultType,
                                Utils.getWarningSetFromString(row[fileWarningRow], WARNINGS_SEPARATOR)
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find validation text file " + fileName);
        }
    }

}
