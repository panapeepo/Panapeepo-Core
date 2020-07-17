package com.github.panapeepo.plugin;

import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.plugin.*;
import com.github.panapeepo.api.plugin.event.PluginStartedEvent;
import com.github.panapeepo.api.plugin.event.PluginStoppedEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DefaultPluginManager implements PluginManager {

    private final Panapeepo panapeepo;
    private final Collection<DefaultPluginContainer> plugins = new ArrayList<>();

    public DefaultPluginManager(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @Override
    public @NotNull Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins);
    }

    @Override
    public Plugin loadPlugin(@NotNull URL url) {
        var classLoader = new FinalizeURLClassLoader(url);

        var mainClass = this.findMainClass(classLoader, url);
        if (mainClass == null) {
            return null;
        }

        var plugin = mainClass.getAnnotation(Plugin.class);

        try {
            var instance = mainClass.getDeclaredConstructor(Panapeepo.class).newInstance(this.panapeepo);

            this.panapeepo.getEventManager().registerListener(instance);
            this.plugins.add(new DefaultPluginContainer(classLoader, plugin, instance, plugin.intents()));

            return plugin;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private Class<?> findMainClass(ClassLoader classLoader, URL url) {
        try (var inputStream = new ZipInputStream(url.openStream())) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                var className = entry.getName().replace('/', '.');
                className = className.substring(0, className.length() - ".class".length());

                Class<?> mainClass;
                try {
                    mainClass = classLoader.loadClass(className);
                } catch (ClassNotFoundException exception) {
                    continue;
                }

                if (!mainClass.isAnnotationPresent(Plugin.class)) {
                    continue;
                }

                return mainClass;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public Plugin loadPlugin(@NotNull Path path) {
        try {
            return this.loadPlugin(path.toUri().toURL());
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadPlugins(@NotNull Path directory) {
        try {
            Files.list(directory).filter(path -> path.getFileName().toString().endsWith(".jar")).forEach(this::loadPlugin);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void enablePlugins() {
        for (var plugin : this.plugins) {
            this.enablePlugin(plugin);
        }
    }

    private void enablePlugin(DefaultPluginContainer plugin) {
        if (plugin.getState() == PluginState.ENABLED) {
            return;
        }

        var dependencies = plugin.getPlugin().depends();
        for (var dependency : dependencies) {
            var optional = this.getPlugin(dependency.id());
            optional.ifPresentOrElse(this::enablePlugin, () -> {
                if (!dependency.optional()) {
                    throw new PluginDependencyNotFoundException(plugin, dependency);
                }
            });
        }

        this.panapeepo.getEventManager().callEvent(new PluginStartedEvent(this.panapeepo), plugin.getInstance());
        plugin.setState(PluginState.ENABLED);
    }

    @Override
    public void disablePlugins() {
        for (var plugin : this.plugins) {

            this.panapeepo.getEventManager().callEvent(new PluginStoppedEvent(this.panapeepo), plugin.getInstance());
            plugin.setState(PluginState.DISABLED);

            this.panapeepo.getEventManager().unregisterListeners(plugin.getClassLoader());

            try {
                plugin.getClassLoader().close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            this.plugins.remove(plugin);
        }
    }

    @Override
    public Optional<DefaultPluginContainer> getPlugin(@NotNull String id) {
        return this.plugins.stream().filter(plugin -> plugin.getPlugin().id().equals(id)).findFirst();
    }

}
