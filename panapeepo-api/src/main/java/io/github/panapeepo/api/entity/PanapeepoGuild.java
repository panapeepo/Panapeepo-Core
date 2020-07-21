package io.github.panapeepo.api.entity;

import io.github.panapeepo.api.Panapeepo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PanapeepoGuild {

    long getId();

    @NotNull
    String getPrefix();

    void setPrefix(@NotNull Panapeepo panapeepo, @Nullable String prefix);

}
