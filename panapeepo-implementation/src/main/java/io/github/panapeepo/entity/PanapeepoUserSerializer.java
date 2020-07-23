package io.github.panapeepo.entity;

import io.github.panapeepo.api.database.buffer.ThreadSaveGrowingByteBuffer;
import io.github.panapeepo.api.database.serializer.ByteToObjectDeserializer;
import io.github.panapeepo.api.database.serializer.ObjectToByteSerializer;
import io.github.panapeepo.api.entity.PanapeepoUser;
import io.netty.buffer.Unpooled;

import java.util.Optional;

public class PanapeepoUserSerializer implements ByteToObjectDeserializer<PanapeepoUser>, ObjectToByteSerializer<PanapeepoUser> {

    @Override
    public byte[] serialize(PanapeepoUser panapeepoUser) {
        var byteBuffer = new ThreadSaveGrowingByteBuffer(Unpooled.buffer());
        byteBuffer.writeLong(panapeepoUser.getId());
        byteBuffer.writeInt(panapeepoUser.getCoins());
        byteBuffer.writeString(panapeepoUser.getLocale());
        return byteBuffer.array();
    }

    @Override
    public Optional<PanapeepoUser> deserialize(ThreadSaveGrowingByteBuffer buffer) {
        var user = new DefaultPanapeepoUser(buffer.readLong());
        user.setCoins(buffer.readInt());
        user.setLocale(buffer.readString());
        return Optional.of(user);
    }
}
