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
        if (IgnoredFiles.isIgnored(fileName)) {
            throw new SkipException(fileName);
        }
        TestFile expected = ValidationFiles.getExpectedTestFile(fileName);
        FileResult result = resultsParser.getTestFileResult(expected);
        softAssert = new SoftAssert();
        softAssert.assertEquals(
                result.getResultType(),
                expected.getExpectedResultType(),
                "\n--------------------------------------------------\n[Result comparison (OK or NOT)]:"
        );
        compareSets(
                result.getWarnings(),
                expected.getExpectedWarnings(),
                getSetComparisonErrorMessage(expected, result)
        );
        softAssert.assertAll();
    }

    private void compareSets(Set<String> resultWarnings, Set<String> expectedWarnings, String errorMessage) {
        boolean containsAll = true;
        for (String expectedWarning : expectedWarnings) {
            boolean containsWarning = false;
            for (String resultWarning : resultWarnings) {
                if (resultWarning.contains(expectedWarning)) {
                    containsWarning = true;
                    break;
                }
            }
            if (!containsWarning) {
                containsAll = false;
                break;
            }
        }
        softAssert.assertTrue(containsAll, errorMessage);
    }

    private static String getSetComparisonErrorMessage(TestFile expected, FileResult result) {
        return String.format("\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
                "---------------------------------------------------",
                validationInfo(),
                "- - - - - - - - - - - - - - - - - - - - - - - - - -",
                "- - - - - [WARNINGS COMPARISON FAILURE] - - - - - -",
                "- - - - - [All expected warnings:] - - - - - - - - ",
                joinWarnings(expected.getExpectedWarnings()),
                "- - - - - [All result warnings:] - - - - - - - - - ",
                joinWarnings(result.getWarnings()),
                "- - - - - [Result has all expected warnings]: - - -"
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
