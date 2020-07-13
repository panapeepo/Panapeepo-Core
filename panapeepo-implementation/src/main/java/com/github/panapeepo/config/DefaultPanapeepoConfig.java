package com.github.panapeepo.config;

import com.github.panapeepo.api.config.ActivityConfig;
import com.github.panapeepo.api.config.PanapeepoConfig;

public class DefaultPanapeepoConfig implements PanapeepoConfig {

    private final String token;
    private final int maxShards;
    private final DefaultActivityConfig presence;

    public DefaultPanapeepoConfig(String token, int maxShards, DefaultActivityConfig presence) {
        this.token = token;
        this.maxShards = maxShards;
        this.presence = presence;
    }

    @Override
    public String getToken() {
        return this.token;
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
