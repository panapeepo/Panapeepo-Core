package io.github.panapeepo.api.database.update;

import io.github.panapeepo.api.database.RequestBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface UpdateOperationBuilder<T> extends RequestBuilder<T> {

    @NotNull UpdateOperationBuilder<T> set(@NotNull String field, @NotNull Object value);

    @NotNull CompletableFuture<Integer> execute();

}
