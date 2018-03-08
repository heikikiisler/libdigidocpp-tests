package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.TestSuiteBuilder;
import ee.testijad.mobilecpp.validation.ResultsParser;

public class GenerateSuite {

    public static void main(String[] args) {
        String className = "ee.testijad.mobilecpp.tests.ValidationTests";
        String resultsFileProperty = System.getProperty("mobilecpp.results-file");
        if (resultsFileProperty == null) {
            TestSuiteBuilder.generateTestSuite(Config.DATA_FILES_DIRECTORY, className);
        } else {
            TestSuiteBuilder.generateTestSuite(ResultsParser.getDefault().getAllFileNames(), className);
        }
    }

}
