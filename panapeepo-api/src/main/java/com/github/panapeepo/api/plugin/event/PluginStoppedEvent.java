package com.github.panapeepo.api.plugin.event;

import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.plugin.PluginContainer;

public class PluginStoppedEvent {

    private final Panapeepo panapeepo;
    private final PluginContainer pluginContainer;

    public PluginStoppedEvent(Panapeepo panapeepo, PluginContainer pluginContainer) {
        this.panapeepo = panapeepo;
        this.pluginContainer = pluginContainer;
    }

    public Panapeepo getPanapeepo() {
        return this.panapeepo;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
}
