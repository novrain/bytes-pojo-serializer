package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.util.SerializerFactory;
import io.netty.buffer.ByteBuf;

public class PrimitiveArrayDeserializer extends AbstractFieldDeserializer {
    private int length(Object obj, Class<?> clazz, Field field)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        SerializeArrayOrListLength lengthAnn = field.getAnnotation(SerializeArrayOrListLength.class);
        if (lengthAnn == null) {
            return 0;
        }
        if (lengthAnn.length() != 0) {
            return lengthAnn.length();
        }
        String fName = lengthAnn.refField();
        Field f = clazz.getDeclaredField(fName);
        if (f == null) {
            return 0;
        }
        boolean accessible = f.canAccess(obj);
        f.setAccessible(true);
        int len = f.getInt(obj);
        f.setAccessible(accessible);
        return len;
    }

    @Override
    public void fromBytes(Object obj, Class<?> clazz, Field field, ByteBuf buf) {
        if (clazz == null) {
            clazz = obj.getClass();
        }
        Class<?> elType = field.getType().getComponentType();
        Class<? extends FieldDeserializer> deSerClass = null;
        if (annotation == null) {
            deSerClass = SerializerFactory.findDeserializerClass(elType, null);
        } else {
            deSerClass = SerializerFactory
                    .findDeserializerClass(annotation.annotationType());
        }
        if (deSerClass == null) {
            return;
        }
        int length;
        try {
            length = length(obj, clazz, field);
        } catch (NoSuchFieldException e) {
            return;
        } catch (SecurityException e) {
            return;
        } catch (IllegalArgumentException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        }
        if (length == 0) {
            return;
        }
        FieldDeserializer deSer = SerializerFactory.createDeserializer(deSerClass);
        if (deSer == null) {
            return;
        }
        deSer.setAnnotation(annotation);
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            Object[] arrFieldValue;
            arrFieldValue = (Object[]) field.get(obj);
            if (arrFieldValue == null || arrFieldValue.length < length) {
                arrFieldValue = (Object[]) Array.newInstance(elType, length);
            }
            for (int i = 0; i < length; i++) {
                Object o = deSer.fromBytes(buf, null);
                arrFieldValue[i] = o;
            }
            field.set(obj, arrFieldValue);
        } catch (IllegalArgumentException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        } finally {
            field.setAccessible(accessible);
        }
    }

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        fromBytes(obj, obj.getClass(), field, buf);
    }

    @Override
    public Object fromBytes(ByteBuf buf, Object obj) {
        return null;
    }
}
