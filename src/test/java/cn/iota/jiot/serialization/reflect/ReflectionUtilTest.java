package cn.iota.jiot.serialization.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeFieldOrder;

class NoFieldClass {

}

class SeveralFieldsClass {
    @SerializeField
    public int intValue;
    @SerializeField
    public byte byteValue;

    NoFieldClass noFieldClass;
}

@SerializeFieldOrder({ "byteValue", "intValue" })
class OrderedFieldsClass {
    @SerializeField
    int intValue;
    @SerializeField
    byte byteValue;

    NoFieldClass noFieldClass;
}

class OrderedFieldsInsideClass {
    @SerializeField(index = 1)
    int intValue;
    @SerializeField(index = 2)
    byte byteValue;

    NoFieldClass noFieldClass;
}

class ListField {
    List<Byte> field;
}

public class ReflectionUtilTest {

    @Test
    void noFieldClassTest() {
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(NoFieldClass.class);
        assertEquals(fields.size(), 0);
    }

    @Test
    void severalFieldsClassTest() {
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(SeveralFieldsClass.class);
        assertEquals(fields.size(), 2);
    }

    @Test
    void orderedFieldsClassTest() {
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(OrderedFieldsClass.class);
        assertEquals(fields.size(), 2);
        Object[] arr = fields.values().toArray();
        assertEquals(((Field) arr[0]).getName(), "byteValue");
        assertEquals(((Field) arr[1]).getName(), "intValue");
    }

    @Test
    void orderedFieldsInsideClassTest() {
        Map<String, Field> fields = ReflectionUtil.findSerializeFields(OrderedFieldsInsideClass.class);
        assertEquals(fields.size(), 2);
        Object[] arr = fields.values().toArray();
        assertEquals(((Field) arr[0]).getName(), "intValue");
        assertEquals(((Field) arr[1]).getName(), "byteValue");
    }

    @Test
    void isPrimitiveListTest() throws NoSuchFieldException, SecurityException {
        assertEquals(true, ReflectionUtil.isPrimitiveWrapperList(ListField.class.getDeclaredField("field")));
    }
}
