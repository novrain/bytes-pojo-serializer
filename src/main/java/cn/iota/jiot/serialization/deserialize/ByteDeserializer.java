package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;

public class ByteDeserializer implements FieldDeserializer {

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        byte value = buf.readByte();
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        } finally {
            field.setAccessible(accessible);
        }
    }

    @Override
    public Object fromBytes(ByteBuf buf, Object obj) {
        // @Todo check obj type
        byte value = buf.readByte();
        return value;
    }
}
