package com.github.panapeepo.config;

import com.github.panapeepo.api.config.ActivityConfig;
import com.github.panapeepo.api.config.PanapeepoConfig;

public class DefaultPanapeepoConfig implements PanapeepoConfig {

    private final String token;
    private final String commandPrefix;
    private final int maxShards;
    private final DefaultActivityConfig presence;

    public DefaultPanapeepoConfig(String token, String commandPrefix, int maxShards, DefaultActivityConfig presence) {
        this.token = token;
        this.commandPrefix = commandPrefix;
        this.maxShards = maxShards;
        this.presence = presence;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getCommandPrefix() {
        return this.commandPrefix;
    }

    @Override
    public int getMaxShards() {
        return this.maxShards;
    }

    @Override
    public ActivityConfig getActivities() {
        return this.presence;
    }

}
