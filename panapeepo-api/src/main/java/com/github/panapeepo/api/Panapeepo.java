package com.github.panapeepo.api;

import com.github.panapeepo.api.event.EventManager;
import com.github.panapeepo.api.plugin.PluginManager;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

public interface Panapeepo {

    @NotNull
    EventManager getEventManager();

    @NotNull
    PluginManager getPluginManager();

    @NotNull
    ShardManager getShardManager();

    void shutdown();

}
