package io.github.panapeepo.api.database.adb;

import io.github.panapeepo.api.database.adb.query.QueryBuilder;

public interface RequestBuilder<T> {

    QueryBuilder<T> where(String field, QueryOperation operation, Object value);

    QueryBuilder<T> and();

    QueryBuilder<T> or();

    String build();

}
