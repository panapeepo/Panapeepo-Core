package com.github.panapeepo.api.event;

import org.jetbrains.annotations.NotNull;

public interface EventManager {

    <T extends Event> T callEvent(@NotNull T event) throws EventException;

    <T extends Event> T callEvent(@NotNull T event, @NotNull Object listener) throws EventException;

    void registerListener(@NotNull Object listener);

    void unregisterListener(@NotNull Object listener);

    void unregisterListeners(@NotNull Class<?> listenerClass);

    void unregisterListeners(@NotNull ClassLoader classLoader);

}
