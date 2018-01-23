package ee.testijad.mobilecpp.validation;

public enum ResultType {

    OK, NOT;

    public static ResultType get(String resultType) {
        switch (resultType.toUpperCase()) {
            case "OK":
                return OK;
            case "NOT":
                return NOT;
            default:
                throw new IllegalArgumentException("Can not resolve ResultType " + resultType);
        }
    }

}
