package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeShortField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ShortSerializer extends AbstractFieldSerializer {

    public ByteBuf toBytes(Object obj, Field field) {
        short value;
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            value = (short) field.get(obj);
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
            if (annotation != null && annotation instanceof SerializeShortField) {
                order = ((SerializeShortField) annotation).order();
            }
            if (order == ByteOrder.LITTLE_ENDIAN) {
                buf.writeShortLE((short) value);
            } else {
                buf.writeShort((short) value);
            }
            return buf;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
