package cn.iota.jiot.serialization.mock;

import cn.iota.jiot.serialization.meta.SerializeBitField;
import cn.iota.jiot.serialization.meta.SerializeField;

public class BitClass {

    @SerializeField
    @SerializeBitField(bit = 7)
    byte bit7 = 1;

    @SerializeField
    @SerializeBitField(bit = 6, end = true)
    int bit6 = 1;

    public byte getBit7() {
        return bit7;
    }

    public void setBit7(byte bit7) {
        this.bit7 = bit7;
    }

    public int getBit6() {
        return bit6;
    }

    public void setBit6(int bit6) {
        this.bit6 = bit6;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bit7;
        result = prime * result + bit6;
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
        BitClass other = (BitClass) obj;
        if (bit7 != other.bit7)
            return false;
        if (bit6 != other.bit6)
            return false;
        return true;
    }
}
