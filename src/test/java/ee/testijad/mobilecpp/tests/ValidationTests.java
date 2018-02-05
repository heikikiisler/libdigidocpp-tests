package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.validation.FileResult;
import ee.testijad.mobilecpp.validation.ResultsParser;
import ee.testijad.mobilecpp.validation.TestFile;
import ee.testijad.mobilecpp.validation.ValidationFiles;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ValidationTests {

    private static ResultsParser resultsParser = ResultsParser.getDefault();

    @Test
    @Parameters({"fileName"})
    public void validateTestFile(String fileName) {
        TestFile expected = ValidationFiles.getExpectedTestFile(fileName);
        FileResult result = resultsParser.getTestFileResult(expected);
        assertEquals(result.getResultType(), expected.getExpectedResultType());
        if (!expected.getExpectedWarnings().isEmpty())
        assertTrue(result.getWarnings().containsAll(expected.getExpectedWarnings()));
        assertTrue(expected.getExpectedWarnings().containsAll(result.getWarnings()));
    }

}
