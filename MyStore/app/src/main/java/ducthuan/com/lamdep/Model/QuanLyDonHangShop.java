package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuanLyDonHangShop implements Parcelable {

    @SerializedName("MAHD")
    @Expose
    private String mAHD;
    @SerializedName("TRANGTHAI")
    @Expose
    private String tRANGTHAI;
    @SerializedName("LOINHAN")
    @Expose
    private String lOINHAN;
    @SerializedName("TONGTIEN")
    @Expose
    private String tONGTIEN;
    @SerializedName("CHUYENKHOAN")
    @Expose
    private String cHUYENKHOAN;
    @SerializedName("NGAYMUA")
    @Expose
    private String nGAYMUA;
    @SerializedName("NGAYGIAO")
    @Expose
    private String nGAYGIAO;
    @SerializedName("TENNGUOINHAN")
    @Expose
    private String tENNGUOINHAN;
    @SerializedName("SODT")
    @Expose
    private String sODT;
    @SerializedName("DIACHI")
    @Expose
    private String dIACHI;
    @SerializedName("VANCHUYEN")
    @Expose
    private String vANCHUYEN;
    @SerializedName("THANHTOAN")
    @Expose
    private String tHANHTOAN;
    @SerializedName("MASP")
    @Expose
    private String mASP;
    @SerializedName("MAUSAC")
    @Expose
    private String mAUSAC;
    @SerializedName("KICHTHUOC")
    @Expose
    private String kICHTHUOC;
    @SerializedName("SOLUONG")
    @Expose
    private String sOLUONG;
    @SerializedName("GIASP")
    @Expose
    private String gIASP;
    @SerializedName("ANHLON")
    @Expose
    private String aNHLON;
    @SerializedName("KHUYENMAI")
    @Expose
    private String kHUYENMAI;
    @SerializedName("TENSHOP")
    @Expose
    private String tENSHOP;
    @SerializedName("TENSP")
    @Expose
    private String tENSP;

    protected QuanLyDonHangShop(Parcel in) {
        mAHD = in.readString();
        tRANGTHAI = in.readString();
        lOINHAN = in.readString();
        tONGTIEN = in.readString();
        cHUYENKHOAN = in.readString();
        nGAYMUA = in.readString();
        nGAYGIAO = in.readString();
        tENNGUOINHAN = in.readString();
        sODT = in.readString();
        dIACHI = in.readString();
        vANCHUYEN = in.readString();
        tHANHTOAN = in.readString();
        mASP = in.readString();
        mAUSAC = in.readString();
        kICHTHUOC = in.readString();
        sOLUONG = in.readString();
        gIASP = in.readString();
        aNHLON = in.readString();
        kHUYENMAI = in.readString();
        tENSHOP = in.readString();
        tENSP = in.readString();
    }

    public static final Creator<QuanLyDonHangShop> CREATOR = new Creator<QuanLyDonHangShop>() {
        @Override
        public QuanLyDonHangShop createFromParcel(Parcel in) {
            return new QuanLyDonHangShop(in);
        }

        @Override
        public QuanLyDonHangShop[] newArray(int size) {
            return new QuanLyDonHangShop[size];
        }
    };

    public String getMAHD() {
        return mAHD;
    }

    public void setMAHD(String mAHD) {
        this.mAHD = mAHD;
    }

    public String getTRANGTHAI() {
        return tRANGTHAI;
    }

    public void setTRANGTHAI(String tRANGTHAI) {
        this.tRANGTHAI = tRANGTHAI;
    }

    public String getLOINHAN() {
        return lOINHAN;
    }

    public void setLOINHAN(String lOINHAN) {
        this.lOINHAN = lOINHAN;
    }

    public String getTONGTIEN() {
        return tONGTIEN;
    }

    public void setTONGTIEN(String tONGTIEN) {
        this.tONGTIEN = tONGTIEN;
    }

    public String getCHUYENKHOAN() {
        return cHUYENKHOAN;
    }

    public void setCHUYENKHOAN(String cHUYENKHOAN) {
        this.cHUYENKHOAN = cHUYENKHOAN;
    }

    public String getNGAYMUA() {
        return nGAYMUA;
    }

    public void setNGAYMUA(String nGAYMUA) {
        this.nGAYMUA = nGAYMUA;
    }

    public String getNGAYGIAO() {
        return nGAYGIAO;
    }

    public void setNGAYGIAO(String nGAYGIAO) {
        this.nGAYGIAO = nGAYGIAO;
    }

    public String getTENNGUOINHAN() {
        return tENNGUOINHAN;
    }

    public void setTENNGUOINHAN(String tENNGUOINHAN) {
        this.tENNGUOINHAN = tENNGUOINHAN;
    }

    public String getSODT() {
        return sODT;
    }

    public void setSODT(String sODT) {
        this.sODT = sODT;
    }

    public String getDIACHI() {
        return dIACHI;
    }

    public void setDIACHI(String dIACHI) {
        this.dIACHI = dIACHI;
    }

    public String getVANCHUYEN() {
        return vANCHUYEN;
    }

    public void setVANCHUYEN(String vANCHUYEN) {
        this.vANCHUYEN = vANCHUYEN;
    }

    public String getTHANHTOAN() {
        return tHANHTOAN;
    }

    public void setTHANHTOAN(String tHANHTOAN) {
        this.tHANHTOAN = tHANHTOAN;
    }

    public String getMASP() {
        return mASP;
    }

    public void setMASP(String mASP) {
        this.mASP = mASP;
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

    public String getGIASP() {
        return gIASP;
    }

    public void setGIASP(String gIASP) {
        this.gIASP = gIASP;
    }

    public String getANHLON() {
        return aNHLON;
    }

    public void setANHLON(String aNHLON) {
        this.aNHLON = aNHLON;
    }

    public String getKHUYENMAI() {
        return kHUYENMAI;
    }

    public void setKHUYENMAI(String kHUYENMAI) {
        this.kHUYENMAI = kHUYENMAI;
    }

    public String getTENSHOP() {
        return tENSHOP;
    }

    public void setTENSHOP(String tENSHOP) {
        this.tENSHOP = tENSHOP;
    }

    public String getTENSP() {
        return tENSP;
    }

    public void setTENSP(String tENSP) {
        this.tENSP = tENSP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAHD);
        parcel.writeString(tRANGTHAI);
        parcel.writeString(lOINHAN);
        parcel.writeString(tONGTIEN);
        parcel.writeString(cHUYENKHOAN);
        parcel.writeString(nGAYMUA);
        parcel.writeString(nGAYGIAO);
        parcel.writeString(tENNGUOINHAN);
        parcel.writeString(sODT);
        parcel.writeString(dIACHI);
        parcel.writeString(vANCHUYEN);
        parcel.writeString(tHANHTOAN);
        parcel.writeString(mASP);
        parcel.writeString(mAUSAC);
        parcel.writeString(kICHTHUOC);
        parcel.writeString(sOLUONG);
        parcel.writeString(gIASP);
        parcel.writeString(aNHLON);
        parcel.writeString(kHUYENMAI);
        parcel.writeString(tENSHOP);
        parcel.writeString(tENSP);
    }
}