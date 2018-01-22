package ee.testijad.mobilecpp.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.testijad.mobilecpp.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsParser {

    private List<Map<String, String>> results;

    public ResultsParser(String resultsFilePath) {
        String contents = Utils.readFileIntoString(resultsFilePath);
        ObjectMapper mapper = new ObjectMapper();
        try {
            results = mapper.readValue(contents, new TypeReference<List<Map<String, String>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean expectedMatchesDetected(TestFile testFile) {
        List<ResultType> result = getResultByTestFile(testFile);
        return testFile.getExpectedWarnings().containsAll(result) &&
               result.containsAll(testFile.getExpectedWarnings());
    }

    private List<ResultType> getResultByTestFile(TestFile testFile) {
        List<ResultType> resultTypes = new ArrayList<>();
        for (Map<String, String> result : results) {
            if (result.get("f").equals(testFile.getFileName())) {
                resultTypes.add(ResultType.get(result.get("s")));
                return resultTypes;
            }
        }
        throw new RuntimeException("Could not find test file " + testFile.getFileName());
    }

}
