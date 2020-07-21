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
import io.github.panapeepo.api.database.DatabaseTable;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.github.panapeepo.database.util.SQLFunction;
import org.h2.Driver;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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
    public Collection<String> getTableNames() {
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
    public <T> DatabaseTable<T> getTable(String name, ObjectToByteSerializer<T> serializer, ByteToObjectDeserializer<T> deserializer) {
        return new H2DatabaseTable<>(this, name, serializer, deserializer);
    }

    public void executeUpdate(String query, Object... objects) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            int i = 1;
            for (Object object : objects) {
                if (object instanceof byte[]) {
                    preparedStatement.setBytes(i++, (byte[]) object);
                } else {
                    preparedStatement.setString(i++, object.toString());
                }
            }

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Nullable
    public <T> T executeQuery(String query, SQLFunction<ResultSet, T> function, T defaultValue, Object... objects) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(query)) {
            int i = 1;
            for (Object object : objects) {
                if (object instanceof byte[]) {
                    preparedStatement.setBytes(i++, (byte[]) object);
                } else {
                    preparedStatement.setString(i++, object.toString());
                }
            }

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
}
