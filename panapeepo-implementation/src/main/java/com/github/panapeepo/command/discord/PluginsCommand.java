package com.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.panapeepo.api.Panapeepo;
import com.github.panapeepo.api.command.DiscordCommandSender;

import javax.annotation.Nonnull;

@Command(
        aliases = {"plugins", "modules", "addons"}
)
public class PluginsCommand {

    private final Panapeepo panapeepo;

    public PluginsCommand(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @SubCommand()
    public void handle(@Nonnull DiscordCommandSender sender) {
        var user = sender.getMember().getUser();
        this.panapeepo.sendDefaultEmbed(sender.getChannel(), user, embed -> {
            embed.setTitle("Plugins");
            embed.setThumbnail(sender.getChannel().getJDA().getSelfUser().getAvatarUrl());

            panapeepo.getPluginManager().getPlugins().forEach(pluginContainer -> {
                var plugin = pluginContainer.getPlugin();
                var title = "[" + plugin.displayName() + "](" + plugin.website() + ")";
                embed.addField(
                        title,
                        String.format("Version: %s. Authors: %s", plugin.version(), String.join(", ", plugin.authors())),
                        true
                );
            });
        });
    }


}
