package cn.iota.jiot.serialization.mock;

import java.util.Arrays;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeStringField;

public class StringArrayClass {
    @SerializeField
    @SerializeStringField(length = 20)
    @SerializeArrayOrListLength(length = 2)
    String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(names);
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
        StringArrayClass other = (StringArrayClass) obj;
        if (!Arrays.equals(names, other.names))
            return false;
        return true;
    }

}
