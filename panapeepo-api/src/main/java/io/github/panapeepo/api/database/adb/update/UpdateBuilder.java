package io.github.panapeepo.api.database.adb.update;

import io.github.panapeepo.api.database.adb.RequestBuilder;

public interface UpdateBuilder<T> extends RequestBuilder<T> {

    UpdateBuilder<T> set(String field, Object value);

}
