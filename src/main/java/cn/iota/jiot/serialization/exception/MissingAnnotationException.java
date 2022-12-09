package cn.iota.jiot.serialization.exception;

import java.lang.annotation.Annotation;

import cn.iota.jiot.serialization.meta.SerializeProcessor;

public class MissingAnnotationException extends SerializeException {

    private Annotation _requiredAnnotation;

    public MissingAnnotationException(Class<? extends Annotation> requiredAnnotation, SerializeProcessor processor) {
        super("Missing Annotation: " + requiredAnnotation.getName(), processor, null);
    }

    public Annotation get_requiredAnnotation() {
        return _requiredAnnotation;
    }
}
