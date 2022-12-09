package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;

public class BCDCodeSerializer implements FieldSerializer {

    @Override
    public ByteBuf toBytes(Object obj, Field field) {
        return null;
    }

    @Override
    public ByteBuf toBytes(Object value) {
        return null;
    }
}
