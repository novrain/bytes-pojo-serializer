package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeIntegerField;
import io.netty.buffer.ByteBuf;

public class IntegerTypeFieldDeserializer extends AbstractFieldDeserializer {

    private long fromBytes(ByteBuf buf, SerializeIntegerField ann) {
        long value = 0;
        byte size = ann.size();
        ByteOrder order = ann.order();
        if (order == ByteOrder.LITTLE_ENDIAN) {
            for (byte i = 0; i < size; i++) {
                long b = buf.readUnsignedByte();
                value = value | ((long) b << i * 8);
            }
        } else {
            for (byte i = 0; i < size; i++) {
                long b = buf.readUnsignedByte();
                value = value | ((long) b << (size - i - 1) * 8);
            }
        }
        return value;
    }

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        if (annotation == null || annotation.annotationType() != SerializeIntegerField.class) {
            annotation = field.getAnnotation(SerializeIntegerField.class);
        }
        if (this.annotation == null) {
            return;
        }
        long value = fromBytes(buf, (SerializeIntegerField) this.annotation);
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        Class<?> rawType = field.getType();
        try {
            if (rawType == Integer.TYPE || rawType == Integer.class) {
                field.setInt(obj, (int) value);
            }
            if (rawType == Long.TYPE || rawType == Long.class) {
                field.setLong(obj, value);
            }
            if (rawType == Byte.TYPE || rawType == Byte.class) {
                field.setByte(obj, (byte) value);
            }
            if (rawType == Short.TYPE || rawType == Short.class) {
                field.setShort(obj, (short) value);
            }
            // if (rawType == Boolean.TYPE) {
            // }
            // if (rawType == Character.TYPE) {
            // }
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
        if (this.annotation == null || annotation.annotationType() != SerializeIntegerField.class) {
            return null;
        }
        long value = fromBytes(buf, (SerializeIntegerField) this.annotation);
        return value;
    }
}
