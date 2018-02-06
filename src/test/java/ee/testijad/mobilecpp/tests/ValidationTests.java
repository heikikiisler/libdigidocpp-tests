package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.validation.FileResult;
import ee.testijad.mobilecpp.validation.ResultsParser;
import ee.testijad.mobilecpp.validation.TestFile;
import ee.testijad.mobilecpp.validation.ValidationFiles;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners({TestListener.class})
public class ValidationTests {

    private static ResultsParser resultsParser = ResultsParser.getDefault();

    @Test
    @Parameters({"fileName"})
    public void validateTestFile(String fileName) {
        TestFile expected = ValidationFiles.getExpectedTestFile(fileName);
        FileResult result = resultsParser.getTestFileResult(expected);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(result.getResultType(), expected.getExpectedResultType(),
                "\n--------------------------------------------------\n[Result comparison (OK or NOT)]:");
        softAssert.assertTrue(result.getWarnings().containsAll(expected.getExpectedWarnings()),
                getSetComparisonErrorMessage(expected, result));
        softAssert.assertTrue(expected.getExpectedWarnings().containsAll(result.getWarnings()),
                getSetComparisonErrorMessage(expected, result));
        softAssert.assertAll();
    }

    private static String getSetComparisonErrorMessage(TestFile expected, FileResult result) {
        return String.format("\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
                "--------------------------------------------------",
                "[WARNINGS COMPARISON FAILURE:]",
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[All expected warnings:]",
                String.join("\n", expected.getExpectedWarnings()),
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[All result warnings:]",
                String.join("\n", result.getWarnings()),
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[Result has all expected warnings]:"
        );
    }

}
