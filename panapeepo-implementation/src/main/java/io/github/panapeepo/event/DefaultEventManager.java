package io.github.panapeepo.event;

import io.github.panapeepo.api.event.EventHandler;
import io.github.panapeepo.api.event.EventManager;
import io.github.panapeepo.api.event.ListenerContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultEventManager implements EventManager {

    private final Collection<ListenerContainer> listenerContainers = new CopyOnWriteArrayList<>();

    @Override
    public <T> T callEvent(@NotNull T event) {
        return this.callEvent(event, null);
    }

    @Override
    public <T> T callEvent(@NotNull T event, @Nullable Object listener) {
        for (var listenerContainer : this.listenerContainers) {
            if (listenerContainer.getTargetEventClass().equals(event.getClass())) {
                if (listener == null || listener == listenerContainer.getListenerInstance()) {
                    listenerContainer.call(event);
                }
            }
        }

        return event;
    }

    @Override
    public void registerListener(@NotNull Object listener) {
        for (var method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                var parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    continue;
                }

                method.setAccessible(true);
                this.listenerContainers.add(new DefaultListenerContainer(parameters[0], listener, method));
            }
        }
    }

    @Override
    public void unregisterListener(@NotNull Object listener) {
        this.listenerContainers.removeIf(listenerContainer -> listenerContainer.getListenerInstance() == listener);
    }

    @Override
    public void unregisterListeners(@NotNull Class<?> listenerClass) {
        this.listenerContainers.removeIf(listenerContainer -> listenerContainer.getListenerInstance().getClass().equals(listenerClass));
    }

    @Override
    public void unregisterListeners(@NotNull ClassLoader classLoader) {
        this.listenerContainers.removeIf(listenerContainer -> listenerContainer.getListenerInstance().getClass().getClassLoader().equals(classLoader));
    }
}
