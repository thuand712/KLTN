package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DanhGia implements Parcelable {

@SerializedName("MASP")
@Expose
private String mASP;
@SerializedName("MANV")
@Expose
private String mANV;
@SerializedName("TIEUDE")
@Expose
private String tIEUDE;
@SerializedName("NOIDUNG")
@Expose
private String nOIDUNG;
@SerializedName("SOSAO")
@Expose
private String sOSAO;
@SerializedName("NGAYDG")
@Expose
private String nGAYDG;
@SerializedName("HINH")
@Expose
private String hINH;
@SerializedName("TENNV")
@Expose
private String tENNV;

    protected DanhGia(Parcel in) {
        mASP = in.readString();
        mANV = in.readString();
        tIEUDE = in.readString();
        nOIDUNG = in.readString();
        sOSAO = in.readString();
        nGAYDG = in.readString();
        hINH = in.readString();
        tENNV = in.readString();
    }

    public static final Creator<DanhGia> CREATOR = new Creator<DanhGia>() {
        @Override
        public DanhGia createFromParcel(Parcel in) {
            return new DanhGia(in);
        }

        @Override
        public DanhGia[] newArray(int size) {
            return new DanhGia[size];
        }
    };

    public String getMASP() {
return mASP;
}

public void setMASP(String mASP) {
this.mASP = mASP;
}

public String getMANV() {
return mANV;
}

public void setMANV(String mANV) {
this.mANV = mANV;
}

public String getTIEUDE() {
return tIEUDE;
}

public void setTIEUDE(String tIEUDE) {
this.tIEUDE = tIEUDE;
}

public String getNOIDUNG() {
return nOIDUNG;
}

public void setNOIDUNG(String nOIDUNG) {
this.nOIDUNG = nOIDUNG;
}

public String getSOSAO() {
return sOSAO;
}

public void setSOSAO(String sOSAO) {
this.sOSAO = sOSAO;
}

public String getNGAYDG() {
return nGAYDG;
}

public void setNGAYDG(String nGAYDG) {
this.nGAYDG = nGAYDG;
}

public String getHINH() {
return hINH;
}

public void setHINH(String hINH) {
this.hINH = hINH;
}

public String getTENNV() {
return tENNV;
}

public void setTENNV(String tENNV) {
this.tENNV = tENNV;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mASP);
        parcel.writeString(mANV);
        parcel.writeString(tIEUDE);
        parcel.writeString(nOIDUNG);
        parcel.writeString(sOSAO);
        parcel.writeString(nGAYDG);
        parcel.writeString(hINH);
        parcel.writeString(tENNV);
    }
}