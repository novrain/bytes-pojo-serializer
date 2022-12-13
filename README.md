# A Java Serializer/Deserializer Using Annotations

By adding annotations to POJO objects and their fields, the serializer can encode POJO objects into a byte array and vice versa (by deserializer).

We use ByteBuf from Netty.

## SerializeMapper

The default serializer/deserializer implements, here are two simple unit tests to show how to use it, you can find more examples in the test directory.

### Simple Primitive Value

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

### A Class

The POJO class:

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

The test:

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

## Supported Data Types and Annotations

The SerializeMapper works according to some default rules and the information of field's annotation:

* Primitive types (number and string) and their wrappers: if there is no annotation, the serializer/deserializer will process them automatically.
  * the length occupied inside the bytes based on it's type, for example short will using 2 bytes.
  * the byte order will be treated as BIG_ENDIAN.
* Arrays/Lists of primitive types (number and string) and arrays/lists of their wrappers: if there is no annotation, serializer will process them automatically too, it will get the length of array of list by reflect; but deserializer cannot work automatically, we must use annotation, such as SerializeArrayOrListLength, to tell the deserializer the length of it.
* Object field: will be processed as nested and recursively.

### Here are the annotations on the field

* SerializeField：a annotation to mark field will be serialized and specify some common attributes, now ```index``` only.
* SerializeBitField: this annotation means this field is a bit of byte stream; the value of ```end``` determines whether to jump to the next byte.
* SerializeByteField: a byte value in the byte stream, supports signed/unsigned.
* SerializeIntField: a int value in the byte stream, supports signed/unsigned, byte order.
* SerializeShortField: a short value in the byte stream, like ```SerializeIntField```.
* SerializeLongField: a long value in the byte stream, like ```SerializeIntField``` and ```SerializeShortField```.
* SerializeIntegerField: common integer value which can set the size.
* SerializeStringField: a string value in the byte stream, supports pad, charset and length or set length by ```refField``` which supports the Length-Value composite sequence in the byte stream.

### Some assistant annotations

* SerializeFieldOrder: a annotation ```target the class``` to specify the order of fields.
* SerializeArrayOrListLength: a annotation to specify the length of an array or list.

### Todo

* SerializeCharField: a char value.
* SerializeBCDCodeField: a BCD sequence.
* ...
