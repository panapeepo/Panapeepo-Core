package io.github.panapeepo.entity;

import io.github.panapeepo.api.database.buffer.ThreadSaveGrowingByteBuffer;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.github.panapeepo.api.entity.PanapeepoGuild;
import io.netty.buffer.Unpooled;

import java.util.Optional;

public class PanapeepoGuildSerializer implements ByteToObjectDeserializer<PanapeepoGuild>, ObjectToByteSerializer<PanapeepoGuild> {

    @Override
    public byte[] serialize(PanapeepoGuild panapeepoGuild) {
        var byteBuffer = new ThreadSaveGrowingByteBuffer(Unpooled.buffer());
        byteBuffer.writeLong(panapeepoGuild.getId());
        byteBuffer.writeString(panapeepoGuild.getPrefix());
        return byteBuffer.array();
    }

    @Override
    public Optional<PanapeepoGuild> deserialize(ThreadSaveGrowingByteBuffer buffer) {
        var guild = new DefaultPanapeepoGuild();
        guild.id = buffer.readLong();
        guild.prefix = buffer.readString();
        return Optional.of(guild);
    }

}
