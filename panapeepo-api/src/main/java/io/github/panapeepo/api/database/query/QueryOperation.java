package io.github.panapeepo.api.database.query;

import org.jetbrains.annotations.NotNull;

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

    public @NotNull String getFormatted() {
        return operation;
    }
}
