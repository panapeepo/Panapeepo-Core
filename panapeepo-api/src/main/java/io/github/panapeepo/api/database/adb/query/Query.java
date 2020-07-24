package io.github.panapeepo.api.database.adb.query;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Query<T> {

    CompletableFuture<T> find();

    CompletableFuture<List<T>> findMany();

}
