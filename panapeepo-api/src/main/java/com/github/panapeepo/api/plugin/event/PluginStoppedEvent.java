package com.github.panapeepo.api.plugin.event;

import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.event.Event;

public class PluginStoppedEvent extends Event {

    private final Panapeepo panapeepo;

    public PluginStoppedEvent(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    public Panapeepo getPanapeepo() {
        return this.panapeepo;
    }
}
