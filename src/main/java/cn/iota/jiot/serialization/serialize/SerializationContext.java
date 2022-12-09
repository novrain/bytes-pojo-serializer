package cn.iota.jiot.serialization.serialize;

import java.lang.reflect.Field;
import java.util.List;

import cn.iota.jiot.serialization.SerializeMapper;
import io.netty.buffer.ByteBuf;

public interface SerializationContext {

    SerializeMapper mapper();

    Object object();

    ByteBuf buffer();

    SerializationEntry lastEntry();

    List<SerializationEntry> entries();

    void addEntry(SerializationEntry entry);

    public final class SerializationEntry {

        private final Field field;

        private final FieldSerializer fieldSerializer;

        private final ByteBuf resultBuf;

        public SerializationEntry(Field field, FieldSerializer fieldSerializer,
                ByteBuf resultBuf) {
            this.field = field;
            this.fieldSerializer = fieldSerializer;
            this.resultBuf = resultBuf;
        }

        public Field getField() {
            return field;
        }

        public FieldSerializer getFieldSerializer() {
            return fieldSerializer;
        }

        public ByteBuf getResultBuf() {
            return resultBuf;
        }

        
    }
}
