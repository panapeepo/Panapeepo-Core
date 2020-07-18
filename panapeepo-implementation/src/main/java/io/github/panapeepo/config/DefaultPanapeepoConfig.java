package io.github.panapeepo.config;

import io.github.panapeepo.api.config.ActivityConfig;
import io.github.panapeepo.api.config.PanapeepoConfig;

public class DefaultPanapeepoConfig implements PanapeepoConfig {

    private final String token;
    private final String commandPrefix;
    private final int maxShards;
    private final DefaultActivityConfig presence;
    private final boolean commandsCaseSensitive;

    public DefaultPanapeepoConfig(String token, String commandPrefix, int maxShards, DefaultActivityConfig presence, boolean commandsCaseSensitive) {
        this.token = token;
        this.commandPrefix = commandPrefix;
        this.maxShards = maxShards;
        this.presence = presence;
        this.commandsCaseSensitive = commandsCaseSensitive;
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

    @Override
    public boolean commandsCaseSensitive() {
        return this.commandsCaseSensitive;
    }

}
