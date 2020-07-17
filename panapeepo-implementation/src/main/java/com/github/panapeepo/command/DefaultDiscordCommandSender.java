package com.github.panapeepo.command;

import com.github.panapeepo.api.command.DiscordCommandSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class DefaultDiscordCommandSender implements DiscordCommandSender {

    private final MessageChannel channel;
    private final Member member;

    public DefaultDiscordCommandSender(MessageChannel channel, Member member) {
        this.channel = channel;
        this.member = member;
    }

    public MessageChannel getChannel() {
        return this.channel;
    }

    public Member getMember() {
        return this.member;
    }

    public void sendMessage(MessageEmbed embed) {
        this.channel.sendMessage(embed).queue();
    }

    private void sendRawMessage(String message) {
        this.sendMessage(new EmbedBuilder()
                .setDescription(message)
                .setFooter(this.getName(), this.member.getUser().getAvatarUrl() != null ? this.member.getUser().getAvatarUrl() : this.member.getUser().getDefaultAvatarUrl())
                .build());
    }

    @Override
    public void sendMessage(@NotNull String message) {
        String currentMessage = message;
        while (currentMessage.length() > 2000) {
            boolean done = false;

            for (int i = 2000; i >= 0; i--) {
                if (currentMessage.charAt(i) == '\n') {
                    this.sendRawMessage(currentMessage.substring(0, i));
                    currentMessage = currentMessage.substring(i);
                    done = true;
                    break;
                }
            }

            if (done) {
                continue;
            }

            this.sendRawMessage(currentMessage.substring(0, 2000));
            currentMessage = currentMessage.substring(2000);
        }

        if (currentMessage.length() != 0) {
            this.sendRawMessage(currentMessage);
        }
    }

    @Override
    public void sendMessage(@NotNull String... messages) {
        this.sendMessage(String.join("\n", messages));
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return true;
    }

    @Override
    public String getName() {
        return this.member.getUser().getAsTag();
    }

    @Override
    public boolean isConsole() {
        return false;
    }
}
