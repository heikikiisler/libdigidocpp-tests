package ee.testijad.mobilecpp.validation;

import java.util.ArrayList;
import java.util.List;

public class TestFile {

    private String fileName;
    private ResultType expectedResultType;
    private List<String> expectedWarnings;

    TestFile(String fileName, ResultType expectedResultType, List<String> expectedWarnings) {
        this.fileName = fileName;
        this.expectedResultType = expectedResultType;
        this.expectedWarnings = expectedWarnings;
    }

    static TestFile getWithoutWarnings(String fileName) {
        return new TestFile(fileName, ResultType.OK, new ArrayList<>());
    }

    public String getFileName() {
        return fileName;
    }

    public ResultType getExpectedResultType() {
        return expectedResultType;
    }

    public List<String> getExpectedWarnings() {
        return expectedWarnings;
    }

}
