package cn.iota.jiot.serialization.serialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.SerializeBitField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class BitSerializer extends ContextualFieldSerializer {

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
        if (this.annotation == null) {
            annotation = field.getAnnotation(SerializeBitField.class);
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
        boolean createNewBuf = true;
        ByteBuf buf = null;
        int index = 0;
        if (context != null) {
            buf = context.buffer();
            index = buf.readableBytes() - 1;
            SerializationContext.SerializationEntry lEntry = context.lastEntry();
            if (lEntry != null) {
                FieldSerializer lastSer = lEntry.getFieldSerializer();
                if (lastSer instanceof BitSerializer) {
                    Annotation annotation = lastSer.getAnnotation();
                    if (annotation instanceof SerializeBitField) {
                        createNewBuf = ((SerializeBitField) annotation).end();
                    }
                }
            }
        }
        if (createNewBuf) {
            buf = ByteBufAllocator.DEFAULT.buffer(1);
            index = 0;
        }
        short v = buf.getUnsignedByte(index);
        byte bit = ((SerializeBitField) annotation).bit();
        // boolean end = ann.end();
        if (value == 0) {
            v = (short) (v & ~(1 << bit));
        } else {
            v = (short) (v | (1 << bit));
        }
        if (createNewBuf) {
            buf.writeByte(v & 0xFF);
            return buf;
        } else {
            // if (end) {
            // buf.writeByte(v & 0xFF);
            // } else {
            buf.setByte(index, v & 0xFF);
            // }
            return null;
        }
    }
}
