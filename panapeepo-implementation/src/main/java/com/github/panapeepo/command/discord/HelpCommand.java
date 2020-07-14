package com.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.derrop.simplecommand.argument.CommandArgumentWrapper;
import com.github.derrop.simplecommand.sender.CommandSender;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.command.DiscordCommandSender;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Command(
        aliases = {"help", "?", "commands"}
)
public class HelpCommand {

    private final Panapeepo panapeepo;

    public HelpCommand(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @SubCommand()
    public void handle(@Nonnull CommandSender sender, @Nonnull CommandArgumentWrapper args) {
        var commandSender = (DiscordCommandSender) sender;
        var embed = new EmbedBuilder();
        var user = commandSender.getMember().getUser();

        embed.setTitle("Help");
        embed.setThumbnail(commandSender.getChannel().getJDA().getSelfUser().getAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.setDescription(
                String.format("The current Command-Prefix for this Guild is `%s`.", this.panapeepo.getConfig().getCommandPrefix())
        );

        embed.setFooter(
                String.format(
                        "%#s • Panapeepo version %s (#%s)",
                        user,
                        this.panapeepo.getCurrentVersion(),
                        this.panapeepo.getCurrentCommit()
                ),
                commandSender.getMember().getUser().getAvatarUrl()
        );

        panapeepo.getDiscordCommandMap().getCommands().forEach(usableCommand -> {
            var title = "`" + String.join("`, `", usableCommand.getAliases()) + "`";
            embed.addField(
                    title,
                    usableCommand.getDescription() != null && !usableCommand.getDescription().isBlank() ?
                            usableCommand.getDescription() : "-",
                    true
            );
        });


        commandSender.getChannel().sendMessage(embed.build()).queue(message -> {
            message.delete().queueAfter(2, TimeUnit.MINUTES);
        });
    }

}
