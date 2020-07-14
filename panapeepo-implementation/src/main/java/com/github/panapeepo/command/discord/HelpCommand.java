package com.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.derrop.simplecommand.argument.CommandArgumentWrapper;
import com.github.derrop.simplecommand.sender.CommandSender;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.command.DiscordCommandSender;
import net.dv8tion.jda.api.EmbedBuilder;

import javax.annotation.Nonnull;
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

        panapeepo.getDiscordCommandMap().getCommands().forEach(usableCommand -> {
            var title = "`" + String.join("`, `", usableCommand.getAliases()) + "`";
            embed.addField(title, usableCommand.getDescription() != null && !usableCommand.getDescription().isBlank() ? usableCommand.getDescription() : "Unknown", true);
        });

        embed.setFooter(user.getName() + "#" + user.getDiscriminator(), commandSender.getMember().getUser().getAvatarUrl());


        commandSender.getChannel().sendMessage(embed.build()).queue(message -> {
            message.delete().queueAfter(2, TimeUnit.MINUTES);
        });
    }

}
