package com.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.command.DiscordCommandSender;

import javax.annotation.Nonnull;
import java.time.Instant;

@Command(
        aliases = {"help", "?", "commands"}
)
public class HelpCommand {

    private final Panapeepo panapeepo;

    public HelpCommand(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @SubCommand()
    public void handle(@Nonnull DiscordCommandSender sender) {
        var user = sender.getMember().getUser();
        this.panapeepo.sendDefaultEmbed(sender.getChannel(), user, embed -> {
            embed.setTitle("Help");
            embed.setThumbnail(sender.getChannel().getJDA().getSelfUser().getAvatarUrl());
            embed.setTimestamp(Instant.now());
            embed.setDescription(
                    String.format("The current Command-Prefix for this Guild is `%s`.", this.panapeepo.getConfig().getCommandPrefix())
            );

            panapeepo.getDiscordCommandMap().getCommands().forEach(command -> {
                var title = "`" + String.join("`, `", command.getAliases()) + "`";
                embed.addField(
                        title,
                        command.getDescription() != null ?
                                command.getDescription() : "",
                        true
                );
            });
        });
    }

}
