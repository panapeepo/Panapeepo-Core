/*
 * MIT License
 *
 * Copyright (c) 2020 Panapeepo (https://github.com/Panapeepo)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.panapeepo.database;

import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.insert.InsertOperationBuilder;
import io.github.panapeepo.api.database.query.QueryOperationBuilder;
import io.github.panapeepo.api.database.update.UpdateOperation;
import io.github.panapeepo.api.database.update.UpdateOperationBuilder;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.github.panapeepo.database.insert.SQLInsertOperationBuilder;
import io.github.panapeepo.database.query.SQLQueryOperationBuilder;
import io.github.panapeepo.database.update.SQLUpdateOperationBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class H2DatabaseTable<T> implements Table<T> {

    H2DatabaseTable(H2DatabaseDriver driver, String name, String scheme, ObjectToByteSerializer<T> serializer, ByteToObjectDeserializer<T> deserializer) {
        this.driver = driver;
        this.name = name;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.schemeLength = scheme.contains(",") ? scheme.split(",").length : 1;

        this.driver.executeUpdate("CREATE TABLE IF NOT EXISTS `" + name + "` " + scheme + ";");
    }

    private final H2DatabaseDriver driver;
    private final String name;
    private final int schemeLength;
    private final ObjectToByteSerializer<T> serializer;
    private final ByteToObjectDeserializer<T> deserializer;

    @Override
    public @NotNull ObjectToByteSerializer<T> getSerializer() {
        return this.serializer;
    }

    @Override
    public @NotNull ByteToObjectDeserializer<T> getDeserializer() {
        return this.deserializer;
    }

    @Override
    public @NotNull InsertOperationBuilder<T> insert() {
        return new SQLInsertOperationBuilder<>(this, this.driver, this.schemeLength);
    }

    @Override
    public @NotNull QueryOperationBuilder<T> select() {
        return new SQLQueryOperationBuilder<>(this.driver, this);
    }

    @Override
    public @NotNull UpdateOperationBuilder<T> update(@NotNull UpdateOperation operation) {
        return new SQLUpdateOperationBuilder<>(this, this.driver, operation);
    }

    @Override
    public @NotNull CompletableFuture<Long> getSize() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                "SELECT COUNT(*) FROM " + this.name,
                resultSet -> {
                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    }

                    return -1L;
                }, -1L
        ));
    }

    @Override
    public @NotNull CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() -> this.driver.executeUpdate("TRUNCATE TABLE " + this.name));
    }

    @Override
    public @NotNull CompletableFuture<Void> delete() {
        return CompletableFuture.runAsync(() -> this.driver.executeUpdate("DROP TABLE " + this.name));
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }
}
