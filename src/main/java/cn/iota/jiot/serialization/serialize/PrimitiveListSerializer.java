package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;
import java.util.List;

import cn.iota.jiot.serialization.reflect.ReflectionUtil;
import cn.iota.jiot.serialization.util.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PrimitiveListSerializer extends
        AbstractFieldSerializer {

    @SuppressWarnings("rawtypes")
    @Override
    public ByteBuf toBytes(Object obj, Field field) {
        Class<? extends FieldSerializer> serClass = null;
        if (annotation == null) {
            serClass = SerializerFactory.findSerializerClass(ReflectionUtil.getElementType(field), null);
        } else {
            serClass = SerializerFactory
                    .findSerializerClass(annotation.annotationType());
        }
        if (serClass == null) {
            return null;
        }
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        List listFieldValue;

        try {
            listFieldValue = (List) field.get(obj);
            if (listFieldValue == null) {
                return null;
            }
            int length = listFieldValue.size();
            if (length == 0) {
                return null;
            }
            FieldSerializer ser = SerializerFactory.createSerializer(serClass);
            if (ser == null) {
                return null;
            }
            ser.setAnnotation(annotation);
            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(128);
            for (int i = 0; i < length; i++) {
                Object item = listFieldValue.get(i);
                ByteBuf fBuf = ser.toBytes(item);
                if (fBuf != null) {
                    buf.writeBytes(fBuf);
                }
            }
            return buf;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }
    }

    @Override
    public ByteBuf toBytes(Object value) {
        return null;
    }

}
