package io.github.panapeepo.api.database.adb;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class CompiledSchema {

    private final String name;
    private final Class<?> type;
    private final Object defaultValue;

    public CompiledSchema(String name, Class<?> type, Object defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public static List<CompiledSchema> compile(Object entity) {
        var list = new ArrayList<CompiledSchema>();

        for (var field : entity.getClass().getFields()) {
            try {
                if (field.isAnnotationPresent(SchemaField.class)) {
                    var annotation = field.getAnnotation(SchemaField.class);
                    if (!Modifier.isTransient(field.getModifiers())) {
                        list.add(new CompiledSchema(
                                (annotation.name().isBlank()) ? field.getName() : annotation.name(),
                                field.getType(),
                                field.get(entity)
                        ));
                    }
                }
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        return list;
    }

}
