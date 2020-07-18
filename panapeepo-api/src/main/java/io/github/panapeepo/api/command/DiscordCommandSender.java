package io.github.panapeepo.api.command;

import com.github.derrop.simplecommand.sender.CommandSender;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface DiscordCommandSender extends CommandSender {

    MessageChannel getChannel();

    Member getMember();

    void sendMessage(MessageEmbed embed);

}
