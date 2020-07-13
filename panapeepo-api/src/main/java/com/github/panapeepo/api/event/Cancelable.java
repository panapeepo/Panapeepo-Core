package com.github.panapeepo.api.event;

public interface Cancelable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    default void cancel() {
        this.setCancelled(true);
    }

}
