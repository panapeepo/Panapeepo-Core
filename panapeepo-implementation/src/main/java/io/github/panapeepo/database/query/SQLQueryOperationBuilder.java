package io.github.panapeepo.database.query;

import com.google.common.collect.Lists;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.buffer.ThreadSaveGrowingByteBuffer;
import io.github.panapeepo.api.database.query.QueryOperation;
import io.github.panapeepo.api.database.query.QueryOperationBuilder;
import io.github.panapeepo.api.database.query.SortOperation;
import io.github.panapeepo.database.H2DatabaseDriver;
import io.github.panapeepo.database.request.SQLRequestBuilder;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SQLQueryOperationBuilder<T> extends SQLRequestBuilder<T> implements QueryOperationBuilder<T> {

    protected final H2DatabaseDriver driver;

    private int offset = -1;
    private int limit = -1;

    public SQLQueryOperationBuilder(@NotNull H2DatabaseDriver driver, @NotNull Table<T> table) {
        super(new StringBuilder("SELECT * FROM " + table.getName() + " "), table, driver);
        this.driver = driver;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> where(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.where(field, operation, value);
        return this;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> and(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.and(field, operation, value);
        return this;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> or(@NotNull String field, @NotNull QueryOperation operation, @NotNull Object value) {
        super.or(field, operation, value);
        return this;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> sort(@NotNull String field, @NotNull SortOperation operation) {
        this.requestStringBuilder.append("ORDER BY ").append(field).append(" ").append(operation.name()).append(" ");
        return this;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public @NotNull QueryOperationBuilder<T> limit(int amount) {
        this.limit = amount;
        return this;
    }

    @Override
    public @NotNull CompletableFuture<T> findOne() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                this.limit(1).build(),
                resultSet -> {
                    if (!resultSet.next()) {
                        return null;
                    }

                    byte[] blob = resultSet.getBytes("data");
                    if (blob == null) {
                        return null;
                    }

                    return this.table.getDeserializer().deserialize(new ThreadSaveGrowingByteBuffer(Unpooled.wrappedBuffer(blob))).orElse(null);
                }, null, this.objects
        ));
    }

    @Override
    public @NotNull CompletableFuture<List<T>> findMany() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(
                this.build(),
                resultSet -> {
                    List<T> list = Lists.newArrayList();
                    while (resultSet.next()) {
                        byte[] blob = resultSet.getBytes("data");
                        if (blob == null) {
                            return null;
                        }

                        this.table.getDeserializer().deserialize(new ThreadSaveGrowingByteBuffer(Unpooled.wrappedBuffer(blob))).ifPresent(list::add);
                    }

                    return list;
                }, new ArrayList<>(), this.objects
        ));
    }

    @Override
    public @NotNull String build() {
        if (this.limit > 0) {
            this.requestStringBuilder.append("LIMIT ").append(this.limit).append(" ");
        }

        if (this.offset > 0) {
            this.requestStringBuilder.append("OFFSET ").append(this.offset).append(" ");
        }

        return super.build();
    }
}
