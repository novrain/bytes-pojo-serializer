package cn.iota.jiot.serialization.serialize;

import java.lang.annotation.Annotation;

public abstract class AbstractFieldSerializer implements FieldSerializer {

    protected Annotation annotation;

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
