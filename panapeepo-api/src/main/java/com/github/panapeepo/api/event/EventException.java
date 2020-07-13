package com.github.panapeepo.api.event;

import java.lang.reflect.Method;

public class EventException extends RuntimeException {

    private final Object listener;
    private final Method method;

    public EventException(Object listener, Method method, Event event, Throwable cause) {
        super("An error occurred while posting event " + event.getClass().getName() + " to listener " + listener.getClass().getName() + "#" + method.getName(), cause);
        this.listener = listener;
        this.method = method;
    }

    public Object getListener() {
        return this.listener;
    }

    public Method getMethod() {
        return this.method;
    }
}
