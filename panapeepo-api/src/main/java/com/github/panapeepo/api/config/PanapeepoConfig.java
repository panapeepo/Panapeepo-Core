package com.github.panapeepo.api.config;

public interface PanapeepoConfig {

    String getToken();

    String getCommandPrefix();

    int getMaxShards();

    ActivityConfig getActivities();

    boolean commandsCaseSensitive();

}
