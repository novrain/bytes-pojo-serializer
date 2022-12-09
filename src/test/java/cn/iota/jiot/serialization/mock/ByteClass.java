package cn.iota.jiot.serialization.mock;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeIntegerField;

public class ByteClass {
    @SerializeField
    Byte classB = 10;
    @SerializeField
    byte rawB = 20;

    @SerializeField
    @SerializeIntegerField(size = 2)
    byte annIntegerB = 30;
    @SerializeField
    @SerializeIntegerField(size = 2, order = ByteOrder.LITTLE_ENDIAN)
    byte annIntegerBLE = 30;

    public Byte getClassB() {
        return classB;
    }

    public void setClassB(Byte classB) {
        this.classB = classB;
    }

    public byte getRawB() {
        return rawB;
    }

    public void setRawB(byte rawB) {
        this.rawB = rawB;
    }

    public byte getAnnIntegerB() {
        return annIntegerB;
    }

    public void setAnnIntegerB(byte annIntegerB) {
        this.annIntegerB = annIntegerB;
    }

    public byte getAnnIntegerBLE() {
        return annIntegerBLE;
    }

    public void setAnnIntegerBLE(byte annIntegerBLE) {
        this.annIntegerBLE = annIntegerBLE;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classB == null) ? 0 : classB.hashCode());
        result = prime * result + rawB;
        result = prime * result + annIntegerB;
        result = prime * result + annIntegerBLE;
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
        ByteClass other = (ByteClass) obj;
        if (classB == null) {
            if (other.classB != null)
                return false;
        } else if (!classB.equals(other.classB))
            return false;
        if (rawB != other.rawB)
            return false;
        if (annIntegerB != other.annIntegerB)
            return false;
        if (annIntegerBLE != other.annIntegerBLE)
            return false;
        return true;
    }

}