package com.github.panapeepo.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;

public interface PluginContainer {

    @NotNull
    PluginState getState();

    @NotNull
    URLClassLoader getClassLoader();

    @NotNull
    Plugin getPlugin();

    @NotNull
    Object getInstance();

}
