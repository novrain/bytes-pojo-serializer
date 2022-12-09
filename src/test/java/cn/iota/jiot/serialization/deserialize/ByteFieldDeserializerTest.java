package cn.iota.jiot.serialization.deserialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.mock.ByteClass;
import cn.iota.jiot.serialization.serialize.ByteSerializer;
import cn.iota.jiot.serialization.serialize.FieldSerializer;
import cn.iota.jiot.serialization.serialize.IntegerTypeFieldSerializer;
import io.netty.buffer.ByteBuf;

public class ByteFieldDeserializerTest {

    private static FieldSerializer SER = new ByteSerializer();
    private static FieldDeserializer DE_SER = new ByteDeserializer();

    @Test
    void byteTest() throws NoSuchFieldException, SecurityException {
        ByteClass byteClass = new ByteClass();
        ByteClass deSerByteClass = new ByteClass();
        deSerByteClass.setClassB((byte) 1);
        deSerByteClass.setRawB((byte) 1);
        deSerByteClass.setAnnIntegerB((byte) 1);
        deSerByteClass.setAnnIntegerBLE((byte) 1);

        ByteBuf buf = SER.toBytes(byteClass, ByteClass.class.getDeclaredField("classB"));
        assertNotNull(buf);
        assertEquals(10, buf.getByte(0));

        DE_SER.fromBytes(deSerByteClass, ByteClass.class.getDeclaredField("classB"), buf);
        assertEquals((byte)10, deSerByteClass.getClassB());
        
        buf = SER.toBytes(byteClass, ByteClass.class.getDeclaredField("rawB"));
        assertNotNull(buf);
        assertEquals(20, buf.getByte(0));

        DE_SER.fromBytes(deSerByteClass, ByteClass.class.getDeclaredField("rawB"), buf);
        assertEquals((byte)20, deSerByteClass.getRawB());

        FieldSerializer intSer = new IntegerTypeFieldSerializer();
        FieldDeserializer intDeSer = new IntegerTypeFieldDeserializer();
        buf = intSer.toBytes(byteClass, ByteClass.class.getDeclaredField("annIntegerB"));
        assertNotNull(buf);
        assertEquals(2, buf.readableBytes());
        assertEquals(30, buf.getByte(1));

        intDeSer.fromBytes(deSerByteClass, ByteClass.class.getDeclaredField("annIntegerB"), buf);
        assertEquals(30, deSerByteClass.getAnnIntegerB());

        intSer = new IntegerTypeFieldSerializer();
        intDeSer = new IntegerTypeFieldDeserializer();
        buf = intSer.toBytes(byteClass, ByteClass.class.getDeclaredField("annIntegerBLE"));
        assertNotNull(buf);
        assertEquals(2, buf.readableBytes());
        assertEquals(30, buf.getByte(0));


        intDeSer.fromBytes(deSerByteClass, ByteClass.class.getDeclaredField("annIntegerBLE"), buf);
        assertEquals(30, deSerByteClass.getAnnIntegerB());
    }
}
