package com.github.panapeepo.shared.config;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertiesConfigurable implements Configurable<PropertiesConfigurable> {
    private Properties properties;
    private String comments;

    public PropertiesConfigurable() {
        this(new Properties());
    }

    public PropertiesConfigurable(Properties properties) {
        this.properties = properties;
    }

    public String comments() {
        return this.comments;
    }

    public PropertiesConfigurable comments(String comments) {
        this.comments = comments;
        return this;
    }

    @Override
    public PropertiesConfigurable append(String key, Object value) {
        return this.append(key, String.valueOf(value));
    }

    @Override
    public PropertiesConfigurable append(String key, String value) {
        this.properties.setProperty(key, value);
        return this;
    }

    @Override
    public PropertiesConfigurable append(String key, Character value) {
        return this.append(key, String.valueOf(value));
    }

    @Override
    public PropertiesConfigurable append(String key, Boolean value) {
        return this.append(key, String.valueOf(value));
    }

    @Override
    public PropertiesConfigurable append(String key, Number value) {
        return this.append(key, String.valueOf(value));
    }

    @Override
    public boolean contains(String key) {
        return this.properties.containsKey(key);
    }

    @Override
    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.properties.getProperty(key));
    }

    @Override
    public char getCharacter(String key) {
        String s = this.properties.getProperty(key);
        if (s == null || s.isEmpty())
            return (char) -1;
        return s.charAt(0);
    }

    @Override
    public byte getByte(String key) {
        try {
            return Byte.parseByte(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public short getShort(String key) {
        try {
            return Short.parseShort(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public int getInt(String key) {
        try {
            return Integer.parseInt(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public long getLong(String key) {
        try {
            return Long.parseLong(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public double getDouble(String key) {
        try {
            return Double.parseDouble(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public BigInteger getBigInteger(String key) {
        try {
            return new BigInteger(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        try {
            return new BigDecimal(this.properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> tClass) {
        throw new UnsupportedOperationException("not supported in PropertiesConfigurable");
    }

    @Override
    public <T> T getObject(String key, Type type) {
        throw new UnsupportedOperationException("not supported in PropertiesConfigurable");
    }

    @Override
    public void clear() {
        this.properties.clear();
    }

    @Override
    public void saveAsFile(Path path) {
        try (OutputStream outputStream = Files.newOutputStream(path);
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            this.properties.store(writer, this.comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties asPropertiesObject() {
        return this.properties;
    }

    @Override
    public byte[] toBytes() {
        return this.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Set<String> keys() {
        return this.properties.keySet().stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public PropertiesConfigurable getAsSpecificConfigurable() {
        return this;
    }

    @Override
    public String toString() {
        try (StringWriter writer = new StringWriter()) {
            this.properties.store(writer, this.comments);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PropertiesConfigurable load(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PropertiesConfigurable(properties);
    }

    public static PropertiesConfigurable load(InputStream inputStream) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PropertiesConfigurable();
    }

    public static PropertiesConfigurable load(Path path) {
        if (!Files.exists(path))
            return new PropertiesConfigurable();
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PropertiesConfigurable();
    }

    public static PropertiesConfigurable load(String path) {
        return load(Paths.get(path));
    }
}
