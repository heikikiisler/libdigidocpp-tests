package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationFiles {

    private static final List<TestFile> testFiles = new ArrayList<>();
    private static final String SEPARATOR = "\t";

    static {
        ValidationFiles.addValidationFile(Config.VALIDATION_WARNING_FILE_PATH, ResultType.OK);
        ValidationFiles.addValidationFile(Config.VALIDATION_ERROR_FILE_PATH, ResultType.NOT);
    }

    /**
     * Gets test file with expected results.
     *
     * If the fileName is not found, it is assumed that the result is OK.
     */
    public static TestFile getExpectedTestFile(String fileName) {
        for (TestFile testFile : testFiles) {
            if (testFile.getFileName().equals(fileName)) {
                return testFile;
            }
        }
        return TestFile.getWithoutWarnings(fileName);
    }

    public static void addValidationFile(String fileName, ResultType resultType) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains(SEPARATOR)) {
                    String[] row = line.split(SEPARATOR);
                    if (row.length >= 3) {
                        testFiles.add(new TestFile(row[0], resultType, Arrays.asList(row[2].split("\\n"))));
                        System.out.println(row[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
