package cn.iota.jiot.serialization.deserialize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.SerializeMapper.DefaultDeserializationContext;
import cn.iota.jiot.serialization.deserialize.DeserializationContext.DeserializationEntry;
import cn.iota.jiot.serialization.meta.SerializeBitField;
import cn.iota.jiot.serialization.mock.BitClass;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class BitFieldDeserializerTest {
    @Test
    void bitTest() throws NoSuchFieldException, SecurityException {
        BitClass bitClass = new BitClass();
        BitDeserializer deSer = new BitDeserializer();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1);
        buf.writeByte(64);

        deSer.fromBytes(bitClass, BitClass.class.getDeclaredField("bit7"), buf);
        assertEquals(0, bitClass.getBit7());

        deSer.fromBytes(bitClass, BitClass.class.getDeclaredField("bit6"), buf);
        assertEquals(0, bitClass.getBit6());
    }

    @Test
    void bitWithContextTest() throws NoSuchFieldException, SecurityException {
        BitClass bitClass = new BitClass();
        BitDeserializer deSer7 = new BitDeserializer();
        BitDeserializer deSer6 = new BitDeserializer();
        Field f7 = BitClass.class.getDeclaredField("bit7");
        Field f6 = BitClass.class.getDeclaredField("bit6");
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1);
        buf.writeByte(1);
        DeserializationContext context = new DefaultDeserializationContext(null, bitClass, buf);
        deSer7.setContext(context);
        deSer7.setAnnotation(f7.getAnnotation(SerializeBitField.class));
        // ser7.toBytes(bitClass, f7);
        context.addEntry(new DeserializationEntry(f7, deSer7, buf));
        deSer6.setContext(context);
        deSer6.setAnnotation(f6.getAnnotation(SerializeBitField.class));
        deSer6.fromBytes(bitClass, BitClass.class.getDeclaredField("bit6"), buf);
        assertEquals(0, bitClass.getBit6());
        buf.resetReaderIndex();
        buf.setByte(0, 0b01000000);
        deSer6.fromBytes(bitClass, BitClass.class.getDeclaredField("bit6"), buf);
        assertEquals(1, bitClass.getBit6());
    }
}
