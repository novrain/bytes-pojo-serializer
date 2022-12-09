package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;

import cn.iota.jiot.serialization.reflect.ReflectionUtil;
import io.netty.buffer.ByteBuf;

public class ObjectDeserializer extends ContextualFieldDeserializer {

    @Override
    public void fromBytes(Object obj, Field f, ByteBuf buf) {
        boolean accessible = f.canAccess(obj);
        f.setAccessible(true);
        Object objFieldValue;
        try {
            objFieldValue = f.get(obj);
            if (objFieldValue == null) {
                objFieldValue = ReflectionUtil.newInstance(f.getType());
            }
            fromBytes(buf, objFieldValue);
            f.set(obj, objFieldValue);
        } catch (IllegalArgumentException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        } finally {
            f.setAccessible(accessible);
        }
    }

    @Override
    public Object fromBytes(ByteBuf buf, Object obj) {
        return context.mapper().fromBytes(buf, obj);
    }

}
