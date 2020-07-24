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
package io.github.panapeepo.database.request;

import io.github.panapeepo.api.database.query.QueryOperation;
import io.github.panapeepo.api.database.RequestBuilder;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.database.H2DatabaseDriver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SQLRequestBuilder<T> implements RequestBuilder<T> {

    protected final StringBuilder requestStringBuilder;
    protected final Table<T> table;
    protected final H2DatabaseDriver driver;
    protected final List<Object> objects = new ArrayList<>();
    protected final AtomicBoolean append = new AtomicBoolean();

    public SQLRequestBuilder(StringBuilder requestStringBuilder, Table<T> table, H2DatabaseDriver driver) {
        this.requestStringBuilder = requestStringBuilder;
        this.table = table;
        this.driver = driver;
    }

    @Override
    public @NotNull RequestBuilder<T> where(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        if (this.append.getAndSet(true)) {
            this.requestStringBuilder.append(",");
        } else {
            this.requestStringBuilder.append("WHERE ");
        }

        this.requestStringBuilder.append("`").append(field).append("` ").append(operation.getFormatted()).append(" ? ");
        this.objects.add(value);
        return this;
    }

    @Override
    public @NotNull RequestBuilder<T> and(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        if (!this.append.get()) {
            return this.where(field, operation, value);
        }

        this.requestStringBuilder.append(" AND ").append(field).append(" ").append(operation.getFormatted()).append(" ? ");
        this.objects.add(value);
        return this;
    }

    @Override
    public @NotNull RequestBuilder<T> or(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        if (!this.append.get()) {
            return this.where(field, operation, value);
        }

        this.requestStringBuilder.append(" OR ").append(field).append(" ").append(operation.getFormatted()).append(" ? ");
        this.objects.add(value);
        return this;
    }

    @Override
    public @NotNull String build() {
        this.requestStringBuilder.setLength(this.requestStringBuilder.length() - 1);
        return this.requestStringBuilder.append(";").toString();
    }
}
