package cn.iota.jiot.serialization.serialize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cn.iota.jiot.serialization.meta.SerializeProcessor;
import io.netty.buffer.ByteBuf;

public interface FieldSerializer extends SerializeProcessor {

   default ByteBuf toBytes(Object obj, Class<?> clazz, Field field) {
      return toBytes(obj, field);
   }

   ByteBuf toBytes(Object obj, Field field);

   ByteBuf toBytes(Object value);

   default void setContext(SerializationContext context) {
      // don't care about the context
   }

   default void setAnnotation(Annotation annotation) {
   }

   default Annotation getAnnotation() {
      return null;
   }
}
