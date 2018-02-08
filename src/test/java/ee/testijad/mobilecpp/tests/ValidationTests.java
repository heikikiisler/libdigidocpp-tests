package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.validation.*;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Set;

@Listeners({TestListener.class})
public class ValidationTests {

    private static ResultsParser resultsParser = ResultsParser.getDefault();
    private SoftAssert softAssert;

    @Test
    @Parameters({"fileName"})
    public void validateTestFile(String fileName) {
        TestFile expected = ValidationFiles.getExpectedTestFile(fileName);
        FileResult result = resultsParser.getTestFileResult(expected);
        softAssert = new SoftAssert();
        if (IgnoredFiles.isIgnored(expected.getFileName())) {
            throw new SkipException(String.format("Filename %s is ignored", expected.getFileName()));
        }
        softAssert.assertEquals(
                result.getResultType(),
                expected.getExpectedResultType(),
                "\n--------------------------------------------------\n[Result comparison (OK or NOT)]:"
        );
        compareSets(
                expected.getExpectedWarnings(),
                result.getWarnings(),
                getSetComparisonErrorMessage(expected, result)
        );
        compareSets(
                result.getWarnings(),
                expected.getExpectedWarnings(),
                getSetComparisonErrorMessage(expected, result)
        );
        softAssert.assertAll();
    }

    private void compareSets(Set<String> container, Set<String> contained, String errorMessage) {
        softAssert.assertTrue(container.containsAll(contained), errorMessage);
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
