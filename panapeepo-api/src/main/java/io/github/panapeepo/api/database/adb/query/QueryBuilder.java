package io.github.panapeepo.api.database.adb.query;

import io.github.panapeepo.api.database.adb.RequestBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface QueryBuilder<T> extends RequestBuilder<T> {

    QueryBuilder<T> sort(String field, SortOperation operation);

    QueryBuilder<T> offset(int offset);

    QueryBuilder<T> limit(int amount);

    CompletableFuture<T> find();

    CompletableFuture<List<T>> findMany();

}
