package cn.iota.jiot.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.mock.BitAndByteClass;
import cn.iota.jiot.serialization.mock.BitClass;
import cn.iota.jiot.serialization.mock.ByteArrayClass;
import cn.iota.jiot.serialization.mock.ByteClass;
import cn.iota.jiot.serialization.mock.ByteListClass;
import cn.iota.jiot.serialization.mock.ShortArrayClass;
import cn.iota.jiot.serialization.mock.ShortClass;
import cn.iota.jiot.serialization.mock.StringArrayClass;
import cn.iota.jiot.serialization.mock.StringClass;
import cn.iota.jiot.serialization.mock.StringListClass;
import cn.iota.jiot.serialization.mock.SubByteClass;
import io.netty.buffer.ByteBuf;

public class SerializerMapperTest {

    private static SerializeMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new SerializeMapper();
    }

    @Test
    void simpleByteTest() {
        byte b = 10;

        ByteBuf buf = mapper.toBytes(b);
        assertEquals(1, buf.readableBytes());
        assertEquals(10, buf.getByte(0));

        byte deSer = mapper.fromBytesByClass(buf, byte.class);
        assertEquals(10, deSer);

        buf = mapper.toBytes((byte) 20);
        assertEquals(1, buf.readableBytes());
        assertEquals(20, buf.readByte());
    }

    @Test
    void simpleByteClassTest() {
        ByteClass bClass = new ByteClass();

        ByteBuf buf = mapper.toBytes(bClass);
        assertEquals(6, buf.readableBytes());

        ByteClass deClass = mapper.fromBytesByClass(buf, ByteClass.class);
        assertNotNull(deClass);

        assertTrue(bClass.equals(deClass));
    }

    @Test
    void simpleSubByteClassTest() {
        ByteClass bClass = new SubByteClass();
        bClass.setAnnIntegerB((byte) 15);

        ByteBuf buf = mapper.toBytes(bClass);
        assertEquals(6, buf.readableBytes());

        ByteClass deClass = mapper.fromBytesByClass(buf, ByteClass.class);
        assertNotNull(deClass);

        assertFalse(bClass.equals(deClass));

        buf.resetReaderIndex();

        ByteClass deSubClass = mapper.fromBytesByClass(buf, SubByteClass.class);
        assertNotNull(deSubClass);

        assertTrue(bClass.equals(deSubClass));
    }

    @Test
    void simpleBitClassTest() {
        BitClass bClass = new BitClass();

        ByteBuf buf = mapper.toBytes(bClass);
        assertEquals(1, buf.readableBytes());
    }

    @Test
    void simpleShortClassTest() {
        ShortClass sClass = new ShortClass();

        ByteBuf buf = mapper.toBytes(sClass);
        assertEquals(10, buf.readableBytes());

        ShortClass deClass = mapper.fromBytesByClass(buf, ShortClass.class);
        assertNotNull(deClass);

        assertTrue(sClass.equals(deClass));
    }

    @Test
    void bitAndByteClassTest() {
        BitAndByteClass bitAndByteClass = new BitAndByteClass();

        ByteBuf buf = mapper.toBytes(bitAndByteClass);
        assertEquals(7, buf.readableBytes());
        byte b = buf.readByte();
        assertEquals(-64, b);
    }

    @Test
    void byteArrayClassTest() {
        ByteArrayClass byteArray = new ByteArrayClass();

        ByteBuf buf = mapper.toBytes(byteArray);
        assertEquals(3, buf.readableBytes());
        byte b = buf.readByte();
        assertEquals(1, b);
        b = buf.readByte();
        assertEquals(2, b);

        buf.setByte(0, 10);
        buf.setByte(1, 11);
        buf.setByte(2, 12);
        buf.resetReaderIndex();

        ByteArrayClass dByteArrayClass = mapper.fromBytesByClass(buf, ByteArrayClass.class);
        assertNotNull(dByteArrayClass);
        assertEquals((byte) 10, dByteArrayClass.getBytes()[0]);
    }

    @Test
    void shortArrayClassTest() {
        ShortArrayClass byteArray = new ShortArrayClass();

        ByteBuf buf = mapper.toBytes(byteArray);
        assertEquals(6, buf.readableBytes());
        short b = buf.readShort();
        assertEquals(1, b);
        b = buf.readShort();
        assertEquals(2, b);

        buf.setShort(0, 10);
        buf.setShort(2, 11);
        buf.setShort(4, 12);
        buf.resetReaderIndex();

        ShortArrayClass dShortArrayClass = mapper.fromBytesByClass(buf, ShortArrayClass.class);
        assertNotNull(dShortArrayClass);
        assertEquals((byte) 10, dShortArrayClass.getShorts()[0]);
    }

    @Test
    void byteListClassTest() {
        ByteListClass byteList = new ByteListClass();

        ByteBuf buf = mapper.toBytes(byteList);
        assertEquals(3, buf.readableBytes());
        byte b = buf.readByte();
        assertEquals(1, b);
        b = buf.readByte();
        assertEquals(2, b);

        buf.setByte(0, 10);
        buf.setByte(1, 11);
        buf.setByte(2, 12);
        buf.resetReaderIndex();

        ByteListClass dByteListClass = mapper.fromBytesByClass(buf, ByteListClass.class);
        assertNotNull(dByteListClass);
        assertEquals((byte) 10, dByteListClass.getBytes().get(0));
    }

    @Test
    void stringClassTest() {
        StringClass s = new StringClass();
        s.setName("我是UTF-8");

        ByteBuf buf = new SerializeMapper().toBytes(s);

        assertNotNull(buf);
        assertEquals(20, buf.readableBytes());
    }

    @Test
    void stringArrayClassTest() {
        StringArrayClass s = new StringArrayClass();
        s.setNames(new String[] { "我是UTF-8", "我是UTF-81" });

        ByteBuf buf = new SerializeMapper().toBytes(s);

        assertNotNull(buf);

        assertEquals(40, buf.readableBytes());

        StringArrayClass dS = new SerializeMapper().fromBytesByClass(buf, StringArrayClass.class);
        assertEquals(s, dS);
    }

    @Test
    void stringListClassTest() {
        StringListClass s = new StringListClass();

        ByteBuf buf = new SerializeMapper().toBytes(s);

        assertNotNull(buf);

        assertEquals(30, buf.readableBytes());
        StringListClass dS = new SerializeMapper().fromBytesByClass(buf, StringListClass.class);
        assertEquals(s, dS);
    }
}
