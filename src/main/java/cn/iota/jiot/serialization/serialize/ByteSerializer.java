package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteSerializer implements FieldSerializer {

    public ByteBuf toBytes(Object obj, Field field) {
        byte value;
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            value = (byte) field.get(obj);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1);
        buf.writeByte(value);
        return buf;
    }

    @Override
    public ByteBuf toBytes(Object value) {
        try {
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1);
            buf.writeByte((byte) value);
            return buf;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
