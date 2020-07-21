package io.github.panapeepo.command;

import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.provider.PanapeepoGuildProvider;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<String> prefix = new AtomicReference<>(this.panapeepo.getConfig().getCommandPrefix());
        this.panapeepo.getServiceRegistry().getProvider(PanapeepoGuildProvider.class).ifPresent(panapeepoGuildProvider -> {
            var guild = panapeepoGuildProvider.getGuild(event.getGuild().getIdLong());
            if (guild != null) {
                prefix.set(guild.getPrefix());
            }
        });

        var input = event.getMessage().getContentRaw();
        if (!this.panapeepo.getConfig().commandsCaseSensitive()) {
            input = input.toLowerCase();
        }
        if (!input.startsWith(prefix.get())) {
            return;
        }

        var command = input.substring(prefix.get().length());
        this.panapeepo.getDiscordCommandMap().dispatchCommand(command, new DefaultDiscordCommandSender(event.getChannel(), event.getMember()));
    }

}
