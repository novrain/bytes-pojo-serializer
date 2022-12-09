package cn.iota.jiot.serialization.mock;

import java.util.Arrays;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.meta.SerializeField;

public class ByteArrayClass {

    @SerializeField
    @SerializeArrayOrListLength(length = 3)
    Byte[] bytes = { 1, 2, 2 };

    public Byte[] getBytes() {
        return bytes;
    }

    public void setBytes(Byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ByteArrayClass other = (ByteArrayClass) obj;
        if (!Arrays.equals(bytes, other.bytes))
            return false;
        return true;
    }
}
