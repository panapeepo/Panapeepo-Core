package com.github.panapeepo.shared.config;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public interface Configurable<V extends Configurable<V>> {

    V append(String key, Object value);

    V append(String key, String value);

    V append(String key, Character value);

    V append(String key, Boolean value);

    V append(String key, Number value);

    default V appendIfNotExists(String key, Object value) {
        if (!this.contains(key)) {
            this.append(key, value);
        }
        return this.getAsSpecificConfigurable();
    }

    default V appendIfNotExists(String key, String value) {
        if (!this.contains(key)) {
            this.append(key, value);
        }
        return this.getAsSpecificConfigurable();
    }

    default V appendIfNotExists(String key, Character value) {
        if (!this.contains(key)) {
            this.append(key, value);
        }
        return this.getAsSpecificConfigurable();
    }

    default V appendIfNotExists(String key, Boolean value) {
        if (!this.contains(key)) {
            this.append(key, value);
        }
        return this.getAsSpecificConfigurable();
    }

    default V appendIfNotExists(String key, Number value) {
        if (!this.contains(key)) {
            this.append(key, value);
        }
        return this.getAsSpecificConfigurable();
    }

    boolean contains(String key);

    String getString(String key);

    boolean getBoolean(String key);

    char getCharacter(String key);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    double getDouble(String key);

    BigInteger getBigInteger(String key);

    BigDecimal getBigDecimal(String key);

    <T> T getObject(String key, Class<T> tClass);

    <T> T getObject(String key, Type type);

    void clear();

    void saveAsFile(Path path);

    default void saveAsFile(String path) {
        this.saveAsFile(Paths.get(path));
    }

    byte[] toBytes();

    Set<String> keys();

    V getAsSpecificConfigurable();

}
