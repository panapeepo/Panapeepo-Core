package com.github.panapeepo;

import com.github.panapeepo.shared.config.SimpleJsonObject;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Panapeepo {
    private final Timer timer = new Timer();
    private final Random random = new Random();
    private final ShardManager shardManager;

    public static void main(String[] args) {
        SimpleJsonObject jsonObject = SimpleJsonObject.load("config.json");
        jsonObject.appendDefault(Panapeepo.class.getClassLoader().getResourceAsStream("config.default.json"))
                .saveAsFile("config.json");
        if (jsonObject.getString("token").isEmpty()) {
            System.out.println("Set the bot token in the config and start the bot again!");
            System.exit(-1);
        }
        try {
            new Panapeepo(jsonObject, args);
        } catch (LoginException exception) {
            exception.printStackTrace();
        }
    }

    Panapeepo(@Nonnull SimpleJsonObject configurable, @Nonnull String[] args) throws LoginException {
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder(configurable.getString("token"));
        builder.setAutoReconnect(true);
        this.shardManager = builder.build();

        for (int i = 0; i < configurable.getInt("maxShards"); i++) {
            this.shardManager.start(i);
        }

        SimpleJsonObject rpc = configurable.getJsonObject("rpc");
        List<String> activities = rpc.getObject("activities", TypeToken.getParameterized(List.class, String.class).getType());
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (JDA shard : shardManager.getShards()) {
                    if (shard != null)
                        shard.getPresence().setActivity(
                                Activity.playing(activities.get(random.nextInt(activities.size())) +
                                        " | #" + shard.getShardInfo().getShardId()
                                ));
                }
            }
        }, 0, rpc.getLong("change") * 1000);
    }

}
