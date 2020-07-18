package io.github.panapeepo.api.plugin.event;

import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.plugin.PluginContainer;

public class PluginStartedEvent {

    private final Panapeepo panapeepo;
    private final PluginContainer pluginContainer;

    public PluginStartedEvent(Panapeepo panapeepo, PluginContainer pluginContainer) {
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
