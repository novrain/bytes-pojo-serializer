package cn.iota.jiot.serialization.deserialize;

import java.lang.annotation.Annotation;

public abstract class AbstractFieldDeserializer implements FieldDeserializer {

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
