package com.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.util.MessageUtils;
import com.github.panapeepo.command.DiscordCommandSender;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

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
        var embed = this.panapeepo.createDefaultEmbed(user);

        embed.setTitle("Info");
        embed.setThumbnail(sender.getChannel().getJDA().getSelfUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        MessageUtils.setDefaultFooter(this.panapeepo, user, embed);

        embed.addField("Version", String.format("%s (Commit: %s)", this.panapeepo.getCurrentVersion(), this.panapeepo.getCurrentCommit()), true);
        embed.addField("Memory usage", String.format("%.2f MB / %.2f MB", panapeepo.getUsedMemory(), panapeepo.getMaxMemory()), true);
        embed.addField("Sourcecode", String.format("[Github](%s)", "https://github.com/Panapeepo/Panapeepo-Core"), true);
        embed.addField("Shards", String.format("%d / %d", this.panapeepo.getShardManager().getShardsRunning(), this.panapeepo.getShardManager().getShardsTotal()), true);
        embed.addField("Java Version", System.getProperty("java.version"), true);
        embed.addField("Operating System", System.getProperty("os.name") + " - " + System.getProperty("os.arch"), true);
        embed.addField("Uptime", this.panapeepo.formatMillis(System.currentTimeMillis() - this.panapeepo.getStartupTime()), true);

        sender.getChannel().sendMessage(embed.build())
                .queue(message -> message.delete().queueAfter(2, TimeUnit.MINUTES));
    }

}
