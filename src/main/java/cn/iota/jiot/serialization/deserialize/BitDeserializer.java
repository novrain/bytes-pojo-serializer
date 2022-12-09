package cn.iota.jiot.serialization.deserialize;

// import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.SerializeBitField;
import io.netty.buffer.ByteBuf;

public class BitDeserializer extends ContextualFieldDeserializer {

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            if (this.annotation == null) {
                annotation = field.getAnnotation(SerializeBitField.class);
            }
            Object value = fromBytes(buf, null);
            if (value != null) {
                field.set(obj, value);
            } else {
                // @Todo
            }
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
        // boolean readNext = false;
        // if (context != null) {
        // DeserializationContext.DeserializationEntry lEntry = context.lastEntry();
        // if (lEntry != null) {
        // FieldDeserializer lastSer = lEntry.getFieldSerializer();
        // if (lastSer instanceof BitDeserializer) {
        // Annotation annotation = lastSer.getAnnotation();
        // if (annotation instanceof SerializeBitField) {
        // readNext = ((SerializeBitField) annotation).end();
        // }
        // }
        // }
        // }
        // short v = 0;
        // if (readNext) {
        // v = buf.readUnsignedByte();
        // } else {
        // v = buf.getUnsignedByte(buf.readerIndex());
        // }
        short v = buf.getUnsignedByte(buf.readerIndex());
        SerializeBitField ann = ((SerializeBitField) annotation);
        byte bit = ann.bit();
        byte bitV = (byte) ((v >> bit) & 1);
        if (ann.end()) {
            buf.skipBytes(1);
        }
        return bitV;
    }

}
