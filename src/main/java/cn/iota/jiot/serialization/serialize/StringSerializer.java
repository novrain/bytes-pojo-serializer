package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import cn.iota.jiot.serialization.meta.PadPosition;
import cn.iota.jiot.serialization.meta.SerializeStringField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

public class StringSerializer extends AbstractFieldSerializer {
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
    public ByteBuf toBytes(Object obj, Class<?> clazz, Field field) {
        String value = "";
        boolean accessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            value = (String) field.get(obj);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            field.setAccessible(accessible);
        }
        if (value == null) {
            value = "";
        }
        if (this.annotation == null) {
            annotation = field.getAnnotation(SerializeStringField.class);
        }
        if (annotation == null) {
            return null;
        }
        int length = 0;
        try {
            length = length(obj, clazz, (SerializeStringField) annotation);
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        if (length == 0) {
            return null;
        }
        SerializeStringField ann = (SerializeStringField) annotation;

        Charset charset = CharsetUtil.UTF_8;
        try {
            charset = Charset.forName(ann.charset());
        } catch (UnsupportedCharsetException e) {

        }
        char pad = ann.pad();
        PadPosition pos = ann.padPosition();
        return toBytes(value, length, pad, pos, charset);
    }

    @Override
    public ByteBuf toBytes(Object obj, Field field) {
        return toBytes(obj, obj.getClass(), field);
    }

    @Override
    public ByteBuf toBytes(Object value) {
        if (value == null) {
            value = "";
        }
        if (!(value instanceof String)) {
            return null;
        }
        if (annotation == null) {
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
        SerializeStringField ann = (SerializeStringField) annotation;
        Charset charset = CharsetUtil.UTF_8;
        try {
            charset = Charset.forName(ann.charset());
        } catch (UnsupportedCharsetException e) {

        }
        char pad = ann.pad();
        PadPosition pos = ann.padPosition();
        return toBytes((String) value, length, pad, pos, charset);
    }

    private ByteBuf toBytes(String value, int length, char pad, PadPosition pos, Charset charset) {
        byte[] bytes = value.getBytes(charset);
        int writeSize = bytes.length;
        int padSize = 0;
        int offset = 0;
        if (length < bytes.length) {
            writeSize = length;
        } else {
            padSize = length - writeSize;
            if (pos == PadPosition.LEFT) {
                offset = padSize;
            }
        }
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(length);
        buf.setIndex(0, offset);
        buf.writeBytes(bytes, 0, writeSize);
        if (pad != '\0' && writeSize < length) {
            int start = writeSize;
            int end = length;
            if (pos == PadPosition.LEFT) {
                start = 0;
                end = offset;
            }
            for (int i = start; i < end; i++) {
                buf.setByte(i, (byte) pad);
            }
        }
        buf.setIndex(0, length);
        return buf;
    }
}
