package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;

public class ObjectSerializer extends ContextualFieldSerializer {

    @Override
    public ByteBuf toBytes(Object obj, Field f) {
        boolean accessible = f.canAccess(obj);
        f.setAccessible(true);
        Object objFieldValue;
        try {
            objFieldValue = f.get(obj);
            if (objFieldValue != null) {
                return toBytes(objFieldValue);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } finally {
            f.setAccessible(accessible);
        }
    }

    @Override
    public ByteBuf toBytes(Object value) {
        return context.mapper().toBytes(value);
    }
}
