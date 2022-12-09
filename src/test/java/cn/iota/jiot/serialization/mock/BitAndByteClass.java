package cn.iota.jiot.serialization.mock;

import cn.iota.jiot.serialization.meta.SerializeField;

public class BitAndByteClass {
    @SerializeField
    BitClass bitClass = new BitClass();
    @SerializeField
    ByteClass byteClass = new ByteClass();

    public BitClass getBitClass() {
        return bitClass;
    }

    public void setBitClass(BitClass bitClass) {
        this.bitClass = bitClass;
    }

    public ByteClass getByteClass() {
        return byteClass;
    }

    public void setByteClass(ByteClass byteClass) {
        this.byteClass = byteClass;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bitClass == null) ? 0 : bitClass.hashCode());
        result = prime * result + ((byteClass == null) ? 0 : byteClass.hashCode());
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
        BitAndByteClass other = (BitAndByteClass) obj;
        if (bitClass == null) {
            if (other.bitClass != null)
                return false;
        } else if (!bitClass.equals(other.bitClass))
            return false;
        if (byteClass == null) {
            if (other.byteClass != null)
                return false;
        } else if (!byteClass.equals(other.byteClass))
            return false;
        return true;
    }

}
