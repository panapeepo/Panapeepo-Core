package com.github.panapeepo;

import com.github.derrop.documents.Document;
import com.github.derrop.documents.Documents;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.event.EventManager;
import com.github.panapeepo.api.plugin.PluginManager;
import com.github.panapeepo.event.DefaultEventManager;
import com.github.panapeepo.plugin.DefaultPluginManager;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PanapeepoCore implements Panapeepo {

    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(4);
    private final Random random = new Random();
    private final ShardManager shardManager;

    private final EventManager eventManager = new DefaultEventManager();
    private final PluginManager pluginManager = new DefaultPluginManager(this);

    public static void main(String[] args) throws IOException {
        Path configPath = Paths.get("config.yml");
        if (!Files.exists(configPath)) {
            try (InputStream inputStream = PanapeepoCore.class.getClassLoader().getResourceAsStream("config.default.yml");
                 OutputStream outputStream = Files.newOutputStream(configPath)) {
                Objects.requireNonNull(inputStream).transferTo(outputStream);
            }

            System.exit(-1);
        }

        Document config = Documents.yamlStorage().read(configPath);
        if (config.getString("token").isEmpty()) {
            System.out.println("Set the bot token in the config and start the bot again!");
            System.exit(-1);
        }

        try {
            new PanapeepoCore(config, args);
        } catch (LoginException exception) {
            exception.printStackTrace();
        }
    }

    PanapeepoCore(@Nonnull Document config, @Nonnull String[] args) throws LoginException {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(config.getString("token"), Arrays.asList(GatewayIntent.values()));
        builder.setAutoReconnect(true);
        this.shardManager = builder.build();

        for (int i = 0; i < config.getInt("maxShards"); i++) {
            this.shardManager.start(i);
        }

        Document rpc = config.getDocument("rpc");
        this.startRPCTimer(rpc);

        this.pluginManager.loadPlugins(Paths.get("plugins"));
        this.pluginManager.enablePlugins();
    }

    private void startRPCTimer(Document rpc) {
        List<RpcActivity> activities = rpc.get("activities", TypeToken.getParameterized(List.class, RpcActivity.class).getType());

        this.timer.scheduleAtFixedRate(() -> {
            for (JDA shard : shardManager.getShards()) {
                if (shard != null)
                    shard.getPresence().setActivity(
                            Activity.playing(activities.get(random.nextInt(activities.size())) +
                                    " | #" + shard.getShardInfo().getShardId()
                            ));
            }
        }, 0, rpc.getLong("changeInterval"), TimeUnit.SECONDS);
    }

    @Override
    public @NotNull EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public @NotNull ShardManager getShardManager() {
        return this.shardManager;
    }

    @Override
    public void shutdown() {
        this.pluginManager.disablePlugins();
    }
}
