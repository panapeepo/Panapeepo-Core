package com.github.panapeepo;

import java.util.Arrays;
import java.util.Objects;

public class Validate {
    public static <T> T notNull(T object, String message) {
        if (object == null)
            throw new NullPointerException(message == null ? "An argument was null!" : message);
        return object;
    }

    public static <T> T notNull(T object) {
        return notNull(object, null);
    }

    public static <T> T[] noneNull(T[] objects, String message) {
        if (objects == null || Arrays.stream(objects).anyMatch(Objects::isNull))
            throw new NullPointerException(message == null ? "An argument was null!" : message);
        return objects;
    }

    @SafeVarargs
    public static <T> T[] noneNull(T... objects) {
        return noneNull(objects, null);
    }

    public static <T> void isNull(T object, String message) {
        if (object != null)
            throw new IllegalArgumentException(message == null ? "An argument was not null, but it must be null!" : message);
    }

    public static <T> void isNull(T object) {
        isNull(object, null);
    }

    public static void isTrue(boolean argument, String message) {
        if (!argument)
            throw new IllegalArgumentException(message == null ? "Value must be True!" : message);
    }

    public static void isTrue(boolean argument) {
        isTrue(argument, null);
    }

    public static void isFalse(boolean argument, String message) {
        if (argument)
            throw new IllegalArgumentException(message == null ? "Value must be False!" : message);
    }

    public static void isFalse(boolean argument) {
        isFalse(argument, null);
    }
}
