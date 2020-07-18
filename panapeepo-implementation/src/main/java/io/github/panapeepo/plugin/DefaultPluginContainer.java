package io.github.panapeepo.plugin;

import com.github.derrop.documents.Documents;
import io.github.panapeepo.api.plugin.Plugin;
import io.github.panapeepo.api.plugin.PluginContainer;
import io.github.panapeepo.api.plugin.PluginState;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;

public class DefaultPluginContainer implements PluginContainer {

    private final URLClassLoader classLoader;
    private final Plugin plugin;
    private final Object instance;
    private PluginState state = PluginState.LOADED;
    private final EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
    private final Path dataFolder;
    private Object config;

    public DefaultPluginContainer(URLClassLoader classLoader, Plugin plugin, Object instance, GatewayIntent[] intents) {
        this.classLoader = classLoader;
        this.plugin = plugin;
        this.instance = instance;
        this.intents.addAll(Arrays.asList(intents));
        this.dataFolder = Paths.get(plugin.dataFolder().replace("%PLUGIN_ID%", plugin.id()));
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

    @Override
    public <T> void reloadConfig(Class<? extends T> clazz) {
        if (!Files.isDirectory(this.dataFolder)) {
            try {
                Files.createDirectory(this.dataFolder);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        var configPath = Paths.get(this.dataFolder + "/config.yml");
        if (!Files.exists(configPath)) {
            var defaultConfig = this.classLoader.getResourceAsStream("config.default.yml");
            if (defaultConfig != null) {
                try (var inputStream = this.getClassLoader().getResourceAsStream("config.default.yml");
                     var outputStream = Files.newOutputStream(configPath)) {
                    Objects.requireNonNull(inputStream).transferTo(outputStream);
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            } else {
                this.config = Documents.newDocument().toInstanceOf(clazz);
            }
        }
        this.config = Documents.yamlStorage().read(configPath).toInstanceOf(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getConfig(Class<? extends T> clazz) {
        if (config == null) {
            this.reloadConfig(clazz);
        }
        return (T) this.config;
    }

    @Override
    public void saveConfig() {
        if (config != null) {
            var configPath = Paths.get(this.dataFolder + "/config.yml");
            Documents.yamlStorage().write(Documents.newDocument(this.config), configPath);
        }
    }
}
