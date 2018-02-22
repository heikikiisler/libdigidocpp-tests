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

    private static final String SEPARATOR_LINE = "--------------------------------------------------";
    private static final String EMPTY_SIGNATURE_FILES_ERROR =
            String.format("\n%s\nExpected at least one signature file in container, found none.\n", SEPARATOR_LINE);

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
        softAssert.assertTrue(result.getSignatureFiles().size() > 0, EMPTY_SIGNATURE_FILES_ERROR);
        softAssert.assertEquals(
                result.getResultType(),
                expected.getExpectedResultType(),
                String.format("\n%s\n[Result comparison (OK or NOT)]:", SEPARATOR_LINE)
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
        return String.join("\n",
                "",
                SEPARATOR_LINE,
                "- - - - - [WARNINGS COMPARISON FAILURE] - - - - - -",
                "",
                "- - - - - [All expected warnings:] - - - - - - - - ",
                joinWarnings(expected.getExpectedWarnings()),
                "- - - - - [All result warnings:] - - - - - - - - - ",
                joinWarnings(result.getWarnings()),
                "- - - - - [Result has all expected warnings]: - - -",
                ""
        );
    }

    private static String getValidationInfo() {
        return String.format("libdigidocpp version: %s ; result file timestamp: %s",
                resultsParser.getVersionInfo(),
                resultsParser.getTimestamp()
        );
    }

    private static String joinWarnings(Set<String> warningsSet) {
        if (warningsSet.size() > 0) {
            return String.join("\n", warningsSet) + "\n";
        } else {
            return "";
        }
    }
}
