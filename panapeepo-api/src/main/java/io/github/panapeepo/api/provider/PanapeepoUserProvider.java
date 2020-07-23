package io.github.panapeepo.api.provider;

import io.github.panapeepo.api.entity.PanapeepoUser;
import org.jetbrains.annotations.NotNull;

public interface PanapeepoUserProvider {

    PanapeepoUser getUser(long id);

    void updateUser(@NotNull PanapeepoUser user);

}
