package io.github.panapeepo.api.database.adb;

public enum QueryOperation {

    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<=");

    private final String operation;

    QueryOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
