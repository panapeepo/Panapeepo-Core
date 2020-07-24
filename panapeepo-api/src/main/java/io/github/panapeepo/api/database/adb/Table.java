package io.github.panapeepo.api.database.adb;

import io.github.panapeepo.api.database.adb.query.Query;
import io.github.panapeepo.api.database.adb.query.QueryBuilder;
import io.github.panapeepo.api.database.adb.update.UpdateBuilder;

import java.util.concurrent.CompletableFuture;

public interface Table<T> {

    CompletableFuture<Void> create(T entity);

    String getName();

    Query<T> execute(QueryBuilder<T> queryBuilder);

    CompletableFuture<Long> update(UpdateBuilder<T> updateBuilder);

    CompletableFuture<Long> getSize();

    CompletableFuture<Void> clear();

    CompletableFuture<Void> delete();

}
