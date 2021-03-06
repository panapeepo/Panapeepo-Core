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
package io.github.panapeepo.database.insert;

import com.google.common.base.Preconditions;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.insert.InsertOperationBuilder;
import io.github.panapeepo.api.database.query.QueryOperation;
import io.github.panapeepo.database.H2DatabaseDriver;
import io.github.panapeepo.database.request.SQLRequestBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SQLInsertOperationBuilder<T> extends SQLRequestBuilder<T> implements InsertOperationBuilder<T> {

    public SQLInsertOperationBuilder(Table<T> table, H2DatabaseDriver driver, int schemeLength) {
        super(new StringBuilder("INSERT INTO " + table.getName() + " "), table, driver);
        this.schemeLength = schemeLength;
    }

    private final int schemeLength;
    private int givenArguments;

    @Override
    public @NotNull InsertOperationBuilder<T> where(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.where(field, operation, value);
        return this;
    }

    @Override
    public @NotNull InsertOperationBuilder<T> and(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.and(field, operation, value);
        return this;
    }

    @Override
    public @NotNull InsertOperationBuilder<T> or(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.or(field, operation, value);
        return this;
    }

    @Override
    public int getRequiredArgumentLength() {
        return this.schemeLength;
    }

    @Override
    public @NotNull InsertOperationBuilder<T> set(@NotNull String field, @NotNull Object value) {
        Preconditions.checkArgument(this.schemeLength < this.givenArguments++);
        if (this.givenArguments == 1) {
            this.requestStringBuilder.append("(").append(field);
        } else {
            this.requestStringBuilder.append(field);
        }

        if (this.schemeLength == this.givenArguments) {
            this.requestStringBuilder.append(") VALUES (");
            this.requestStringBuilder.append("?, ".repeat(Math.max(0, this.givenArguments - 1))).append(") ");
        } else {
            this.requestStringBuilder.append(", ");
        }

        return this;
    }

    @Override
    public @NotNull CompletableFuture<Integer> execute() {
        Preconditions.checkArgument(this.schemeLength == this.givenArguments);
        return CompletableFuture.supplyAsync(() -> this.driver.executeUpdate(this.build(), this.objects));
    }
}
