package cn.iota.jiot.serialization.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.mock.ShortClass;
import io.netty.buffer.ByteBuf;

public class ShortFieldSerializerTest {

    private static FieldSerializer SER = new ShortSerializer();

    @Test
    void shortTest() throws NoSuchFieldException, SecurityException {
        ShortClass shortClass = new ShortClass();
        ByteBuf buf = SER.toBytes(shortClass, ShortClass.class.getDeclaredField("classS"));
        assertNotNull(buf);
        assertEquals(10, buf.getShort(0));
        buf = SER.toBytes(shortClass, ShortClass.class.getDeclaredField("rawS"));
        assertNotNull(buf);
        assertEquals(20, buf.getShort(0));
        FieldSerializer intSer = new IntegerTypeFieldSerializer();
        buf = intSer.toBytes(shortClass, ShortClass.class.getDeclaredField("annIntegerS"));
        assertNotNull(buf);
        assertEquals(3, buf.readableBytes());
        assertEquals(-256, buf.getShort(1));
        intSer = new IntegerTypeFieldSerializer();
        buf = intSer.toBytes(shortClass, ShortClass.class.getDeclaredField("annIntegerSLE"));
        assertNotNull(buf);
        assertEquals(3, buf.readableBytes());
        assertEquals(1234, buf.getShortLE(0));
    }
}
