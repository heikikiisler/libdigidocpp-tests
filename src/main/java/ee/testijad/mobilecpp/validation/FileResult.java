package ee.testijad.mobilecpp.validation;

import java.util.Set;

public class FileResult {

    private ResultType resultType;
    private Set<String> warnings;

    FileResult(ResultType resultType, Set<String> errors) {
        this.resultType = resultType;
        this.warnings = errors;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

}
