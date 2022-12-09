package cn.iota.jiot.serialization.deserialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.SerializeProcessor;
import io.netty.buffer.ByteBuf;

public interface FieldDeserializer extends SerializeProcessor {
    default void fromBytes(Object obj, Class<?> clazz, Field field, ByteBuf buf) {
        fromBytes(obj, field, buf);
    };

    void fromBytes(Object obj, Field field, ByteBuf buf);

    Object fromBytes(ByteBuf buf, Object obj);

    default void setContext(DeserializationContext context) {

    }

    default Annotation getAnnotation() {
        return null;
    }

    default void setAnnotation(Annotation annotation) {
    }
}
