package ee.testijad.mobilecpp.validation;

import java.util.HashSet;
import java.util.Set;

public class TestFile {

    private String fileName;
    private ResultType expectedResultType;
    private Set<String> expectedWarnings;

    TestFile(String fileName, ResultType expectedResultType, Set<String> expectedWarnings) {
        this.fileName = fileName.trim();
        this.expectedResultType = expectedResultType;
        this.expectedWarnings = expectedWarnings;
    }

    static TestFile getWithoutWarnings(String fileName) {
        return new TestFile(fileName, ResultType.OK, new HashSet<>());
    }

    public String getFileName() {
        return fileName;
    }

    public ResultType getExpectedResultType() {
        return expectedResultType;
    }

    public Set<String> getExpectedWarnings() {
        return expectedWarnings;
    }

}
