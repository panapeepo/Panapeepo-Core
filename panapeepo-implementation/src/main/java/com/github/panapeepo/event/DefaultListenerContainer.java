package com.github.panapeepo.event;

import com.github.panapeepo.api.event.Event;
import com.github.panapeepo.api.event.ListenerContainer;
import com.github.panapeepo.api.event.exception.EventException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class DefaultListenerContainer implements ListenerContainer {

    private final Class<?> eventClassTarget;
    private final Object listenerInstance;
    private final Method method;

    DefaultListenerContainer(Class<?> eventClassTarget, Object listenerInstance, Method method) {
        this.eventClassTarget = eventClassTarget;
        this.listenerInstance = listenerInstance;
        this.method = method;
    }

    @Override
    public @NotNull Object getListenerInstance() {
        return this.listenerInstance;
    }

    @Override
    public @NotNull Class<?> getTargetEventClass() {
        return this.eventClassTarget;
    }

    @Override
    public void call(@NotNull Event event) throws EventException {
        try {
            this.method.invoke(this.listenerInstance, event);
        } catch (InvocationTargetException | IllegalAccessException exception) {
            throw new EventException(this.listenerInstance, this.method, event, exception);
        }
    }
}
