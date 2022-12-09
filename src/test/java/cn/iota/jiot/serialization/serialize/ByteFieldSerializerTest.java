package cn.iota.jiot.serialization.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.mock.ByteClass;
import io.netty.buffer.ByteBuf;

public class ByteFieldSerializerTest {

    private static FieldSerializer SER = new ByteSerializer();

    @Test
    void byteTest() throws NoSuchFieldException, SecurityException {
        ByteClass byteClass = new ByteClass();
        ByteBuf buf = SER.toBytes(byteClass, ByteClass.class.getDeclaredField("classB"));
        assertNotNull(buf);
        assertEquals(10, buf.getByte(0));
        buf = SER.toBytes(byteClass, ByteClass.class.getDeclaredField("rawB"));
        assertNotNull(buf);
        assertEquals(20, buf.getByte(0));
        FieldSerializer intSer = new IntegerTypeFieldSerializer();
        buf = intSer.toBytes(byteClass, ByteClass.class.getDeclaredField("annIntegerB"));
        assertNotNull(buf);
        assertEquals(2, buf.readableBytes());
        assertEquals(30, buf.getByte(1));
        intSer = new IntegerTypeFieldSerializer();
        buf = intSer.toBytes(byteClass, ByteClass.class.getDeclaredField("annIntegerBLE"));
        assertNotNull(buf);
        assertEquals(2, buf.readableBytes());
        assertEquals(30, buf.getByte(0));
    }
}
