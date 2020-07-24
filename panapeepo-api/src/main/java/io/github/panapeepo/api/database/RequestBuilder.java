package io.github.panapeepo.api.database;

import io.github.panapeepo.api.database.query.QueryOperation;
import org.jetbrains.annotations.NotNull;

public interface RequestBuilder<T> {

    @NotNull RequestBuilder<T> where(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @NotNull RequestBuilder<T> and(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @NotNull RequestBuilder<T> or(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @NotNull String build();

}
