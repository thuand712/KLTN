package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SanPham implements Parcelable{

    @SerializedName("MASP")
    @Expose
    private String mASP;
    @SerializedName("TENSP")
    @Expose
    private String tENSP;
    @SerializedName("GIA")
    @Expose
    private String gIA;
    @SerializedName("KHUYENMAI")
    @Expose
    private String kHUYENMAI;
    @SerializedName("ANHLON")
    @Expose
    private String aNHLON;
    @SerializedName("ANHNHO")
    @Expose
    private String aNHNHO;
    @SerializedName("THONGTIN")
    @Expose
    private String tHONGTIN;
    @SerializedName("SOLUONG")
    @Expose
    private String sOLUONG;
    @SerializedName("MALOAISP")
    @Expose
    private String mALOAISP;
    @SerializedName("MATHUONGHIEU")
    @Expose
    private String mATHUONGHIEU;
    @SerializedName("MANV")
    @Expose
    private String mANV;
    @SerializedName("LUOTMUA")
    @Expose
    private String lUOTMUA;
    @SerializedName("LUOTTHICH")
    @Expose
    private String lUOTTHICH;
    @SerializedName("TENNV")
    @Expose
    private String tENNV;
    @SerializedName("LUOTXEM")
    @Expose
    private String lUOTXEM;
    @SerializedName("MAUSAC")
    @Expose
    private String mAUSAC;
    @SerializedName("KICHTHUOC")
    @Expose
    private String kICHTHUOC;

    @SerializedName("TRANGTHAI")
    @Expose
    private String tRANGTHAI;

    protected SanPham(Parcel in) {
        mASP = in.readString();
        tENSP = in.readString();
        gIA = in.readString();
        kHUYENMAI = in.readString();
        aNHLON = in.readString();
        aNHNHO = in.readString();
        tHONGTIN = in.readString();
        sOLUONG = in.readString();
        mALOAISP = in.readString();
        mATHUONGHIEU = in.readString();
        mANV = in.readString();
        lUOTMUA = in.readString();
        lUOTTHICH = in.readString();
        tENNV = in.readString();
        lUOTXEM = in.readString();
        mAUSAC = in.readString();
        kICHTHUOC = in.readString();
        tRANGTHAI = in.readString();
    }

    public static final Creator<SanPham> CREATOR = new Creator<SanPham>() {
        @Override
        public SanPham createFromParcel(Parcel in) {
            return new SanPham(in);
        }

        @Override
        public SanPham[] newArray(int size) {
            return new SanPham[size];
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

    public String getGIA() {
        return gIA;
    }

    public void setGIA(String gIA) {
        this.gIA = gIA;
    }

    public String getKHUYENMAI() {
        return kHUYENMAI;
    }

    public void setKHUYENMAI(String kHUYENMAI) {
        this.kHUYENMAI = kHUYENMAI;
    }

    public String getANHLON() {
        return aNHLON;
    }

    public void setANHLON(String aNHLON) {
        this.aNHLON = aNHLON;
    }

    public String getANHNHO() {
        return aNHNHO;
    }

    public void setANHNHO(String aNHNHO) {
        this.aNHNHO = aNHNHO;
    }

    public String getTHONGTIN() {
        return tHONGTIN;
    }

    public void setTHONGTIN(String tHONGTIN) {
        this.tHONGTIN = tHONGTIN;
    }

    public String getSOLUONG() {
        return sOLUONG;
    }

    public void setSOLUONG(String sOLUONG) {
        this.sOLUONG = sOLUONG;
    }

    public String getMALOAISP() {
        return mALOAISP;
    }

    public void setMALOAISP(String mALOAISP) {
        this.mALOAISP = mALOAISP;
    }

    public String getMATHUONGHIEU() {
        return mATHUONGHIEU;
    }

    public void setMATHUONGHIEU(String mATHUONGHIEU) {
        this.mATHUONGHIEU = mATHUONGHIEU;
    }

    public String getMANV() {
        return mANV;
    }

    public void setMANV(String mANV) {
        this.mANV = mANV;
    }

    public String getLUOTMUA() {
        return lUOTMUA;
    }

    public void setLUOTMUA(String lUOTMUA) {
        this.lUOTMUA = lUOTMUA;
    }

    public String getLUOTTHICH() {
        return lUOTTHICH;
    }

    public void setLUOTTHICH(String lUOTTHICH) {
        this.lUOTTHICH = lUOTTHICH;
    }

    public String getTENNV() {
        return tENNV;
    }

    public void setTENNV(String tENNV) {
        this.tENNV = tENNV;
    }

    public String getLUOTXEM() {
        return lUOTXEM;
    }

    public void setLUOTXEM(String lUOTXEM) {
        this.lUOTXEM = lUOTXEM;
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

    public String getTRANGTHAI() {
        return tRANGTHAI;
    }

    public void setTRANGTHAI(String tRANGTHAI) {
        this.tRANGTHAI = tRANGTHAI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mASP);
        parcel.writeString(tENSP);
        parcel.writeString(gIA);
        parcel.writeString(kHUYENMAI);
        parcel.writeString(aNHLON);
        parcel.writeString(aNHNHO);
        parcel.writeString(tHONGTIN);
        parcel.writeString(sOLUONG);
        parcel.writeString(mALOAISP);
        parcel.writeString(mATHUONGHIEU);
        parcel.writeString(mANV);
        parcel.writeString(lUOTMUA);
        parcel.writeString(lUOTTHICH);
        parcel.writeString(tENNV);
        parcel.writeString(lUOTXEM);
        parcel.writeString(mAUSAC);
        parcel.writeString(kICHTHUOC);
        parcel.writeString(tRANGTHAI);
    }


}