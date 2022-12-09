package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeLongField;
import io.netty.buffer.ByteBuf;

public class LongDeserializer extends AbstractFieldDeserializer {

    private ByteOrder getByteOrder() {
        ByteOrder order = ByteOrder.BIG_ENDIAN;
        if (annotation != null && annotation instanceof SerializeLongField) {
            order = ((SerializeLongField) annotation).order();
        }
        return order;
    }

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        long value = (long) fromBytes(buf, obj);
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
        ByteOrder order = getByteOrder();
        if (order == ByteOrder.LITTLE_ENDIAN) {
            return buf.readLongLE();
        }
        return buf.readLong();
    }
}
