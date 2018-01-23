package ee.testijad.mobilecpp.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.testijad.mobilecpp.util.Utils;

import java.io.IOException;
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

    private ResultType getResultByTestFile(TestFile testFile) {
        for (Map<String, String> result : results) {
            if (result.get("f").equals(testFile.getFileName())) {
                return ResultType.get(result.get("s"));
            }
        }
        throw new RuntimeException("Could not find test file " + testFile.getFileName());
    }

}
