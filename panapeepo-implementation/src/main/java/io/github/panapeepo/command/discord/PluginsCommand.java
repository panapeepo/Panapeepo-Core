package io.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.command.DiscordCommandSender;

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
                String title;
                if (plugin.website().isBlank()) {
                    title = plugin.displayName();
                } else {
                    title = String.format("[%s](%s)", plugin.displayName(), plugin.website());
                }

                var description = new StringBuilder("Version: " + plugin.version() + ".");
                if (plugin.authors().length > 0) {
                    description.append(" Author");
                    if (plugin.authors().length != 1) {
                        description.append("s");
                    }
                    description.append(": ").append(String.join(", ", plugin.authors()));
                    description.append(".");
                }
                if (!plugin.description().isBlank()) {
                    description.append(" ");
                    description.append(plugin.description());
                }

                embed.addField(
                        title,
                        description.toString(),
                        true
                );
            });
        });
    }


}
