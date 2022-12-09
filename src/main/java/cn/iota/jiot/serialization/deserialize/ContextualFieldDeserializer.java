package cn.iota.jiot.serialization.deserialize;

public abstract class ContextualFieldDeserializer extends AbstractFieldDeserializer {

    protected DeserializationContext context;

    @Override
    public void setContext(DeserializationContext context) {
        this.context = context;
    }
}
