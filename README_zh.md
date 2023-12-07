# 通过注解实现的Java序列化反序列化工具

[英文](https://github.com/novrain/bytes-pojo-serializer/blob/master/README.md)

通过注解将POJO对象序列化为字节流，或从字节流中反序列化为对象实例。

使用Netty的ByteBuf，便于与Netty集成。

## SerializeMapper

默认实现，下面是两个例子，更多的例子请查看测试用例。

### 简单的基本类型

```Java
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
}
```

### 对象实例

对象:

```Java
public class ShortClass {
    @SerializeField
    Short classS = 10;
    @SerializeField
    short rawS = 20;

    @SerializeField
    @SerializeIntegerField(size = 3)
    short annIntegerS = -256;
    @SerializeField
    @SerializeIntegerField(size = 3, order = ByteOrder.LITTLE_ENDIAN)
    short annIntegerSLE = 1234;

    // ... other getter/setter, hash and equal functions to help build tests.
}
```

字段序列化测试用例:

```Java
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
```

对象序列化/反序列化用例:

```java
public class SerializerMapperTest {

    private static SerializeMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new SerializeMapper();
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
}
```

## 支持的数据类型和注解

SerializeMapper有些默认的规则，配合注解完成序列化和反序列化的工作:

* 整型和字符串及其包装类: 如果没有注解说明，将按照默认规则自动处理。
  * 类型决定字节流里的长度，比如short占两个字节.
  * 默认字节序为BIG_ENDIAN.
* 整型和字符串及其包装类的数组或List: 如果没有注解说明，序列化将按照默认规则自动处理, 通过反射获取其长度信息; 反序列化需要通过注解，比如```SerializeArrayOrListLength```, 来告知长度.
* 对象字段: 将被嵌套递归处理.

### 注解

* SerializeField：基本注解，用来描述字段是否需要处理，并包含一些通用的信息，目前只有```index```，用于排序.
* SerializeBitField: 这个注解可以从字节流中读出一个bit，```end```用来说明是否需要跳转到下个字节.
* SerializeByteField: byte类型，支持有符号/无符号.
* SerializeIntField: int类型，支持有符号/无符号，字节序.
* SerializeShortField: short类型，与```SerializeIntField```类似.
* SerializeLongField: long类型，与```SerializeIntField```和```SerializeShortField```类似.
* SerializeIntegerField: 可以指定字节流长度的整型数据.
* SerializeStringField: 字符串类型, 支持补齐，charset set，设置或通过 ```refField```字段来指定长度，从而支持Length-Value的字节流数据.

### 其他辅助的注解

* SerializeFieldOrder: 类型注解，可以定义字段的序列化/反序列化的顺序.
* SerializeArrayOrListLength: 设置或通过 ```refField```字段来指定Array或List的长度，尤其是反序列化时.

### Todo

* SerializeCharField: char类型.
* SerializeBCDCodeField: BCD类型.
* ...
