package ee.testijad.mobilecpp.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileResult {

    private ResultType resultType;
    private Set<String> warnings;
    private List<Map<String, Object>> signatureFiles;

    public FileResult(ResultType resultType, Set<String> warnings, List<Map<String, Object>> signatureFiles) {
        this.resultType = resultType;
        this.warnings = warnings;
        this.signatureFiles = signatureFiles;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public List<Map<String, Object>> getSignatureFiles() {
        return signatureFiles;
    }
}
