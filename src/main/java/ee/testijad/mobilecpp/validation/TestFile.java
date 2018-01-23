package ee.testijad.mobilecpp.validation;

import java.util.List;

public class TestFile {

    private String fileName;
    private ResultType expectedResult;
    private List<String> expectedWarnings;

    public TestFile(String fileName, ResultType expectedResult, List<String> expectedWarnings) {
        this.fileName = fileName;
        this.expectedResult = expectedResult;
        this.expectedWarnings = expectedWarnings;
    }

    public static TestFile getWithoutWarnings(String fileName) {
        return new TestFile(fileName, ResultType.OK, null);
    }

    public String getFileName() {
        return fileName;
    }

    public ResultType getExpectedResult() {
        return expectedResult;
    }

    public List<String> getExpectedWarnings() {
        return expectedWarnings;
    }

    public boolean equals(TestFile testFile) {
        return fileName.equals(testFile.fileName) &&
               expectedResult == testFile.expectedResult &&
               expectedWarnings.containsAll(testFile.expectedWarnings) &&
               testFile.expectedWarnings.containsAll(expectedWarnings);
    }

}
