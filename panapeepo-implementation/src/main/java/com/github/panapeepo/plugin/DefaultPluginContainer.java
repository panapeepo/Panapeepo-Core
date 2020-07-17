package com.github.panapeepo.plugin;

import com.github.panapeepo.api.plugin.Plugin;
import com.github.panapeepo.api.plugin.PluginContainer;
import com.github.panapeepo.api.plugin.PluginState;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumSet;

public class DefaultPluginContainer implements PluginContainer {

    private final URLClassLoader classLoader;
    private final Plugin plugin;
    private final Object instance;
    private PluginState state = PluginState.LOADED;
    private final EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
    private final Path dataFolder;

    public DefaultPluginContainer(URLClassLoader classLoader, Plugin plugin, Object instance, GatewayIntent[] intents) {
        this.classLoader = classLoader;
        this.plugin = plugin;
        this.instance = instance;
        this.intents.addAll(Arrays.asList(intents));
        this.dataFolder = Path.of(plugin.dataFolder().replace("%PLUGIN_ID%", plugin.id()));
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

    @Override
    public EnumSet<GatewayIntent> getIntents() {
        return this.intents;
    }

    @Override
    public Path getDataFolder() {
        return this.dataFolder;
    }
}
