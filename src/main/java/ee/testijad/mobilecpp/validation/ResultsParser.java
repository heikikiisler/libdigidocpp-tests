package ee.testijad.mobilecpp.validation;

import ee.testijad.mobilecpp.util.Utils;

import java.util.Map;

public class ResultsParser {

    private Map results;

    public ResultsParser(String resultsFilePath) {
        String contents = Utils.readFileIntoString(resultsFilePath);
        // TODO: 19.01.2018 Finish reading JSON file
    }

    public ResultType getResultForFile(TestFile testFile) {
        // TODO: 19.01.2018 Finish getting for results map
        return null;
    }

}
