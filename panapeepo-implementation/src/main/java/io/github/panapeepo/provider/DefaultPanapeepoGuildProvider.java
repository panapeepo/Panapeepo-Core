package io.github.panapeepo.provider;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.panapeepo.api.Panapeepo;
import io.github.panapeepo.api.database.Table;
import io.github.panapeepo.api.database.query.QueryOperation;
import io.github.panapeepo.api.entity.PanapeepoGuild;
import io.github.panapeepo.api.provider.PanapeepoGuildProvider;
import io.github.panapeepo.entity.PanapeepoGuildSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DefaultPanapeepoGuildProvider implements PanapeepoGuildProvider {

    private final Table<PanapeepoGuild> databaseTable;

    private final LoadingCache<Long, PanapeepoGuild> cache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public PanapeepoGuild load(@NotNull Long key) throws Exception {
                    var object = databaseTable.select().where("key", QueryOperation.EQUALS, key).findOne().get(5, TimeUnit.SECONDS);
                    if (object == null) {
                        throw new Exception();
                    }

                    return object;
                }
            });

    public DefaultPanapeepoGuildProvider(Panapeepo panapeepo) {
        var serializer = new PanapeepoGuildSerializer();
        this.databaseTable = panapeepo.getDatabase().getTable("guilds", "(`key` TEXT, `data` LONGBLOB)", serializer, serializer);
    }

    @Override
    public PanapeepoGuild getGuild(long id) {
        try {
            return this.cache.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    public void updateGuild(PanapeepoGuild guild) {
        this.cache.put(guild.getId(), guild);
        this.databaseTable.insert().set("key", guild.getId()).set("data", this.databaseTable.getSerializer().serialize(guild)).execute();
    }
}
