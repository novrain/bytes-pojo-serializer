package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeIntField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class IntSerializer extends AbstractFieldSerializer {

    public ByteBuf toBytes(Object obj, Field field) {
        int value;
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            value = (int) field.get(obj);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }

        return toBytes(value);
    }

    @Override
    public ByteBuf toBytes(Object value) {
        try {
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(4);
            ByteOrder order = ByteOrder.BIG_ENDIAN;
            if (annotation != null && annotation instanceof SerializeIntField) {
                order = ((SerializeIntField) annotation).order();
            }
            if (order == ByteOrder.LITTLE_ENDIAN) {
                buf.writeIntLE((int) value);
            } else {
                buf.writeInt((int) value);
            }
            return buf;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
