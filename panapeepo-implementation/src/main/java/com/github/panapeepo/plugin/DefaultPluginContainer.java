package com.github.panapeepo.plugin;

import com.github.panapeepo.api.plugin.Plugin;
import com.github.panapeepo.api.plugin.PluginContainer;
import com.github.panapeepo.api.plugin.PluginState;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;

public class DefaultPluginContainer implements PluginContainer {

    private final URLClassLoader classLoader;
    private final Plugin plugin;
    private final Object instance;
    private PluginState state = PluginState.LOADED;

    public DefaultPluginContainer(URLClassLoader classLoader, Plugin plugin, Object instance) {
        this.classLoader = classLoader;
        this.plugin = plugin;
        this.instance = instance;
    }

    public void setState(PluginState state) {
        this.state = state;
    }

    @Override
    public @NotNull PluginState getState() {
        return this.state;
    }

    @Override
    public @NotNull URLClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull Object getInstance() {
        return this.instance;
    }

}
