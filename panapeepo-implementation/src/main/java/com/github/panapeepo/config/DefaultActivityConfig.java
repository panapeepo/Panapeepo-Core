package com.github.panapeepo.config;

import com.github.panapeepo.api.config.ActivityConfig;
import com.github.panapeepo.api.config.ConfigPresence;

import java.util.List;

public class DefaultActivityConfig implements ActivityConfig {

    private final int updateInterval;
    private final List<ConfigPresence> activities;

    public DefaultActivityConfig(int updateInterval, List<ConfigPresence> activities) {
        this.updateInterval = updateInterval;
        this.activities = activities;
    }

    @Override
    public int getUpdateInterval() {
        return this.updateInterval;
    }

    @Override
    public List<ConfigPresence> getActivities() {
        return this.activities;
    }
}
