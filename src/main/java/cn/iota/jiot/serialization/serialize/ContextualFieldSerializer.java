package cn.iota.jiot.serialization.serialize;

public abstract class ContextualFieldSerializer extends AbstractFieldSerializer {

    protected SerializationContext context;

    @Override
    public void setContext(SerializationContext context) {
        this.context = context;
    }
}
