package cn.iota.jiot.serialization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.iota.jiot.serialization.deserialize.DeserializationContext;
import cn.iota.jiot.serialization.deserialize.FieldDeserializer;
import cn.iota.jiot.serialization.deserialize.ObjectDeserializer;
import cn.iota.jiot.serialization.deserialize.PrimitiveArrayDeserializer;
import cn.iota.jiot.serialization.deserialize.ObjectArrayDeserializer;
import cn.iota.jiot.serialization.deserialize.PrimitiveListDeserializer;
import cn.iota.jiot.serialization.reflect.ReflectionUtil;
import cn.iota.jiot.serialization.serialize.FieldSerializer;
import cn.iota.jiot.serialization.serialize.ObjectArraySerializer;
import cn.iota.jiot.serialization.serialize.ObjectSerializer;
import cn.iota.jiot.serialization.serialize.PrimitiveArraySerializer;
import cn.iota.jiot.serialization.serialize.PrimitiveListSerializer;
import cn.iota.jiot.serialization.serialize.SerializationContext;
import cn.iota.jiot.serialization.util.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class SerializeMapper implements Serializer {

    private <T> void deSerializeSingleFieldValue(T object, Class<?> clazz, Field f, DeserializationContext context,
            Class<? extends FieldDeserializer> deSerClass, Annotation ann, ByteBuf buf) {
        FieldDeserializer deSer = SerializerFactory.createDeserializer(deSerClass);
        if (deSer != null) {
            deSer.setContext(context);
            deSer.setAnnotation(ann);
            deSer.fromBytes(object, clazz, f, buf);
            context.addEntry(new DeserializationContext.DeserializationEntry(f, deSer, buf));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T primitiveFromBytes(ByteBuf buf, Class<?> objClass) {
        Class<? extends FieldDeserializer> deSerClass = SerializerFactory.findDeserializerClass(objClass, null);
        if (deSerClass != null) {
            FieldDeserializer deSer = SerializerFactory.createDeserializer(deSerClass);
            if (deSer != null) {
                T obj = (T) deSer.fromBytes(buf, null);
                return obj;
            } else {
                return null;
            }
        }
        return null;
    }

    private <T> T fromBytes(ByteBuf buf, T object, Class<?> objClass, DeserializationContext context) {
        Class<?> superClass = objClass.getSuperclass();
        if (superClass != Object.class) {
            fromBytes(buf, object, superClass, context);
        }
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(objClass);
        Iterator<Map.Entry<String, Field>> iterator = fields.entrySet().iterator();
        while (iterator.hasNext()) {
            Field f = iterator.next().getValue();
            Class<?> type = f.getType();
            if (ReflectionUtil.isPrimitiveOrWrapper(type) || type == String.class) {
                Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                        SerializerFactory.findStandardAnnotationClass(type));
                Class<? extends FieldDeserializer> deSerClass = SerializerFactory.findDeserializerClass(type,
                        theBestAnn);
                deSerializeSingleFieldValue(object, objClass, f, context, deSerClass, theBestAnn, buf);
            } else if (ReflectionUtil.isPrimitiveArray(type) || ReflectionUtil.isPrimitiveWrapperArray(type)
                    || ReflectionUtil.isStringArray(type)) {
                type = type.getComponentType();
                if (type != null) {
                    Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                            SerializerFactory.findStandardAnnotationClass(type));
                    deSerializeSingleFieldValue(object, objClass, f, context, PrimitiveArrayDeserializer.class,
                            theBestAnn, buf);
                }
            } else if (ReflectionUtil.isPrimitiveOrWrapperList(f)
                    || ReflectionUtil.isStringArray(superClass)) {
                type = ReflectionUtil.getElementType(f);
                if (type != null) {
                    Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                            SerializerFactory.findStandardAnnotationClass(type));
                    deSerializeSingleFieldValue(object, objClass, f, context, PrimitiveListDeserializer.class,
                            theBestAnn, buf);
                }
            } else if (type.isArray()) {
                deSerializeSingleFieldValue(object, objClass, f, context, ObjectArrayDeserializer.class, null, buf);
            } else {
                deSerializeSingleFieldValue(object, objClass, f, context, ObjectDeserializer.class, null, buf);
            }
        }
        return object;
    }

    @Override
    public <T> T fromBytesByClass(ByteBuf buf, Class<T> objClass) {
        if (ReflectionUtil.isPrimitiveOrWrapper(objClass)) {
            return primitiveFromBytes(buf, objClass);
        }
        T object = ReflectionUtil.newInstance(objClass);
        if (object == null) {
            return null;
        }
        DeserializationContext context = new DefaultDeserializationContext(this, object, buf);
        return fromBytes(buf, object, objClass, context);
    }

    @Override
    public <T> T fromBytes(ByteBuf buf, T object) {
        if (object == null) {
            return null;
        }
        Class<?> objClass = object.getClass();
        if (ReflectionUtil.isPrimitiveOrWrapper(objClass)) {
            return primitiveFromBytes(buf, objClass);
        }
        DeserializationContext context = new DefaultDeserializationContext(this, object, buf);
        return fromBytes(buf, object, objClass, context);
    }

    private void serializeSingleFieldValue(Object object, Class<?> clazz, Field f, SerializationContext context,
            Class<? extends FieldSerializer> serClass,
            Annotation ann, ByteBuf buf) {
        FieldSerializer ser = SerializerFactory.createSerializer(serClass);
        if (ser != null) {
            ser.setContext(context);
            ser.setAnnotation(ann);
            ByteBuf fBuf = ser.toBytes(object, clazz, f);
            if (fBuf != null) {
                buf.writeBytes(fBuf);
            }
            context.addEntry(new SerializationContext.SerializationEntry(f, ser, fBuf));
        }
    }

    @Override
    public <T> ByteBuf toBytes(T object) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(128);
        SerializationContext context = new DefaultSerializationContext(this, object, buf);
        Class<?> objClass = object.getClass();
        if (ReflectionUtil.isPrimitiveOrWrapper(objClass)) {
            Class<? extends FieldSerializer> serClass = SerializerFactory.findSerializerClass(objClass, null);
            if (serClass != null) {
                FieldSerializer ser = SerializerFactory.createSerializer(serClass);
                if (ser != null) {
                    return ser.toBytes(object);
                } else {
                    return null;
                }
            }
        }
        return toBytes(object, objClass, context, buf);
    }

    private <T> ByteBuf toBytes(T object, Class<?> objClass, SerializationContext context, ByteBuf buf) {
        Class<?> superClass = objClass.getSuperclass();
        if (superClass != Object.class) {
            toBytes(object, superClass, context, buf);
            // if (cBuf != null) {
            // buf.writeBytes(cBuf);
            // }
        }
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(objClass);
        Iterator<Map.Entry<String, Field>> iterator = fields.entrySet().iterator();
        while (iterator.hasNext()) {
            Field f = iterator.next().getValue();
            Class<?> type = f.getType();
            if (ReflectionUtil.isPrimitiveOrWrapper(type) || type == String.class) {
                Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                        SerializerFactory.findStandardAnnotationClass(type));
                Class<? extends FieldSerializer> serClass = SerializerFactory.findSerializerClass(type, theBestAnn);
                serializeSingleFieldValue(object, objClass, f, context, serClass, theBestAnn, buf);
            } else if (ReflectionUtil.isPrimitiveArray(type) || ReflectionUtil.isPrimitiveWrapperArray(type)
                    || ReflectionUtil.isStringArray(type)) {
                type = type.getComponentType();
                if (type != null) {
                    Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                            SerializerFactory.findStandardAnnotationClass(type));
                    serializeSingleFieldValue(object, objClass, f, context, PrimitiveArraySerializer.class, theBestAnn,
                            buf);
                }
            } else if (ReflectionUtil.isPrimitiveOrWrapperList(f) || ReflectionUtil.isStringList(f)) {
                type = ReflectionUtil.getElementType(f);
                if (type != null) {
                    Annotation theBestAnn = SerializerFactory.findBestSerialAnnotation(f.getAnnotations(),
                            SerializerFactory.findStandardAnnotationClass(type));
                    serializeSingleFieldValue(object, objClass, f, context, PrimitiveListSerializer.class, theBestAnn,
                            buf);
                }
            } else if (type.isArray()) {
                serializeSingleFieldValue(object, objClass, f, context, ObjectArraySerializer.class, null, buf);
            } else {
                serializeSingleFieldValue(object, objClass, f, context, ObjectSerializer.class, null, buf);
            }
        }
        return buf;
    }

    public static class DefaultSerializationContext implements SerializationContext {

        private final SerializeMapper mapper;

        private final Object obj;

        private final ByteBuf buf;

        private List<SerializationEntry> sEntries;

        public DefaultSerializationContext(SerializeMapper mapper, Object obj, ByteBuf buf) {
            this.mapper = mapper;
            this.obj = obj;
            this.buf = buf;
            sEntries = new ArrayList<>(20);
        }

        @Override
        public Object object() {
            return obj;
        }

        @Override
        public ByteBuf buffer() {
            return buf;
        }

        @Override
        public SerializationEntry lastEntry() {
            if (sEntries.size() > 0) {
                return sEntries.get(sEntries.size() - 1);
            } else {
                return null;
            }
        }

        @Override
        public List<SerializationEntry> entries() {
            return sEntries;
        }

        @Override
        public void addEntry(SerializationEntry entry) {
            if (entry != null) {
                sEntries.add(entry);
            }
        }

        @Override
        public SerializeMapper mapper() {
            return mapper;
        }
    }

    public static class DefaultDeserializationContext implements DeserializationContext {
        private final SerializeMapper mapper;

        private final Object obj;

        private final ByteBuf buf;

        private List<DeserializationEntry> sEntries;

        public DefaultDeserializationContext(SerializeMapper mapper, Object obj, ByteBuf buf) {
            this.mapper = mapper;
            this.obj = obj;
            this.buf = buf;
            sEntries = new ArrayList<>(20);
        }

        @Override
        public Object object() {
            return obj;
        }

        @Override
        public ByteBuf buffer() {
            return buf;
        }

        @Override
        public DeserializationEntry lastEntry() {
            if (sEntries.size() > 0) {
                return sEntries.get(sEntries.size() - 1);
            } else {
                return null;
            }
        }

        @Override
        public List<DeserializationEntry> entries() {
            return sEntries;
        }

        @Override
        public void addEntry(DeserializationEntry entry) {
            if (entry != null) {
                sEntries.add(entry);
            }
        }

        @Override
        public SerializeMapper mapper() {
            return mapper;
        }
    }
}
