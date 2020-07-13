package com.github.panapeepo.api.plugin;

public class PluginDependencyNotFoundException extends RuntimeException {

    private final PluginContainer plugin;
    private final PluginDependency dependency;

    public PluginDependencyNotFoundException(PluginContainer plugin, PluginDependency dependency) {
        super("Dependency " + dependency.id() + " for plugin " + plugin.getPlugin().id() + " not found");
        this.plugin = plugin;
        this.dependency = dependency;
    }

    public PluginContainer getPlugin() {
        return this.plugin;
    }

    public PluginDependency getDependency() {
        return this.dependency;
    }
}
