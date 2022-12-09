package cn.iota.jiot.serialization.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.iota.jiot.serialization.deserialize.BitDeserializer;
import cn.iota.jiot.serialization.deserialize.ByteDeserializer;
import cn.iota.jiot.serialization.deserialize.FieldDeserializer;
import cn.iota.jiot.serialization.deserialize.IntDeserializer;
import cn.iota.jiot.serialization.deserialize.IntegerTypeFieldDeserializer;
import cn.iota.jiot.serialization.deserialize.LongDeserializer;
import cn.iota.jiot.serialization.deserialize.ShortDeserializer;
import cn.iota.jiot.serialization.deserialize.StringDeserializer;
import cn.iota.jiot.serialization.meta.SerializeBCDCodeField;
import cn.iota.jiot.serialization.meta.SerializeBitField;
import cn.iota.jiot.serialization.meta.SerializeIntegerField;
import cn.iota.jiot.serialization.meta.SerializeStringField;
import cn.iota.jiot.serialization.reflect.ReflectionUtil;
import cn.iota.jiot.serialization.serialize.BCDCodeSerializer;
import cn.iota.jiot.serialization.serialize.BitSerializer;
import cn.iota.jiot.serialization.serialize.ByteSerializer;
import cn.iota.jiot.serialization.serialize.FieldSerializer;
import cn.iota.jiot.serialization.serialize.IntSerializer;
import cn.iota.jiot.serialization.serialize.IntegerTypeFieldSerializer;
import cn.iota.jiot.serialization.serialize.LongSerializer;
import cn.iota.jiot.serialization.serialize.ShortSerializer;
import cn.iota.jiot.serialization.serialize.StringSerializer;

public class SerializerFactory {

    /**
     * 原始字段类型对应的默认Serializer
     */
    public static HashMap<Class<?>, Class<? extends FieldSerializer>> RAW_FIELD_SERIALIZER = new HashMap<>();

    /**
     * Integer(byte/short/int/long)支持的扩展注解列表
     */
    public static List<Class<? extends Annotation>> INTEGER_FIELD_STANDARD_ANNOTATIONS = new ArrayList<>();

    public static List<Class<? extends Annotation>> STRING_FIELD_STANDARD_ANNOTATIONS = new ArrayList<>();

    /**
     * 
     */
    public static HashMap<Class<?>, List<Class<? extends Annotation>>> RAW_TYPE_STANDARD_ANNOTATIONS = new HashMap<>();

    /**
     * 注解对应的Serializer
     */
    public static HashMap<Class<? extends Annotation>, Class<? extends FieldSerializer>> ANNOTATION_SERIALIZER = new HashMap<>();

    /**
     * 原始字段类型对应的默认Deserializer
     */
    public static HashMap<Class<?>, Class<? extends FieldDeserializer>> RAW_FIELD_DESERIALIZER = new HashMap<>();

    /**
     * 注解对应的Serializer
     */
    public static HashMap<Class<? extends Annotation>, Class<? extends FieldDeserializer>> ANNOTATION_DESERIALIZER = new HashMap<>();

