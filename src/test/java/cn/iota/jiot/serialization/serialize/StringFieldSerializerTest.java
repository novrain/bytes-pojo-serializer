package cn.iota.jiot.serialization.serialize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.deserialize.StringDeserializer;
import cn.iota.jiot.serialization.mock.StringClass;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class StringFieldSerializerTest {
    @Test
    void stringFieldTest() throws NoSuchFieldException, SecurityException {
        StringClass s = new StringClass();
        s.setName("我是UTF-8");

        ByteBuf buf = new StringSerializer().toBytes(s, StringClass.class.getDeclaredField("name"));

        assertNotNull(buf);

        System.out.println(s.getName());
        System.out.println(buf.toString(CharsetUtil.UTF_8));
        assertEquals(20, buf.readableBytes());

        StringClass dS = new StringClass();

        new StringDeserializer().fromBytes(dS, StringClass.class.getDeclaredField("name"), buf);

        assertEquals(s.getName(), dS.getName());
    }

}
