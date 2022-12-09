package cn.iota.jiot.serialization.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SerializeAnnotation
public @interface SerializeFieldOrder {

    /**
     * 
     * @return
     */
    public String[] value() default {};

}
