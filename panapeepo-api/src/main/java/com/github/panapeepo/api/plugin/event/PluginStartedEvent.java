package com.github.panapeepo.api.plugin.event;

import com.github.panapeepo.api.Panapeepo;

public class PluginStartedEvent {

    private final Panapeepo panapeepo;

    public PluginStartedEvent(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    public Panapeepo getPanapeepo() {
        return this.panapeepo;
    }
}
