package ee.testijad.mobilecpp.validation;

import java.util.List;

public class TestFile {

    private String fileName;
    private ResultType expectedResult;
    private List<String> expectedWarnings;

    public TestFile(String fileName, ResultType expectedResult, List<String> expectedWarnings) {
        this.fileName = fileName;
        this.expectedWarnings = expectedWarnings;
        this.expectedResult = expectedResult;
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
}
