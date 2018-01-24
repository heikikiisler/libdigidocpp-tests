package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.validation.ResultsParser;
import ee.testijad.mobilecpp.validation.TestFile;
import ee.testijad.mobilecpp.validation.ValidationFiles;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ValidationTests {

    private static ResultsParser resultsParser = ResultsParser.getDefault();

    @Test
    @Parameters({"fileName"})
    public void validateTestFile(String fileName) {
        TestFile expected = ValidationFiles.getExpectedTestFile(fileName);
        assertEquals(resultsParser.getTestFileResult(expected), expected.getExpectedResult());
    }

}
