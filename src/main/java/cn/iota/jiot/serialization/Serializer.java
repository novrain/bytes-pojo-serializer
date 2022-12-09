package cn.iota.jiot.serialization;

import io.netty.buffer.ByteBuf;

public interface Serializer {
    <T> T fromBytesByClass(ByteBuf buf, Class<T> objectType);

    <T> T fromBytes(ByteBuf buf, T object);

    <T> ByteBuf toBytes(T object);
}
