package cn.iota.jiot.serialization.mock;

import java.util.ArrayList;
import java.util.List;

import cn.iota.jiot.serialization.meta.SerializeArrayOrListLength;
import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeStringField;

public class StringListClass {

    @SerializeField
    @SerializeArrayOrListLength(length = 3)
    @SerializeStringField(length = 10)
    List<String> strings = new ArrayList<>();

    public StringListClass() {
        strings.add("China");
        strings.add("America");
        strings.add("England");
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> bytes) {
        this.strings = bytes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((strings == null) ? 0 : strings.hashCode());
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
        StringListClass other = (StringListClass) obj;
        if (strings == null) {
            if (other.strings != null)
                return false;
        } else if (!strings.equals(other.strings))
            return false;
        return true;
    }

}
