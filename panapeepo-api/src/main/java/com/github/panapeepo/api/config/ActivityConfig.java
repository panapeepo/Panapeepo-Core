package com.github.panapeepo.api.config;

import java.util.List;

public interface ActivityConfig {

    int getUpdateInterval();

    List<ConfigPresence> getActivities();

}
