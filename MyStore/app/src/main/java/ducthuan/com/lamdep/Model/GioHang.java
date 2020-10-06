package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GioHang implements Parcelable {

    @SerializedName("MASP")
    @Expose
    private String mASP;
    @SerializedName("TENSP")
    @Expose
    private String tENSP;
    @SerializedName("GIASP")
    @Expose
    private String gIASP;
    @SerializedName("KHUYENMAI")
    @Expose
    private String kHUYENMAI;
    @SerializedName("MAUSAC")
    @Expose
    private String mAUSAC;
    @SerializedName("KICHTHUOC")
    @Expose
    private String kICHTHUOC;
    @SerializedName("SOLUONG")
    @Expose
    private String sOLUONG;
    @SerializedName("HINH")
    @Expose
    private String hINH;
    @SerializedName("SOLUONGTONKHO")
    @Expose
    private String sOLUONGTONKHO;
    @SerializedName("MANV")
    @Expose
    private String mANV;
    @SerializedName("DUOCCHON")
    @Expose
    private String dUOCCHON;
    @SerializedName("TENNV")
    @Expose
    private String tENNV;
    @SerializedName("MASHOP")
    @Expose
    private String mASHOP;
    @SerializedName("MAUSACSP")
    @Expose
    private String mAUSACSP;
    @SerializedName("KICHTHUOCSP")
    @Expose
    private String kICHTHUOCSP;

    protected GioHang(Parcel in) {
        mASP = in.readString();
        tENSP = in.readString();
        gIASP = in.readString();
        kHUYENMAI = in.readString();
        mAUSAC = in.readString();
        kICHTHUOC = in.readString();
        sOLUONG = in.readString();
        hINH = in.readString();
        sOLUONGTONKHO = in.readString();
        mANV = in.readString();
        dUOCCHON = in.readString();
        tENNV = in.readString();
        mASHOP = in.readString();
        mAUSACSP = in.readString();
        kICHTHUOCSP = in.readString();
    }

    public static final Creator<GioHang> CREATOR = new Creator<GioHang>() {
        @Override
        public GioHang createFromParcel(Parcel in) {
            return new GioHang(in);
        }

        @Override
        public GioHang[] newArray(int size) {
            return new GioHang[size];
        }
    };

    public String getMASP() {
        return mASP;
    }

    public void setMASP(String mASP) {
        this.mASP = mASP;
    }

    public String getTENSP() {
        return tENSP;
    }

    public void setTENSP(String tENSP) {
        this.tENSP = tENSP;
    }

    public String getGIASP() {
        return gIASP;
    }

    public void setGIASP(String gIASP) {
        this.gIASP = gIASP;
    }

    public String getKHUYENMAI() {
        return kHUYENMAI;
    }

    public void setKHUYENMAI(String kHUYENMAI) {
        this.kHUYENMAI = kHUYENMAI;
    }

    public String getMAUSAC() {
        return mAUSAC;
    }

    public void setMAUSAC(String mAUSAC) {
        this.mAUSAC = mAUSAC;
    }

    public String getKICHTHUOC() {
        return kICHTHUOC;
    }

    public void setKICHTHUOC(String kICHTHUOC) {
        this.kICHTHUOC = kICHTHUOC;
    }

    public String getSOLUONG() {
        return sOLUONG;
    }

    public void setSOLUONG(String sOLUONG) {
        this.sOLUONG = sOLUONG;
    }

    public String getHINH() {
        return hINH;
    }

    public void setHINH(String hINH) {
        this.hINH = hINH;
    }

    public String getSOLUONGTONKHO() {
        return sOLUONGTONKHO;
    }

    public void setSOLUONGTONKHO(String sOLUONGTONKHO) {
        this.sOLUONGTONKHO = sOLUONGTONKHO;
    }

    public String getMANV() {
        return mANV;
    }

    public void setMANV(String mANV) {
        this.mANV = mANV;
    }

    public String getDUOCCHON() {
        return dUOCCHON;
    }

    public void setDUOCCHON(String dUOCCHON) {
        this.dUOCCHON = dUOCCHON;
    }

    public String getTENNV() {
        return tENNV;
    }

    public void setTENNV(String tENNV) {
        this.tENNV = tENNV;
    }

    public String getMASHOP() {
        return mASHOP;
    }

    public void setMASHOP(String mASHOP) {
        this.mASHOP = mASHOP;
    }

    public String getMAUSACSP() {
        return mAUSACSP;
    }

    public void setMAUSACSP(String mAUSACSP) {
        this.mAUSACSP = mAUSACSP;
    }

    public String getKICHTHUOCSP() {
        return kICHTHUOCSP;
    }

    public void setKICHTHUOCSP(String kICHTHUOCSP) {
        this.kICHTHUOCSP = kICHTHUOCSP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mASP);
        parcel.writeString(tENSP);
        parcel.writeString(gIASP);
        parcel.writeString(kHUYENMAI);
        parcel.writeString(mAUSAC);
        parcel.writeString(kICHTHUOC);
        parcel.writeString(sOLUONG);
        parcel.writeString(hINH);
        parcel.writeString(sOLUONGTONKHO);
        parcel.writeString(mANV);
        parcel.writeString(dUOCCHON);
        parcel.writeString(tENNV);
        parcel.writeString(mASHOP);
        parcel.writeString(mAUSACSP);
        parcel.writeString(kICHTHUOCSP);
    }
}