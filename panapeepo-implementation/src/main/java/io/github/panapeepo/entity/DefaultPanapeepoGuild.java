package io.github.panapeepo.entity;

import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.entity.PanapeepoGuild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultPanapeepoGuild implements PanapeepoGuild {

    protected long id;
    protected String prefix;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getPrefix() {
        return this.prefix;
    }

    @Override
    public void setPrefix(@NotNull Panapeepo panapeepo, @Nullable String prefix) {
        if (prefix == null) {
            prefix = panapeepo.getConfig().getCommandPrefix();
        }
        this.prefix = prefix;
    }
}
