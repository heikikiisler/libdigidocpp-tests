package ee.testijad.mobilecpp.validation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationFiles {

    private static final List<TestFile> testFiles = new ArrayList<>();
    private static final String SEPARATOR = "\t";

    public static List<String> getExpectedWarnings(String fileName) {
        for (TestFile testFile : testFiles) {
            if (testFile.getFileName().equals(fileName)) {
                return testFile.getExpectedWarnings();
            }
        }
        return null;
    }

    public static void addValidationFile(String fileName, ResultType resultType) {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#") && line.contains(SEPARATOR)) {
                    String[] row = line.split(SEPARATOR);
                    if (row.length >= 3) {
                        System.out.println(Arrays.toString(row));
                        testFiles.add(new TestFile(row[0], resultType, Arrays.asList(row[2].split("\\n"))));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
