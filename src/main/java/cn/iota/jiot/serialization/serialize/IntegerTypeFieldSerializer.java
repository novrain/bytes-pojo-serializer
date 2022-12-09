package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeIntegerField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class IntegerTypeFieldSerializer extends AbstractFieldSerializer {

    @Override
    public ByteBuf toBytes(Object obj, Field field) {
        long value = 0;
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            value = field.getLong(obj);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }
        if (annotation == null) {
            annotation = field.getAnnotation(SerializeIntegerField.class);
        }
        return toBytes(value);
    }

    @Override
    public ByteBuf toBytes(Object vObj) {
        long value = 0;
        try {
            value = (long) vObj;
        } catch (ClassCastException e) {
            return null;
        }
        if (this.annotation == null) {
            return null;
        }
        SerializeIntegerField ann = (SerializeIntegerField) annotation;
        byte size = ann.size();
        ByteOrder order = ann.order();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(size);
        if (order == ByteOrder.BIG_ENDIAN) {
            for (byte i = 0; i < size; i++) {
                byte b = (byte) ((value >> (size - i - 1) * 8) & 0xFF);
                buf.writeByte(b);
            }
        } else {
            for (byte i = 0; i < size; i++) {
                byte b = (byte) ((value >> i * 8) & 0xFF);
                buf.writeByte(b);
            }
        }
        return buf;
    }

}
