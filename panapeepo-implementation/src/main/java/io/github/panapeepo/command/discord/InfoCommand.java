package io.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.command.DiscordCommandSender;
import io.github.panapeepo.api.database.DatabaseDriver;

import javax.annotation.Nonnull;

@Command(
        aliases = {"version", "about", "info"}
)
public class InfoCommand {

    private final Panapeepo panapeepo;

    public InfoCommand(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @SubCommand()
    public void handle(@Nonnull DiscordCommandSender sender) {
        var user = sender.getMember().getUser();
        this.panapeepo.sendDefaultEmbed(sender.getChannel(), user, embed -> {
            embed.setTitle("Info");
            embed.setThumbnail(sender.getChannel().getJDA().getSelfUser().getAvatarUrl());

            embed.addField("Version (Commit)", String.format("%s (%s)", this.panapeepo.getCurrentVersion(), this.panapeepo.getCurrentCommit()), true);
            embed.addField("Memory usage", String.format("%.2f MB / %.2f MB", panapeepo.getUsedMemory(), panapeepo.getMaxMemory()), true);
            embed.addField("Sourcecode", String.format("[Github](%s)", "https://github.com/Panapeepo/Panapeepo-Core"), true);
            embed.addField("Shards (Current Shard)", String.format("%d / %d (#%d)", this.panapeepo.getShardManager().getShardsRunning(), this.panapeepo.getShardManager().getShardsTotal(), user.getJDA().getShardInfo().getShardId() + 1), true);
            embed.addField("Java Version", System.getProperty("java.version"), true);
            embed.addField("Operating System", System.getProperty("os.name") + " - " + System.getProperty("os.arch"), true);
            embed.addField("Uptime", this.panapeepo.formatMillis(System.currentTimeMillis() - this.panapeepo.getStartupTime()), true);

            this.panapeepo.getServiceRegistry().getProvider(DatabaseDriver.class).ifPresent(databaseDriver -> {
                embed.addField("Database (Tables)", String.format("%s (%d)",
                        databaseDriver.getClass().getSimpleName(),
                        databaseDriver.getTableNames().size()), true);
            });
        });
    }

}
