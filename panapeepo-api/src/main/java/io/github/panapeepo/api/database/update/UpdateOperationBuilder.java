package io.github.panapeepo.api.database.update;

import io.github.panapeepo.api.database.RequestBuilder;
import io.github.panapeepo.api.database.query.QueryOperation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface UpdateOperationBuilder<T> extends RequestBuilder<T> {

    @Override
    @NotNull UpdateOperationBuilder<T> where(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @Override
    @NotNull UpdateOperationBuilder<T> and(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @Override
    @NotNull UpdateOperationBuilder<T> or(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value);

    @NotNull UpdateOperationBuilder<T> set(@NotNull String field, @NotNull Object value);

    @NotNull CompletableFuture<Integer> execute();

}
