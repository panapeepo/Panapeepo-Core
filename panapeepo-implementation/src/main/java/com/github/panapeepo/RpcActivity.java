package com.github.panapeepo;

import net.dv8tion.jda.api.entities.Activity;

public class RpcActivity {

    private final Activity.ActivityType type;
    private final String text;

    public RpcActivity(Activity.ActivityType type, String text) {
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
