package com.github.panapeepo.api.plugin.event;

import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.event.Event;

public class PluginStartedEvent extends Event {

    private final Panapeepo panapeepo;

    public PluginStartedEvent(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    public Panapeepo getPanapeepo() {
        return this.panapeepo;
    }
}
