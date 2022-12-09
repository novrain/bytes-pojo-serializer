package cn.iota.jiot.serialization.deserialize;

import java.lang.reflect.Field;
import java.util.List;

import cn.iota.jiot.serialization.SerializeMapper;
import io.netty.buffer.ByteBuf;

public interface DeserializationContext {

    SerializeMapper mapper();

    Object object();

    ByteBuf buffer();

    DeserializationEntry lastEntry();

    List<DeserializationEntry> entries();

    void addEntry(DeserializationEntry entry);

    public final class DeserializationEntry {
        private final Field field;

        private final FieldDeserializer fieldSerializer;

        private ByteBuf sourceBuf;

        public DeserializationEntry(Field field, FieldDeserializer fieldSerializer, ByteBuf sourceBuf) {
            this.field = field;
            this.fieldSerializer = fieldSerializer;
            this.sourceBuf = sourceBuf;
        }

        public Field getField() {
            return field;
        }

        public FieldDeserializer getFieldSerializer() {
            return fieldSerializer;
        }

        public ByteBuf getSourceBuf() {
            return sourceBuf;
        }
    }
}
