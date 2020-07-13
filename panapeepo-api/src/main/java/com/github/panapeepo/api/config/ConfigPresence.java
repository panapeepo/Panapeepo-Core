package com.github.panapeepo.api.config;

import net.dv8tion.jda.api.entities.Activity;

public class ConfigPresence {

    private final Activity.ActivityType type;
    private final String text;

    public ConfigPresence(Activity.ActivityType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Activity.ActivityType getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }
}
