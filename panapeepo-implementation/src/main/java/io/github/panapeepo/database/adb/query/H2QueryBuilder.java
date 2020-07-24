package io.github.panapeepo.database.adb.query;

import io.github.panapeepo.api.database.adb.QueryOperation;
import io.github.panapeepo.api.database.adb.query.QueryBuilder;
import io.github.panapeepo.api.database.adb.query.SortOperation;
import io.github.panapeepo.database.H2DatabaseDriver;
import io.github.panapeepo.database.util.SQLFunction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class H2QueryBuilder<T> implements QueryBuilder<T> {

    private final H2DatabaseDriver driver;
    private final StringBuilder queryStringBuilder;
    private int offset = -1;
    private int limit = -1;
    private boolean append = false;
    private final List<Object> objects = new ArrayList<>();

    public H2QueryBuilder(H2DatabaseDriver driver, String table) {
        this.queryStringBuilder = new StringBuilder("SELECT * FROM " + table);
        this.driver = driver;
    }

    @Override
    public QueryBuilder<T> sort(String field, SortOperation operation) {
        return null;
    }

    @Override
    public QueryBuilder<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public QueryBuilder<T> limit(int amount) {
        this.limit = amount;
        return this;
    }

    @Override
    public CompletableFuture<T> find() {
        return null;
    }

    @Override
    public CompletableFuture<List<T>> findMany() {
        return CompletableFuture.supplyAsync(() -> this.driver.executeQuery(this.build(),
                (SQLFunction<ResultSet, List<T>>) resultSet -> {
                    var list = new ArrayList<T>();
                    while (resultSet.next()) {
                        // TODO: ResultSet to Object
                    }
                    return list;
                }, null, this.objects.toArray())
        );
    }

    @Override
    public QueryBuilder<T> where(String field, QueryOperation operation, Object value) {
        if (!this.append) {
            this.append = true;
            this.queryStringBuilder.append(" WHERE");
        } else {
            this.queryStringBuilder.append(",");
        }
        this.queryStringBuilder.append(" ")
                .append(field)
                .append(" ")
                .append(operation.getOperation())
                .append(" ?");
        this.objects.add(value);
        return this;
    }

    @Override
    public QueryBuilder<T> and() {
        this.queryStringBuilder.append(" AND");
        return this;
    }

    @Override
    public QueryBuilder<T> or() {
        this.queryStringBuilder.append(" OR");
        return this;
    }

    @Override
    public String build() {
        if (this.limit != -1) {
            this.queryStringBuilder.append(" LIMIT").append(this.limit);
        }
        if (this.offset != -1) {
            this.queryStringBuilder.append(" OFFSET").append(this.offset);
        }
        this.queryStringBuilder.append(";");

        return this.queryStringBuilder.toString();
    }
}
