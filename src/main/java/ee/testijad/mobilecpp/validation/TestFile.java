package ee.testijad.mobilecpp.validation;

import java.util.ArrayList;
import java.util.List;

public class TestFile {

    private String fileName;
    private List<ResultType> expectedWarnings;

    public TestFile(String fileName) {
        this.fileName = fileName;
    }

    public TestFile(String fileName, List<ResultType> expectedWarnings) {
        this.fileName = fileName;
        this.expectedWarnings = expectedWarnings;
    }

    public TestFile(String fileName, ResultType expectedWarning) {
        this.fileName = fileName;
        expectedWarnings = new ArrayList<>();
        this.expectedWarnings.add(expectedWarning);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ResultType> getExpectedWarnings() {
        return expectedWarnings;
    }

    public void setExpectedWarnings(List<ResultType> expectedWarnings) {
        this.expectedWarnings = expectedWarnings;
    }

}
