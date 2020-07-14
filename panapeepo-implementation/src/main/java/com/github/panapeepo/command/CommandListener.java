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
        String prefix = this.panapeepo.getConfig().getCommandPrefix();
        String input = event.getMessage().getContentRaw();
        if (!input.startsWith(prefix)) {
            return;
        }

        String command = input.substring(prefix.length());
        this.panapeepo.getDiscordCommandMap().dispatchCommand(command, new DiscordCommandSender(event.getChannel(), event.getMember()));
    }

}
