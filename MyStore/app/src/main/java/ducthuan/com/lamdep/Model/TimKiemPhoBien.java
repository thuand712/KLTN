package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimKiemPhoBien implements Parcelable {

    @SerializedName("MATIMKIEMPB")
    @Expose
    private String mATIMKIEMPB;
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

    protected TimKiemPhoBien(Parcel in) {
        mATIMKIEMPB = in.readString();
        nOIDUNG = in.readString();
        lUOTTIMKIEM = in.readString();
        hINH = in.readString();
        if (in.readByte() == 0) {
            lUOTMUA = null;
        } else {
            lUOTMUA = in.readInt();
        }
    }

    public static final Creator<TimKiemPhoBien> CREATOR = new Creator<TimKiemPhoBien>() {
        @Override
        public TimKiemPhoBien createFromParcel(Parcel in) {
            return new TimKiemPhoBien(in);
        }

        @Override
        public TimKiemPhoBien[] newArray(int size) {
            return new TimKiemPhoBien[size];
        }
    };

    public String getMATIMKIEMPB() {
        return mATIMKIEMPB;
    }

    public void setMATIMKIEMPB(String mATIMKIEMPB) {
        this.mATIMKIEMPB = mATIMKIEMPB;
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
        parcel.writeString(mATIMKIEMPB);
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