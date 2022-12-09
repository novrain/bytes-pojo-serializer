package cn.iota.jiot.serialization.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@SerializeAnnotation
public @interface SerializeStringField {
    int length() default 0;

    String refField() default "";

    char pad() default '\0';

    PadPosition padPosition() default PadPosition.RIGHT;

    String charset() default "UTF-8";
}
