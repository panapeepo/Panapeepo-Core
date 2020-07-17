package com.github.panapeepo.command;

import com.github.panapeepo.api.Panapeepo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    private final Panapeepo panapeepo;

    public CommandListener(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        var prefix = this.panapeepo.getConfig().getCommandPrefix();
        var input = event.getMessage().getContentRaw();
        if (!this.panapeepo.getConfig().commandsCaseSensitive()) {
            input = input.toLowerCase();
        }
        if (!input.startsWith(prefix)) {
            return;
        }

        var command = input.substring(prefix.length());
        this.panapeepo.getDiscordCommandMap().dispatchCommand(command, new DefaultDiscordCommandSender(event.getChannel(), event.getMember()));
    }

}
