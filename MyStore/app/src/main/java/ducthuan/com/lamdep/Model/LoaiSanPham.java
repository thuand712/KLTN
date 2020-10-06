package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoaiSanPham implements Parcelable {

    @SerializedName("MALOAISP")
    @Expose
    private String mALOAISP;
    @SerializedName("TENLOAISP")
    @Expose
    private String tENLOAISP;
    @SerializedName("HINHICON")
    @Expose
    private String hINHICON;
    @SerializedName("HINHANH")
    @Expose
    private String hINHANH;

    protected LoaiSanPham(Parcel in) {
        mALOAISP = in.readString();
        tENLOAISP = in.readString();
        hINHICON = in.readString();
        hINHANH = in.readString();
    }

    public static final Creator<LoaiSanPham> CREATOR = new Creator<LoaiSanPham>() {
        @Override
        public LoaiSanPham createFromParcel(Parcel in) {
            return new LoaiSanPham(in);
        }

        @Override
        public LoaiSanPham[] newArray(int size) {
            return new LoaiSanPham[size];
        }
    };

    public String getMALOAISP() {
        return mALOAISP;
    }

    public void setMALOAISP(String mALOAISP) {
        this.mALOAISP = mALOAISP;
    }

    public String getTENLOAISP() {
        return tENLOAISP;
    }

    public void setTENLOAISP(String tENLOAISP) {
        this.tENLOAISP = tENLOAISP;
    }

    public String getHINHICON() {
        return hINHICON;
    }

    public void setHINHICON(String hINHICON) {
        this.hINHICON = hINHICON;
    }

    public String getHINHANH() {
        return hINHANH;
    }

    public void setHINHANH(String hINHANH) {
        this.hINHANH = hINHANH;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mALOAISP);
        parcel.writeString(tENLOAISP);
        parcel.writeString(hINHICON);
        parcel.writeString(hINHANH);
    }
}