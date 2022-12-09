package cn.iota.jiot.serialization.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@SerializeAnnotation
public @interface SerializeField {
    /**
     * order
     * 
     * @return
     */
    short index() default -1;
}
