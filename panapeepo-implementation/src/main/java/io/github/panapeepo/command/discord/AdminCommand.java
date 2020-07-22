package io.github.panapeepo.command.discord;

import com.github.derrop.simplecommand.annotation.Argument;
import com.github.derrop.simplecommand.annotation.Command;
import com.github.derrop.simplecommand.annotation.SubCommand;
import com.github.derrop.simplecommand.argument.ArgumentRequirement;
import com.github.derrop.simplecommand.argument.ArgumentType;
import com.github.derrop.simplecommand.argument.CommandArgumentWrapper;
import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.command.DiscordCommandSender;
import io.github.panapeepo.api.provider.PanapeepoGuildProvider;
import io.github.panapeepo.entity.DefaultPanapeepoGuild;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;

import static com.github.derrop.simplecommand.argument.DefaultArgumentTypes.anyStringIgnoreCase;
import static com.github.derrop.simplecommand.argument.DefaultArgumentTypes.dynamicString;

@Command(
        aliases = {"admin", "serversettings", "serveradmin"}
)
public class AdminCommand {

    private final Panapeepo panapeepo;

    @Argument
    public final ArgumentType<?> setPrefix = anyStringIgnoreCase("setprefix");

    @Argument
    public final ArgumentType<?> value = dynamicString("value", 16);

    public AdminCommand(Panapeepo panapeepo) {
        this.panapeepo = panapeepo;
    }

    @SubCommand()
    public void handle(@Nonnull DiscordCommandSender sender) {
        var user = sender.getMember().getUser();
        this.panapeepo.sendDefaultEmbed(sender.getChannel(), user, embed -> {

            if (sender.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                embed.setTitle("Serversettings");
                embed.addField("`serversettings setprefix <Prefix>`", "Change the Command prefix for this Guild", true);
            } else {
                embed.setTitle("Error.");
                embed.setDescription("Only Server Administrators can use this command");
            }
        });
    }

    @SubCommand(
            args = {"setPrefix", "value"},
            requirement = ArgumentRequirement.EXACT
    )
    public void setPrefixCommand(@Nonnull DiscordCommandSender sender, @Nonnull CommandArgumentWrapper commandArgumentWrapper) {
        var prefix = (String) commandArgumentWrapper.argument("value");

        var guild = this.panapeepo.getServiceRegistry().getProvider(PanapeepoGuildProvider.class).map(
                guildProvider -> guildProvider.getGuild(sender.getMember().getGuild().getIdLong())
        ).orElse(new DefaultPanapeepoGuild(sender.getMember().getGuild().getIdLong(), this.panapeepo.getConfig().getCommandPrefix()));

        if (!guild.getPrefix().equals(prefix)) {
            guild.setPrefix(this.panapeepo, prefix);
            this.panapeepo.getServiceRegistry().getProvider(PanapeepoGuildProvider.class).ifPresent(guildProvider -> {
                guildProvider.updateGuild(guild);
            });
        }
    }

}
