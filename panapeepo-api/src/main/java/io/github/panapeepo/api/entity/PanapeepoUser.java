package io.github.panapeepo.api.entity;

import org.jetbrains.annotations.NotNull;

public interface PanapeepoUser {

    long getId();

    int getCoins();

    void setCoins(int coins);

    @NotNull
    String getLocale();

    void setLocale(@NotNull String locale);

}
