package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiaChiKhachHang implements Parcelable {

    @SerializedName("MADC")
    @Expose
    private String mADC;
    @SerializedName("MAKH")
    @Expose
    private String mAKH;
    @SerializedName("TENKH")
    @Expose
    private String tENKH;
    @SerializedName("DIACHIKH")
    @Expose
    private String dIACHIKH;
    @SerializedName("SODTKH")
    @Expose
    private String sODTKH;
    @SerializedName("EMAILKH")
    @Expose
    private String eMAILKH;
    @SerializedName("MACDINH")
    @Expose
    private String mACDINH;

    protected DiaChiKhachHang(Parcel in) {
        mADC = in.readString();
        mAKH = in.readString();
        tENKH = in.readString();
        dIACHIKH = in.readString();
        sODTKH = in.readString();
        eMAILKH = in.readString();
        mACDINH = in.readString();
    }

    public static final Creator<DiaChiKhachHang> CREATOR = new Creator<DiaChiKhachHang>() {
        @Override
        public DiaChiKhachHang createFromParcel(Parcel in) {
            return new DiaChiKhachHang(in);
        }

        @Override
        public DiaChiKhachHang[] newArray(int size) {
            return new DiaChiKhachHang[size];
        }
    };

    public String getMADC() {
        return mADC;
    }

    public void setMADC(String mADC) {
        this.mADC = mADC;
    }

    public String getMAKH() {
        return mAKH;
    }

    public void setMAKH(String mAKH) {
        this.mAKH = mAKH;
    }

    public String getTENKH() {
        return tENKH;
    }

    public void setTENKH(String tENKH) {
        this.tENKH = tENKH;
    }

    public String getDIACHIKH() {
        return dIACHIKH;
    }

    public void setDIACHIKH(String dIACHIKH) {
        this.dIACHIKH = dIACHIKH;
    }

    public String getSODTKH() {
        return sODTKH;
    }

    public void setSODTKH(String sODTKH) {
        this.sODTKH = sODTKH;
    }

    public String getEMAILKH() {
        return eMAILKH;
    }

    public void setEMAILKH(String eMAILKH) {
        this.eMAILKH = eMAILKH;
    }

    public String getMACDINH() {
        return mACDINH;
    }

    public void setMACDINH(String mACDINH) {
        this.mACDINH = mACDINH;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mADC);
        parcel.writeString(mAKH);
        parcel.writeString(tENKH);
        parcel.writeString(dIACHIKH);
        parcel.writeString(sODTKH);
        parcel.writeString(eMAILKH);
        parcel.writeString(mACDINH);
    }
}