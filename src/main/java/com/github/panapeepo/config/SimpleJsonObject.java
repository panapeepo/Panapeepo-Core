package com.github.panapeepo.config;

import com.github.panapeepo.Validate;
import com.github.panapeepo.misc.FileUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SimpleJsonObject implements Configurable<SimpleJsonObject>, JsonSerializer<SimpleJsonObject>, JsonDeserializer<SimpleJsonObject> {
    public static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(
            () -> new GsonBuilder().registerTypeAdapter(
                    SimpleJsonObject.class, new SimpleJsonObject()
            ).setPrettyPrinting().serializeNulls().create()
    );
    private static final char SEPARATOR = '.';

    public static final Configurable<SimpleJsonObject> EMPTY = UnmodifiableConfigurable.create(new SimpleJsonObject());

    private JsonObject jsonObject;

    public SimpleJsonObject() {
        this(new JsonObject());
    }

    public SimpleJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public SimpleJsonObject(String input) {
        JsonElement jsonElement = null;
        try {
            jsonElement = JsonParser.parseString(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonElement != null) {
            Validate.isTrue(jsonElement.isJsonObject(), "JsonInput must be a json object, not " + jsonElement.getClass().getSimpleName());
            this.jsonObject = jsonElement.getAsJsonObject();
        }
    }

    public SimpleJsonObject(Reader reader) {
        JsonElement jsonElement = null;
        try {
            jsonElement = JsonParser.parseReader(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonElement != null) {
            Validate.isTrue(jsonElement.isJsonObject() || jsonElement.isJsonNull(), "JsonInput must be a json object or null, not " + jsonElement.getClass().getSimpleName());
            this.jsonObject = jsonElement.getAsJsonObject();
        }
    }

    public SimpleJsonObject(Object object) {
        JsonElement jsonElement = GSON.get().toJsonTree(object);
        Validate.isTrue(jsonElement.isJsonObject(), "JsonInput must be a json object, not " + jsonElement.getClass().getSimpleName());
        this.jsonObject = jsonElement.getAsJsonObject();
    }

    public SimpleJsonObject append(String key, Object value) {
        return this.append(key, GSON.get().toJsonTree(value));
    }

    @Override
    public SimpleJsonObject append(String key, String value) {
        return this.append(key, (Object) value);
    }

    @Override
    public SimpleJsonObject append(String key, Character value) {
        return this.append(key, (Object) value);
    }

    @Override
    public SimpleJsonObject append(String key, Boolean value) {
        return this.append(key, (Object) value);
    }

    @Override
    public SimpleJsonObject append(String key, Number value) {
        return this.append(key, (Object) value);
    }

    public SimpleJsonObject append(String key, SimpleJsonObject value) {
        return this.append(key, value != null ? value.jsonObject : null);
    }

    public SimpleJsonObject append(SimpleJsonObject value) {
        return this.append(value != null ? value.jsonObject : null);
    }

    public SimpleJsonObject append(JsonObject jsonObject) {
        if (jsonObject != null) {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                this.append(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public SimpleJsonObject append(String key, JsonElement value) {
        if (this.jsonObject == null)
            this.jsonObject = new JsonObject();

        int separator = key.indexOf(SEPARATOR);

        if (separator != -1) {
            String firstKey = key.substring(0, separator);
            String subKey = key.substring(separator + 1);
            if (!subKey.isEmpty() && !firstKey.isEmpty()) {
                SimpleJsonObject jsonObject = this.getJsonObject(firstKey);
                if (jsonObject == null) {
                    jsonObject = new SimpleJsonObject();
                    this.append(firstKey, jsonObject);
                }
                jsonObject.append(subKey, value);
                return this;
            }
        }

        this.jsonObject.add(key, value);

        return this;
    }

    public SimpleJsonObject appendNull(String key) {
        return this.append(key, JsonNull.INSTANCE);
    }

    public boolean contains(String key) {
        return this.get(key) != null;
    }

    public JsonElement get(String key) {
        if (this.jsonObject == null)
            return null;

        int separator = key.indexOf(SEPARATOR);

        if (separator != -1) {
            String firstKey = key.substring(0, separator);
            String subKey = key.substring(separator + 1);
            if (!subKey.isEmpty() && !firstKey.isEmpty()) {
                SimpleJsonObject jsonObject = this.getJsonObject(firstKey);
                if (jsonObject == null)
                    return null;
                return jsonObject.get(subKey);
            }
        }

        JsonElement jsonElement = this.jsonObject.get(key);
        if (jsonElement instanceof JsonNull)
            return null;
        return jsonElement;
    }

    public String getString(String key) {
        return contains(key) ? get(key).getAsString() : null;
    }

    public boolean getBoolean(String key) {
        return contains(key) && get(key).getAsBoolean();
    }

    public char getCharacter(String key) {
        return contains(key) ? get(key).getAsCharacter() : (char) 0;
    }

    public byte getByte(String key) {
        return contains(key) ? get(key).getAsByte() : -1;
    }

    public short getShort(String key) {
        return contains(key) ? get(key).getAsShort() : -1;
    }

    public int getInt(String key) {
        return contains(key) ? get(key).getAsInt() : -1;
    }

    public long getLong(String key) {
        return contains(key) ? get(key).getAsLong() : -1;
    }

    @Override
    public double getDouble(String key) {
        return contains(key) ? get(key).getAsDouble() : -1;
    }

    public BigInteger getBigInteger(String key) {
        return contains(key) ? get(key).getAsBigInteger() : null;
    }

    public BigDecimal getBigDecimal(String key) {
        return contains(key) ? get(key).getAsBigDecimal() : null;
    }

    public <T> T getObject(String key, Class<T> tClass) {
        return this.get(key) == null ? null : GSON.get().fromJson(this.get(key), tClass);
    }

    public <T> T getObject(String key, Type type) {
        return this.get(key) == null ? null : GSON.get().fromJson(this.get(key), type);
    }

    public <T> T getObject(String key, TypeToken<T> typeToken) {
        return this.get(key) == null ? null : GSON.get().fromJson(this.get(key), typeToken.getType());
    }

    @Override
    public void clear() {
        this.jsonObject = new JsonObject();
    }

    public SimpleJsonObject getJsonObject(String key) {
        if (this.jsonObject == null)
            return null;
        JsonElement jsonElement = this.jsonObject.get(key);
        return jsonElement != null && jsonElement.isJsonObject() ? new SimpleJsonObject(jsonElement.getAsJsonObject()) : null;
    }

    public JsonObject asJsonObject() {
        return this.jsonObject;
    }

    public <T> T asObject(Class<T> clazz) {
        return GSON.get().fromJson(this.jsonObject, clazz);
    }

    public <T> T asObject(Type type) {
        return GSON.get().fromJson(this.jsonObject, type);
    }

    public <T> T asObject(TypeToken<T> typeToken) {
        return GSON.get().fromJson(this.jsonObject, typeToken.getType());
    }

    public void saveAsFile(Path path) {
        FileUtils.createFile(path);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {
            GSON.get().toJson(this.jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SimpleJsonObject load(String path) {
        return load(path, null);
    }

    public static SimpleJsonObject load(Path path) {
        return load(path, null);
    }

    public static SimpleJsonObject load(String path, SimpleJsonObject def) {
        return load(Paths.get(path), def);
    }

    public static SimpleJsonObject load(Path path, SimpleJsonObject def) {
        if (!Files.exists(path)) {
            if (def != null)
                def.saveAsFile(path);
            return def == null ? new SimpleJsonObject((JsonObject) null) : def;
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleJsonObject((JsonObject) null);
    }

    public static SimpleJsonObject load(InputStream inputStream) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            return new SimpleJsonObject(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleJsonObject((JsonObject) null);
    }

    public String toJson() {
        return this.jsonObject != null ? this.jsonObject.toString() : "null";
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public String toPrettyJson() {
        return GSON.get().toJson(this.jsonObject);
    }

    public byte[] toBytes() {
        return toJson().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Set<String> keys() {
        return this.jsonObject == null ? Collections.emptySet() : this.jsonObject.keySet();
    }

    @Override
    public SimpleJsonObject getAsSpecificConfigurable() {
        return this;
    }

    public byte[] toPrettyBytes() {
        return toPrettyJson().getBytes(StandardCharsets.UTF_8);
    }

    public SimpleJsonObject replaceAll(Map<String, String> replacements) {
        if (this.jsonObject == null)
            return this;
        String json = this.jsonObject.toString();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            json = json.replace(entry.getKey(), entry.getValue());
        }
        this.jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return this;
    }

    @Override
    public SimpleJsonObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonElement.isJsonObject() ? new SimpleJsonObject(jsonElement.getAsJsonObject()) : null;
    }

    @Override
    public JsonElement serialize(SimpleJsonObject simpleJsonObject, Type type, JsonSerializationContext jsonSerializationContext) {
        return simpleJsonObject.jsonObject;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SimpleJsonObject ? Objects.equals(this.jsonObject, ((SimpleJsonObject) obj).jsonObject) : obj instanceof JsonObject && obj.equals(this.jsonObject);
    }

    @Override
    public int hashCode() {
        return this.jsonObject == null ? super.hashCode() : this.jsonObject.hashCode();
    }
}
