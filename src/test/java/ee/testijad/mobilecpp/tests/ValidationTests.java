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
        return String.format("\n%s\n%s\n%s\n%s\n%s %s\n%s\n%s %s\n%s\n%s\n%s",
                "--------------------------------------------------",
                "[WARNINGS COMPARISON FAILURE:]",
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[All expected warnings:]",
                joinWarnings(expected.getExpectedWarnings()),
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[All result warnings:]",
                joinWarnings(result.getWarnings()),
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                validationInfo(),
                "- - - - - - - - - - - - - - - - - - - - - - - - - ",
                "[Result has all expected warnings]:"
        );
    }

    private static String validationInfo() {
        return String.format("*** libdigidocpp version: %s ; result file timestamp: %s ***",
                resultsParser.getVersionInfo(), resultsParser.getTimestamp());
    }

    private static String joinWarnings(Set<String> warningsSet) {
        if (warningsSet.size() > 0) {
            return String.join("\n", warningsSet) + "\n";
        } else {
            return "";
        }
    }
}
