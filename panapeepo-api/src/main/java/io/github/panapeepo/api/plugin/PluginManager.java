package io.github.panapeepo.api.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public interface PluginManager {

    @NotNull
    Collection<PluginContainer> getPlugins();

    @Nullable
    Plugin loadPlugin(@NotNull URL url);

    @Nullable
    Plugin loadPlugin(@NotNull Path path);

    void loadPlugins(@NotNull Path directory);

    void enablePlugins();

    void disablePlugins();

    Optional<? extends PluginContainer> getPlugin(@NotNull String id);

}