    static {

        /**
         * 初始化Integer(byte/short/int/long)支持的扩展注解列表
         */
        INTEGER_FIELD_STANDARD_ANNOTATIONS.add(SerializeBCDCodeField.class);
        INTEGER_FIELD_STANDARD_ANNOTATIONS.add(SerializeBitField.class);
        INTEGER_FIELD_STANDARD_ANNOTATIONS.add(SerializeIntegerField.class);

        STRING_FIELD_STANDARD_ANNOTATIONS.add(SerializeStringField.class);

        /**
         * 
         */
        RAW_TYPE_STANDARD_ANNOTATIONS.put(byte.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(Byte.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(int.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(Integer.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(short.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(Short.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(long.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);
        RAW_TYPE_STANDARD_ANNOTATIONS.put(Long.class, INTEGER_FIELD_STANDARD_ANNOTATIONS);

        RAW_TYPE_STANDARD_ANNOTATIONS.put(String.class, STRING_FIELD_STANDARD_ANNOTATIONS);

        /**
         * 初始化原始类型对应的Serializer列表
         */
        RAW_FIELD_SERIALIZER.put(byte.class, ByteSerializer.class);
        RAW_FIELD_SERIALIZER.put(Byte.class, ByteSerializer.class);
        RAW_FIELD_SERIALIZER.put(short.class, ShortSerializer.class);
        RAW_FIELD_SERIALIZER.put(Short.class, ShortSerializer.class);
        RAW_FIELD_SERIALIZER.put(int.class, IntSerializer.class);
        RAW_FIELD_SERIALIZER.put(Integer.class, IntSerializer.class);
        RAW_FIELD_SERIALIZER.put(long.class, LongSerializer.class);
        RAW_FIELD_SERIALIZER.put(Long.class, LongSerializer.class);
        RAW_FIELD_SERIALIZER.put(String.class, StringSerializer.class);

        /**
         * 
         */
        ANNOTATION_SERIALIZER.put(SerializeIntegerField.class, IntegerTypeFieldSerializer.class);
        ANNOTATION_SERIALIZER.put(SerializeBCDCodeField.class, BCDCodeSerializer.class);
        ANNOTATION_SERIALIZER.put(SerializeBitField.class, BitSerializer.class);
        ANNOTATION_SERIALIZER.put(SerializeStringField.class, StringSerializer.class);

        /**
         * 
         */
        RAW_FIELD_DESERIALIZER.put(byte.class, ByteDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(Byte.class, ByteDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(short.class, ShortDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(Short.class, ShortDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(int.class, IntDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(Integer.class, IntDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(long.class, LongDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(Long.class, LongDeserializer.class);
        RAW_FIELD_DESERIALIZER.put(String.class, StringDeserializer.class);

        /**
         * 
         */
        ANNOTATION_DESERIALIZER.put(SerializeIntegerField.class, IntegerTypeFieldDeserializer.class);
        ANNOTATION_DESERIALIZER.put(SerializeBitField.class, BitDeserializer.class);
        ANNOTATION_DESERIALIZER.put(SerializeStringField.class, StringDeserializer.class);
    }

    public static List<Class<? extends Annotation>> findStandardAnnotationClass(Class<?> type) {
        return RAW_TYPE_STANDARD_ANNOTATIONS.get(type);
    }

    public static Annotation findBestSerialAnnotation(Annotation[] candidates,
            List<Class<? extends Annotation>> standards) {
        if (standards == null) {
            return null;
        }
        for (Annotation ann : candidates) {
            if (standards.contains(ann.annotationType())) {
                return ann;
            }
        }
        return null;
    }

    public static Class<? extends FieldSerializer> findSerializerClass(Class<? extends Annotation> theBestAnn) {
        Class<? extends FieldSerializer> serClass = null;
        if (theBestAnn != null) {
            serClass = ANNOTATION_SERIALIZER.get(theBestAnn);
        }
        return serClass;
    }

    public static Class<? extends FieldSerializer> findSerializerClass(Field field) {
        Class<? extends FieldSerializer> serClass = null;
        Class<?> type = field.getType();
        List<Class<? extends Annotation>> standards = RAW_TYPE_STANDARD_ANNOTATIONS.get(type);
        Annotation theBestAnn = findBestSerialAnnotation(field.getAnnotations(), standards);
        serClass = findSerializerClass(theBestAnn.getClass());
        if (serClass == null) {
            serClass = RAW_FIELD_SERIALIZER.get(type);
        }
        return serClass;
    }

    public static Class<? extends FieldSerializer> findSerializerClass(Class<?> type, Annotation theBestAnn) {
        Class<? extends FieldSerializer> serClass = null;
        if (theBestAnn != null) {
            serClass = ANNOTATION_SERIALIZER.get(theBestAnn.annotationType());
        }
        if (serClass == null) {
            serClass = RAW_FIELD_SERIALIZER.get(type);
        }
        return serClass;
    }

    public static Class<? extends FieldSerializer> findSerializerClass(Field field, Annotation theBestAnn) {
        Class<?> type = field.getType();
        return findSerializerClass(type, theBestAnn);
    }

    public static FieldSerializer createSerializer(Class<? extends FieldSerializer> serClazz) {
        return ReflectionUtil.newInstance(serClazz);
    }

    public static FieldDeserializer createDeserializer(Class<? extends FieldDeserializer> deSerClazz) {
        return ReflectionUtil.newInstance(deSerClazz);
    }

    public static Class<? extends FieldDeserializer> findDeserializerClass(Class<?> type, Annotation theBestAnn) {
        Class<? extends FieldDeserializer> serClass = null;
        if (theBestAnn != null) {
            serClass = ANNOTATION_DESERIALIZER.get(theBestAnn.annotationType());
        }
        if (serClass == null) {
            serClass = RAW_FIELD_DESERIALIZER.get(type);
        }
        return serClass;
    }

    public static Class<? extends FieldDeserializer> findDeserializerClass(Class<? extends Annotation> theBestAnn) {
        Class<? extends FieldDeserializer> serClass = null;
        if (theBestAnn != null) {
            serClass = ANNOTATION_DESERIALIZER.get(theBestAnn);
        }
        return serClass;
    }
}
