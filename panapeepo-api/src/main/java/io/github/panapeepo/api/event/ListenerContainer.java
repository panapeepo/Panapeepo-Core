package io.github.panapeepo.api.event;

import io.github.panapeepo.api.event.exception.EventException;
import org.jetbrains.annotations.NotNull;

public interface ListenerContainer {

    @NotNull
    Object getListenerInstance();

    @NotNull
    Class<?> getTargetEventClass();

    void call(@NotNull Object event) throws EventException;
}
