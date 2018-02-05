package ee.testijad.mobilecpp.validation;

import java.util.List;

public class FileResult {

    private ResultType resultType;
    private List<String> warnings;

    FileResult(ResultType resultType, List<String> errors) {
        this.resultType = resultType;
        this.warnings = errors;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public List<String> getWarnings() {
        return warnings;
    }

}
