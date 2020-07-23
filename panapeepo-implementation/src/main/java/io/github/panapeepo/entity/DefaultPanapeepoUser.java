package io.github.panapeepo.entity;

import io.github.panapeepo.api.entity.PanapeepoUser;
import org.jetbrains.annotations.NotNull;

public class DefaultPanapeepoUser implements PanapeepoUser {

    private long id;
    private int coins;
    private String locale;

    public DefaultPanapeepoUser(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public @NotNull String getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(@NotNull String locale) {
        this.locale = locale;
    }
}
