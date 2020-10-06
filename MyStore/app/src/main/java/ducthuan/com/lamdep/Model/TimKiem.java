package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimKiem implements Parcelable {

@SerializedName("MATIMKIEM")
@Expose
private String mATIMKIEM;
@SerializedName("MANV")
@Expose
private String mANV;
@SerializedName("NOIDUNG")
@Expose
private String nOIDUNG;
@SerializedName("LUOTTIMKIEM")
@Expose
private String lUOTTIMKIEM;
@SerializedName("HINH")
@Expose
private String hINH;
@SerializedName("LUOTMUA")
@Expose
private Integer lUOTMUA;

    protected TimKiem(Parcel in) {
        mATIMKIEM = in.readString();
        mANV = in.readString();
        nOIDUNG = in.readString();
        lUOTTIMKIEM = in.readString();
        hINH = in.readString();
        if (in.readByte() == 0) {
            lUOTMUA = null;
        } else {
            lUOTMUA = in.readInt();
        }
    }

    public static final Creator<TimKiem> CREATOR = new Creator<TimKiem>() {
        @Override
        public TimKiem createFromParcel(Parcel in) {
            return new TimKiem(in);
        }

        @Override
        public TimKiem[] newArray(int size) {
            return new TimKiem[size];
        }
    };

    public String getMATIMKIEM() {
return mATIMKIEM;
}

public void setMATIMKIEM(String mATIMKIEM) {
this.mATIMKIEM = mATIMKIEM;
}

public String getMANV() {
return mANV;
}

public void setMANV(String mANV) {
this.mANV = mANV;
}

public String getNOIDUNG() {
return nOIDUNG;
}

public void setNOIDUNG(String nOIDUNG) {
this.nOIDUNG = nOIDUNG;
}

public String getLUOTTIMKIEM() {
return lUOTTIMKIEM;
}

public void setLUOTTIMKIEM(String lUOTTIMKIEM) {
this.lUOTTIMKIEM = lUOTTIMKIEM;
}

public String getHINH() {
return hINH;
}

public void setHINH(String hINH) {
this.hINH = hINH;
}

public Integer getLUOTMUA() {
return lUOTMUA;
}

public void setLUOTMUA(Integer lUOTMUA) {
this.lUOTMUA = lUOTMUA;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mATIMKIEM);
        parcel.writeString(mANV);
        parcel.writeString(nOIDUNG);
        parcel.writeString(lUOTTIMKIEM);
        parcel.writeString(hINH);
        if (lUOTMUA == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(lUOTMUA);
        }
    }
}