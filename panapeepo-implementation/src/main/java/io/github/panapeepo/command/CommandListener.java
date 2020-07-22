package io.github.panapeepo.command;

import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.provider.PanapeepoGuildProvider;
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

        var prefix = this.panapeepo.getServiceRegistry().getProvider(PanapeepoGuildProvider.class).map(guildProvider -> {
            var guild = guildProvider.getGuild(event.getGuild().getIdLong());
            return guild == null ? null : guild.getPrefix();
        }).orElse(this.panapeepo.getConfig().getCommandPrefix());

        var input = event.getMessage().getContentRaw();

        if (!startsWith(input, prefix, !this.panapeepo.getConfig().commandsCaseSensitive())) {
            return;
        }

        var command = input.substring(prefix.length());
        this.panapeepo.getDiscordCommandMap().dispatchCommand(command, new DefaultDiscordCommandSender(event.getChannel(), event.getMember()));
    }

    private boolean startsWith(String input, String prefix, boolean ignoreCase) {
        if (ignoreCase) {
            return input.toLowerCase().startsWith(prefix);
        }
        return input.startsWith(prefix);
    }

}
