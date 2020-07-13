package com.github.panapeepo.event;

import com.github.panapeepo.api.event.Event;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RegisteredListener {

    private final Object listener;
    private final Method method;

    public RegisteredListener(@NotNull Object listener, @NotNull Method method) {
        this.listener = listener;
        this.method = method;
    }

    @NotNull
    public Object getListener() {
        return this.listener;
    }

    @NotNull
    public Method getMethod() {
        return this.method;
    }

    public void fire(Event event) throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(this.listener, event);
    }

}
