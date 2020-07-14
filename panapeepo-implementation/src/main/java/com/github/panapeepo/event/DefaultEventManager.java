package com.github.panapeepo.event;

import com.github.panapeepo.api.event.Event;
import com.github.panapeepo.api.event.EventException;
import com.github.panapeepo.api.event.EventHandler;
import com.github.panapeepo.api.event.EventManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class DefaultEventManager implements EventManager {

    private final Map<Class<? extends Event>, Collection<RegisteredListener>> registeredListeners = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> T callEvent(@NotNull T event) {
        Collection<RegisteredListener> listeners = this.registeredListeners.get(event.getClass());
        if (listeners != null) {
            this.fireEvent(event, listeners);
        }
        return event;
    }

    @Override
    public <T extends Event> T callEvent(@NotNull T event, @NotNull Object listener) {
        Collection<RegisteredListener> listeners = this.registeredListeners.get(event.getClass());
        if (listeners != null) {
            for (RegisteredListener registeredListener : listeners) {
                if (registeredListener.getListener() == listener) {
                    this.fireEvent(event, Collections.singletonList(registeredListener));
                }
            }
        }
        return event;
    }

    private void fireEvent(@NotNull Event event, @NotNull Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Throwable exception) {
                throw new EventException(listener.getListener(), listener.getMethod(), event, exception);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerListener(@NotNull Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length != 1) {
                    continue;
                }
                Class<?> type = parameters[0].getType();
                if (!Event.class.isAssignableFrom(type)) {
                    continue;
                }

                method.setAccessible(true);

                Class<? extends Event> eventClass = (Class<? extends Event>) type;
                this.registeredListeners.computeIfAbsent(eventClass, aClass -> new ArrayList<>())
                        .add(new RegisteredListener(listener, method));
            }
        }
    }

    private void unregisterListeners(Predicate<RegisteredListener> tester) {
        for (Collection<RegisteredListener> listeners : this.registeredListeners.values()) {
            listeners.removeIf(tester);
        }
        this.registeredListeners.values().removeIf(Collection::isEmpty);
    }

    @Override
    public void unregisterListener(@NotNull Object listener) {
        this.unregisterListeners(registeredListener -> registeredListener.getListener() == listener);
    }

    @Override
    public void unregisterListeners(@NotNull Class<?> listenerClass) {
        this.unregisterListeners(registeredListener -> registeredListener.getListener().getClass().equals(listenerClass));
    }

    @Override
    public void unregisterListeners(@NotNull ClassLoader classLoader) {
        this.unregisterListeners(registeredListener -> registeredListener.getListener().getClass().getClassLoader().equals(classLoader));
    }
}
