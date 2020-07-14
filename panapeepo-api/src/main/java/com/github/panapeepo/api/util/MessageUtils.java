package com.github.panapeepo.api.util;

import com.github.panapeepo.api.Panapeepo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public final class MessageUtils {

    private MessageUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getAvatarUrl(User user) {
        return user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();
    }

    public static void setDefaultFooter(Panapeepo panapeepo, User user, EmbedBuilder embed) {
        embed.setFooter(
                String.format(
                        "%#s • Panapeepo version %s (#%s)",
                        user,
                        panapeepo.getCurrentVersion(),
                        panapeepo.getCurrentCommit()
                ),
                MessageUtils.getAvatarUrl(user)
        );
    }

}
