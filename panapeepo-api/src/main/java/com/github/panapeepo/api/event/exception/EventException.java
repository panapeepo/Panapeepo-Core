package com.github.panapeepo.api.event.exception;

import java.lang.reflect.Method;

public class EventException extends RuntimeException {

    private final Object listener;
    private final Method method;
    private final Object event;

    public EventException(Object listener, Method method, Object event, Throwable cause) {
        super("An error occurred while posting event " + event.getClass().getName() + " to listener " + listener.getClass().getName() + "#" + method.getName(), cause);
        this.listener = listener;
        this.method = method;
        this.event = event;
    }

    public Object getListener() {
        return this.listener;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object getEvent() {
        return this.event;
    }
}
