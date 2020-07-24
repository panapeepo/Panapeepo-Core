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
package io.github.panapeepo.database.update;

import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.update.UpdateOperation;
import io.github.panapeepo.api.database.update.UpdateOperationBuilder;
import io.github.panapeepo.database.H2DatabaseDriver;
import io.github.panapeepo.database.request.SQLRequestBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SQLUpdateOperationBuilder<T> extends SQLRequestBuilder<T> implements UpdateOperationBuilder<T> {

    public SQLUpdateOperationBuilder(@NotNull Table<T> table, @NotNull H2DatabaseDriver databaseDriver, @NotNull UpdateOperation operation) {
        super(new StringBuilder(operation.name() + " FROM " + table.getName() + " "), table, databaseDriver);
    }

    @Override
    public @NotNull UpdateOperationBuilder<T> set(@NotNull String field, @NotNull Object value) {
        this.requestStringBuilder.append("SET ").append(field).append(" = ? ");
        this.objects.add(value);
        return this;
    }

    @Override
    public @NotNull CompletableFuture<Integer> execute() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeUpdate(this.build(), this.objects));
    }
}
