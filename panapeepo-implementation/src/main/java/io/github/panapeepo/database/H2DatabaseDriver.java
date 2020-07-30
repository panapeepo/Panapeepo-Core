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

import io.github.panapeepo.api.database.DatabaseDriver;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.github.panapeepo.database.util.SQLFunction;
import org.h2.Driver;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class H2DatabaseDriver implements DatabaseDriver {

    private static final String END_POINT = new File("database").getAbsolutePath();
    private Connection connection;

    @Override
    public void connect() {
        try {
            Driver.load();
            this.connection = DriverManager.getConnection("jdbc:h2:" + END_POINT + "/h2;DB_CLOSE_ON_EXIT=FALSE");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public @NotNull Collection<String> getTableNames() {
        return this.executeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='PUBLIC'",
                resultSet -> {
                    Collection<String> collection = new ArrayList<>();
                    while (resultSet.next()) {
                        collection.add(resultSet.getString("table_name"));
                    }

                    return collection;
                }, new ArrayList<>()
        );
    }

    @Override
    public @NotNull <T> Table<T> getTable(@NotNull String name, @NotNull String scheme, @NotNull ObjectToByteSerializer<T> serializer, @NotNull ByteToObjectDeserializer<T> deserializer) {
        return new H2DatabaseTable<>(this, name, scheme, serializer, deserializer);
    }

    public int executeUpdate(String query, Object... objects) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            this.applyParameters(preparedStatement, objects);
            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    public <T> T executeQuery(String query, SQLFunction<ResultSet, T> function, T defaultValue, Object... objects) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            this.applyParameters(preparedStatement, objects);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return function.apply(resultSet);
            } catch (Throwable throwable) {
                return defaultValue;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return defaultValue;
    }

    private void applyParameters(@NotNull PreparedStatement preparedStatement, @NonNls Object... objects) throws SQLException {
        int i = 1;
        for (Object object : objects) {
            if (object instanceof byte[]) {
                preparedStatement.setBytes(i++, (byte[]) object);
            } else if (object instanceof Short) { // short before int
                preparedStatement.setShort(i++, (short) object);
            } else if (object instanceof Integer) { // int before long
                preparedStatement.setInt(i++, (int) object);
            } else if (object instanceof Long) {
                preparedStatement.setLong(i++, (long) object);
            } else if (object instanceof Double) { // double before float
                preparedStatement.setDouble(i++, (double) object);
            } else if (object instanceof Float) {
                preparedStatement.setFloat(i++, (float) object);
            } else if (object instanceof Byte) {
                preparedStatement.setByte(i++, (byte) object);
            } else if (object instanceof Boolean) {
                preparedStatement.setBoolean(i++, (boolean) object);
            } else if (object instanceof BigDecimal) {
                preparedStatement.setBigDecimal(i++, (BigDecimal) object);
            } else if (object instanceof Date) {
                preparedStatement.setDate(i++, (Date) object);
            } else if (object instanceof Timestamp) {
                preparedStatement.setTimestamp(i++, (Timestamp) object);
            } else if (object instanceof Time) {
                preparedStatement.setTime(i++, (Time) object);
            } else if (object instanceof String) {
                preparedStatement.setString(i++, (String) object);
            } else {
                throw new IllegalStateException("Unable to set object " + object.getClass().getName());
            }
        }
    }
}
