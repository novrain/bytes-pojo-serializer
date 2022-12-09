package cn.iota.jiot.serialization.exception;

import java.lang.annotation.Annotation;

import cn.iota.jiot.serialization.meta.SerializeProcessor;

public abstract class SerializeException extends Exception {

    private SerializeProcessor _processor;
    private Annotation _annotation;

    protected SerializeException(String msg, SerializeProcessor processor, Annotation annotation) {
        super(msg);
        _processor = processor;
        _annotation = annotation;
    }

    protected SerializeException(Throwable t, SerializeProcessor processor, Annotation annotation) {
        super(t);
        _processor = processor;
        _annotation = annotation;
    }

    protected SerializeException(String msg, Throwable rootCause, SerializeProcessor processor, Annotation annotation) {
        super(msg, rootCause);
    }

    public SerializeProcessor getProcessor() {
        return _processor;
    }

    public Annotation getAnnotation() {
        return _annotation;
    }
}
