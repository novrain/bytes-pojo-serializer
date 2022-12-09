package cn.iota.jiot.serialization.mock;

import cn.iota.jiot.serialization.meta.ByteOrder;
import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeIntegerField;

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

    public Short getClassS() {
        return classS;
    }

    public void setClassS(Short classS) {
        this.classS = classS;
    }

    public short getRawS() {
        return rawS;
    }

    public void setRawS(short rawS) {
        this.rawS = rawS;
    }

    public short getAnnIntegerS() {
        return annIntegerS;
    }

    public void setAnnIntegerS(short annIntegerS) {
        this.annIntegerS = annIntegerS;
    }

    public short getAnnIntegerSLE() {
        return annIntegerSLE;
    }

    public void setAnnIntegerSLE(short annIntegerSLE) {
        this.annIntegerSLE = annIntegerSLE;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classS == null) ? 0 : classS.hashCode());
        result = prime * result + rawS;
        result = prime * result + annIntegerS;
        result = prime * result + annIntegerSLE;
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
        ShortClass other = (ShortClass) obj;
        if (classS == null) {
            if (other.classS != null)
                return false;
        } else if (!classS.equals(other.classS))
            return false;
        if (rawS != other.rawS)
            return false;
        if (annIntegerS != other.annIntegerS)
            return false;
        if (annIntegerSLE != other.annIntegerSLE)
            return false;
        return true;
    }
}
