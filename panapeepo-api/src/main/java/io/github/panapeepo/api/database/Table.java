package io.github.panapeepo.api.database;

import io.github.panapeepo.api.database.insert.InsertOperationBuilder;
import io.github.panapeepo.api.database.query.QueryOperationBuilder;
import io.github.panapeepo.api.database.update.UpdateOperation;
import io.github.panapeepo.api.database.update.UpdateOperationBuilder;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Table<T> {

    @NotNull ObjectToByteSerializer<T> getSerializer();

    @NotNull ByteToObjectDeserializer<T> getDeserializer();

    @NotNull InsertOperationBuilder<T> insert();

    @NotNull QueryOperationBuilder<T> select();

    @NotNull UpdateOperationBuilder<T> update(@NotNull UpdateOperation operation);

    @NotNull CompletableFuture<Long> getSize();

    @NotNull CompletableFuture<Void> clear();

    @NotNull CompletableFuture<Void> delete();

    @NotNull String getName();
}
