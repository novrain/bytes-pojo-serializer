package cn.iota.jiot.serialization.mock;

import java.util.ArrayList;
import java.util.List;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.meta.SerializeField;

public class ByteListClass {

    @SerializeField
    @SerializeArrayOrListLength(length = 3)
    List<Byte> bytes = new ArrayList<>();

    public ByteListClass() {
        bytes.add((byte) 1);
        bytes.add((byte) 2);
        bytes.add((byte) 3);
    }

    public List<Byte> getBytes() {
        return bytes;
    }

    public void setBytes(List<Byte> bytes) {
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bytes == null) ? 0 : bytes.hashCode());
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
        ByteListClass other = (ByteListClass) obj;
        if (bytes == null) {
            if (other.bytes != null)
                return false;
        } else if (!bytes.equals(other.bytes))
            return false;
        return true;
    }

}
