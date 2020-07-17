package com.github.panapeepo.api.plugin;

import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.EnumSet;

public interface PluginContainer {

    @NotNull
    PluginState getState();

    @NotNull
    URLClassLoader getClassLoader();

    @NotNull
    Plugin getPlugin();

    @NotNull
    Object getInstance();

    EnumSet<GatewayIntent> getIntents();

    Path getDataFolder();
}
