package com.github.panapeepo;

import com.github.panapeepo.config.SimpleJsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Panapeepo {
    private final JDA jda;

    public static void main(String[] args) {
        SimpleJsonObject jsonObject = SimpleJsonObject.load("config.json");
        if (!jsonObject.contains("token")) {
            jsonObject.append("token", "").saveAsFile("config.json");
            System.out.println("Config created. Set the bot token in the config and start the bot again!");
            System.exit(-1);
        }
        try {
            new Panapeepo(jsonObject, args);
        } catch (LoginException exception) {
            exception.printStackTrace();
        }
    }

    Panapeepo(@Nonnull SimpleJsonObject configurable, @Nonnull String[] args) throws LoginException {
        this.jda = new JDABuilder(configurable.getString("token")).setAutoReconnect(true).build();
        this.jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("Help us at http://github.com/panapeepo/Panapeepo-Opensource"));
    }

}
