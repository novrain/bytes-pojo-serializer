package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import cn.iota.jiot.serialization.meta.SerializeStringField;
import io.netty.buffer.ByteBuf;

public class StringDeserializer extends AbstractFieldDeserializer {
    private int length(Object obj, Class<?> clazz, SerializeStringField ann)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (ann == null) {
            return 0;
        }
        if (ann.length() != 0) {
            return ann.length();
        }
        if (clazz == null) {
            return 0;
        }
        String fName = ann.refField();

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
        if (annotation == null || annotation.annotationType() != SerializeStringField.class) {
            annotation = field.getAnnotation(SerializeStringField.class);
        }
        if (this.annotation == null) {
            return;
        }
        int length = 0;
        try {
            length = length(buf, clazz, (SerializeStringField) annotation);
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        if (length == 0) {
            return;
        }
        String value = fromBytes(buf, (SerializeStringField) this.annotation, length);
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

    private String fromBytes(ByteBuf buf, SerializeStringField ann, int length) {
        // @Todo pos missing
        // PadPosition pos = ann.padPosition();
        // char pad = ann.pad();
        Charset charset = Charset.forName(ann.charset());
        int start = 0;
        int end = length - 1;
        int index = buf.readerIndex();
        // if(pad == '\0')
        while (true) {
            byte l = buf.getByte(index + start);
            byte r = buf.getByte(index + end);
            if (l != 0 && r != 0 || start >= end) {
                break;
            }
            if (l == 0) {
                start++;
            }
            if (r == 0) {
                end--;
            }
        }
        // }
        String value = "";
        if (start < end) {
            value = buf.toString(index + start, end - start + 1, charset);
        }
        buf.setIndex(index + length, buf.writerIndex());
        return value;
    }

    @Override
    public void fromBytes(Object obj, Field field, ByteBuf buf) {
        fromBytes(obj, obj.getClass(), field, buf);
    }

    @Override
    public Object fromBytes(ByteBuf buf, Object obj) {
        if (this.annotation == null || annotation.annotationType() != SerializeStringField.class) {
            return null;
        }
        int length = 0;
        try {
            length = length(null, null, (SerializeStringField) annotation);
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        if (length == 0) {
            return null;
        }
        String value = fromBytes(buf, (SerializeStringField) this.annotation, length);
        return value;
    }
}
