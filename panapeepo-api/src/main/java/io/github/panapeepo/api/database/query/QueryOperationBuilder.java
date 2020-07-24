package io.github.panapeepo.api.database.query;

import io.github.panapeepo.api.database.RequestBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface QueryOperationBuilder<T> extends RequestBuilder<T> {

    @NotNull QueryOperationBuilder<T> sort(@NotNull String field, @NotNull SortOperation operation);

    @NotNull QueryOperationBuilder<T> offset(int offset);

    @NotNull QueryOperationBuilder<T> limit(int amount);

    @NotNull CompletableFuture<T> findOne();

    @NotNull CompletableFuture<List<T>> findMany();
}
