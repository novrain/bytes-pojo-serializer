package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.reflect.ReflectionUtil;
import cn.iota.jiot.serialization.util.SerializerFactory;
import io.netty.buffer.ByteBuf;

public class PrimitiveListDeserializer extends AbstractFieldDeserializer {

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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void fromBytes(Object obj, Class<?> clazz, Field field, ByteBuf buf) {
        Class<? extends FieldDeserializer> deSerClass = null;
        if (annotation == null) {
            deSerClass = SerializerFactory.findDeserializerClass(ReflectionUtil.getElementType(field), null);
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
            List listFieldValue;
            listFieldValue = (List) field.get(obj);
            if (listFieldValue == null) {
                listFieldValue = new ArrayList<>();
            } else {
                listFieldValue.clear();
            }
            for (int i = 0; i < length; i++) {
                Object o = deSer.fromBytes(buf, null);
                listFieldValue.add(o);
            }
            field.set(obj, listFieldValue);
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
