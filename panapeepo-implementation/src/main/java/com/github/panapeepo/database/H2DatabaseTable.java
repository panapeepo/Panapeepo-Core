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
package com.github.panapeepo.database;

import com.github.panapeepo.api.database.DatabaseTable;
import com.github.panapeepo.api.database.buffer.ThreadSaveGrowingByteBuffer;
import com.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import com.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.netty.buffer.Unpooled;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class H2DatabaseTable<T> implements DatabaseTable<T> {

    H2DatabaseTable(H2DatabaseDriver driver, String name, ObjectToByteSerializer<T> serializer, ByteToObjectDeserializer<T> deserializer) {
        this.driver = driver;
        this.name = name;
        this.serializer = serializer;
        this.deserializer = deserializer;

        this.driver.executeUpdate("CREATE TABLE IF NOT EXISTS `" + name + "` (`key` TEXT, `data` LONGBLOB);");
    }

    private final H2DatabaseDriver driver;
    private final String name;
    private final ObjectToByteSerializer<T> serializer;
    private final ByteToObjectDeserializer<T> deserializer;

    private final Executor pool = Executors.newCachedThreadPool();

    @Override
    public CompletableFuture<Void> insertObject(String key, T t) {
        return this.contains(key).thenAcceptAsync(result -> {
            byte[] blob = this.serializer.serialize(t);
            if (result) {
                this.driver.executeUpdate("UPDATE `" + this.name + "` SET `data` = ? WHERE `key` = ?", blob, key);
            } else {
                this.driver.executeUpdate("INSERT INTO `" + this.name + "` (`key`, `data`) VALUES (?, ?);", key, blob);
            }
        }, this.pool);
    }

    @Override
    public CompletableFuture<Void> removeObject(String key) {
        return CompletableFuture.runAsync(() -> this.driver.executeUpdate("DELETE FROM `" + this.name + "` WHERE `key` = ?", key));
    }

    @Override
    public CompletableFuture<Boolean> contains(String key) {
        return CompletableFuture.supplyAsync(
                () -> this.driver.executeQuery("SELECT `key` FROM " + this.name + " WHERE `key` = ?", ResultSet::next, false),
                this.pool
        );
    }

    @Override
    public CompletableFuture<T> getObject(String key) {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                "SELECT `data` FROM `" + this.name + "` WHERE `key` = ?",
                resultSet -> {
                    if (!resultSet.next()) {
                        return null;
                    }

                    byte[] blob = resultSet.getBytes("data");
                    if (blob == null) {
                        return null;
                    }

                    return this.deserializer.deserialize(new ThreadSaveGrowingByteBuffer(Unpooled.wrappedBuffer(blob))).orElse(null);
                }, null, key
        ), this.pool);
    }

    @Override
    public CompletableFuture<Collection<T>> getAllEntries() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                "SELECT `data` FROM " + this.name,
                resultSet -> {
                    Collection<T> result = new ArrayList<>();
                    while (resultSet.next()) {
                        byte[] bytes = resultSet.getBytes("data");
                        if (bytes == null) {
                            continue;
                        }

                        this.deserializer.deserialize(new ThreadSaveGrowingByteBuffer(Unpooled.wrappedBuffer(bytes))).ifPresent(result::add);
                    }

                    return result;
                }, new ArrayList<>()
        ), this.pool);
    }

    @Override
    public CompletableFuture<Long> getSize() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                "SELECT COUNT(*) FROM " + this.name,
                resultSet -> {
                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    }

                    return -1L;
                }, -1L
        ), this.pool);
    }

    @Override
    public CompletableFuture<Void> clear() {
        return CompletableFuture.runAsync(() -> this.driver.executeUpdate("TRUNCATE TABLE " + this.name));
    }

    @Override
    public CompletableFuture<Void> delete() {
        return CompletableFuture.runAsync(() -> this.driver.executeUpdate("DROP TABLE " + this.name));
    }
}
