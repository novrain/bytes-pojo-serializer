package cn.iota.jiot.serialization.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@SerializeAnnotation
public @interface SerializeIntegerField {

    /**
     * Byte order
     * @return
     */
    ByteOrder order() default ByteOrder.BIG_ENDIAN;
    /**
     * size in bytes
     * 
     * @return
     */
    byte size();

    /**
     * 是否无符号
     * 
     * @return
     */
    boolean unsigned() default false;
}
