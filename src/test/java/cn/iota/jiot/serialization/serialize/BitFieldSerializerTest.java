package cn.iota.jiot.serialization.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.SerializeMapper.DefaultSerializationContext;
import cn.iota.jiot.serialization.meta.SerializeBitField;
import cn.iota.jiot.serialization.mock.BitClass;
import cn.iota.jiot.serialization.serialize.SerializationContext.SerializationEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class BitFieldSerializerTest {

    @Test
    void bitTest() throws NoSuchFieldException, SecurityException {
        BitClass bitClass = new BitClass();
        BitSerializer ser = new BitSerializer();
        ByteBuf buf = ser.toBytes(bitClass, BitClass.class.getDeclaredField("bit7"));
        assertNotNull(buf);
        assertEquals(-128, buf.getByte(0));
        ser = new BitSerializer();
        buf = ser.toBytes(bitClass, BitClass.class.getDeclaredField("bit6"));
        assertNotNull(buf);
        assertEquals(64, buf.getByte(0));
    }

    @Test
    void bitWithContextTest() throws NoSuchFieldException, SecurityException {
        BitClass bitClass = new BitClass();
        BitSerializer ser7 = new BitSerializer();
        BitSerializer ser6 = new BitSerializer();
        Field f7 = BitClass.class.getDeclaredField("bit7");
        Field f6 = BitClass.class.getDeclaredField("bit6");
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1);
        buf.writeByte(1);
        SerializationContext context = new DefaultSerializationContext(null, bitClass, buf);
        ser7.setContext(context);
        ser7.setAnnotation(f7.getAnnotation(SerializeBitField.class));
        // ser7.toBytes(bitClass, f7);
        context.addEntry(new SerializationEntry(f7, ser7, buf));
        ser6.setContext(context);
        ser6.setAnnotation(f6.getAnnotation(SerializeBitField.class));
        ByteBuf nullBuf = ser6.toBytes(bitClass, BitClass.class.getDeclaredField("bit6"));
        assertNull(nullBuf);
        assertEquals(65, buf.getByte(0));
    }
}
