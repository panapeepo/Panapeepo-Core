package com.github.panapeepo.api.plugin.event;

import com.github.panapeepo.api.Panapeepo;

public class PluginStoppedEvent {

    private final Panapeepo panapeepo;

    public PluginStoppedEvent(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    public Panapeepo getPanapeepo() {
        return this.panapeepo;
    }
}
