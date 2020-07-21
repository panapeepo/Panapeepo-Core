package io.github.panapeepo.api.provider;

import io.github.panapeepo.api.entity.PanapeepoGuild;

public interface PanapeepoGuildProvider {

    PanapeepoGuild getGuild(long id);

    void updateGuild(PanapeepoGuild guild);

}
