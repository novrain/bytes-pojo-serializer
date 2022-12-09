package cn.iota.jiot.serialization.mock;

import java.util.Arrays;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.meta.SerializeField;

public class ShortArrayClass {

    @SerializeField
    @SerializeArrayOrListLength(length = 3)
    Short[] shorts = { 1, 2, 2 };

    public Short[] getShorts() {
        return shorts;
    }

    public void setShorts(Short[] bytes) {
        this.shorts = bytes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(shorts);
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
        ShortArrayClass other = (ShortArrayClass) obj;
        if (!Arrays.equals(shorts, other.shorts))
            return false;
        return true;
    }
}
