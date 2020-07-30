package io.github.panapeepo.provider;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.query.QueryOperation;
import io.github.panapeepo.api.database.update.UpdateOperation;
import io.github.panapeepo.api.entity.PanapeepoUser;
import io.github.panapeepo.api.provider.PanapeepoUserProvider;
import io.github.panapeepo.entity.PanapeepoUserSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DefaultPanapeepoUserProvider implements PanapeepoUserProvider {

    private final Table<PanapeepoUser> databaseTable;

    private final LoadingCache<Long, PanapeepoUser> cache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public PanapeepoUser load(@NotNull Long key) throws Exception {
                    var object = databaseTable.select().where("key", QueryOperation.EQUALS, key.toString()).findOne().get(5, TimeUnit.SECONDS);
                    if (object == null) {
                        throw new Exception();
                    }

                    return object;
                }
            });

    public DefaultPanapeepoUserProvider(Panapeepo panapeepo) {
        var serializer = new PanapeepoUserSerializer();
        this.databaseTable = panapeepo.getDatabase().getTable("user", "(`key` TEXT, `coins` BIGINT, `data` LONGBLOB)", serializer, serializer);
    }

    @Override
    public PanapeepoUser getUser(long id) {
        try {
            return this.cache.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    public void updateUser(@NotNull PanapeepoUser user) {
        this.cache.put(user.getId(), user);
        this.databaseTable
                .update(UpdateOperation.UPDATE)
                .set("coins", user.getCoins())
                .set("data", this.databaseTable.getSerializer().serialize(user))
                .where("key", QueryOperation.EQUALS, user.getId())
                .execute();
    }
}
