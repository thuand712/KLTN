package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChuyenKhoan implements Parcelable {

    @SerializedName("MAHDTONG")
    @Expose
    private String mAHDTONG;
    @SerializedName("MAKH")
    @Expose
    private String mAKH;
    @SerializedName("NGAYCK")
    @Expose
    private String nGAYCK;
    @SerializedName("NGANHANGCK")
    @Expose
    private String nGANHANGCK;
    @SerializedName("STKCK")
    @Expose
    private String sTKCK;
    @SerializedName("NGANHANGNHAN")
    @Expose
    private String nGANHANGNHAN;
    @SerializedName("STKNHAN")
    @Expose
    private String sTKNHAN;
    @SerializedName("SOTIENCHUYEN")
    @Expose
    private String sOTIENCHUYEN;
    @SerializedName("GHICHUCK")
    @Expose
    private String gHICHUCK;
    @SerializedName("HINHCK")
    @Expose
    private String hINHCK;
    @SerializedName("TRANGTHAI")
    @Expose
    private String tRANGTHAI;
    @SerializedName("TENNGUOINHAN")
    @Expose
    private String tENNGUOINHAN;
    @SerializedName("TONGTIENHD")
    @Expose
    private String tONGTIENHD;

    protected ChuyenKhoan(Parcel in) {
        mAHDTONG = in.readString();
        mAKH = in.readString();
        nGAYCK = in.readString();
        nGANHANGCK = in.readString();
        sTKCK = in.readString();
        nGANHANGNHAN = in.readString();
        sTKNHAN = in.readString();
        sOTIENCHUYEN = in.readString();
        gHICHUCK = in.readString();
        hINHCK = in.readString();
        tRANGTHAI = in.readString();
        tENNGUOINHAN = in.readString();
        tONGTIENHD = in.readString();
    }

    public static final Creator<ChuyenKhoan> CREATOR = new Creator<ChuyenKhoan>() {
        @Override
        public ChuyenKhoan createFromParcel(Parcel in) {
            return new ChuyenKhoan(in);
        }

        @Override
        public ChuyenKhoan[] newArray(int size) {
            return new ChuyenKhoan[size];
        }
    };

    public String getMAHDTONG() {
        return mAHDTONG;
    }

    public void setMAHDTONG(String mAHDTONG) {
        this.mAHDTONG = mAHDTONG;
    }

    public String getMAKH() {
        return mAKH;
    }

    public void setMAKH(String mAKH) {
        this.mAKH = mAKH;
    }

    public String getNGAYCK() {
        return nGAYCK;
    }

    public void setNGAYCK(String nGAYCK) {
        this.nGAYCK = nGAYCK;
    }

    public String getNGANHANGCK() {
        return nGANHANGCK;
    }

    public void setNGANHANGCK(String nGANHANGCK) {
        this.nGANHANGCK = nGANHANGCK;
    }

    public String getSTKCK() {
        return sTKCK;
    }

    public void setSTKCK(String sTKCK) {
        this.sTKCK = sTKCK;
    }

    public String getNGANHANGNHAN() {
        return nGANHANGNHAN;
    }

    public void setNGANHANGNHAN(String nGANHANGNHAN) {
        this.nGANHANGNHAN = nGANHANGNHAN;
    }

    public String getSTKNHAN() {
        return sTKNHAN;
    }

    public void setSTKNHAN(String sTKNHAN) {
        this.sTKNHAN = sTKNHAN;
    }

    public String getSOTIENCHUYEN() {
        return sOTIENCHUYEN;
    }

    public void setSOTIENCHUYEN(String sOTIENCHUYEN) {
        this.sOTIENCHUYEN = sOTIENCHUYEN;
    }

    public String getGHICHUCK() {
        return gHICHUCK;
    }

    public void setGHICHUCK(String gHICHUCK) {
        this.gHICHUCK = gHICHUCK;
    }

    public String getHINHCK() {
        return hINHCK;
    }

    public void setHINHCK(String hINHCK) {
        this.hINHCK = hINHCK;
    }

    public String getTRANGTHAI() {
        return tRANGTHAI;
    }

    public void setTRANGTHAI(String tRANGTHAI) {
        this.tRANGTHAI = tRANGTHAI;
    }

    public String getTENNGUOINHAN() {
        return tENNGUOINHAN;
    }

    public void setTENNGUOINHAN(String tENNGUOINHAN) {
        this.tENNGUOINHAN = tENNGUOINHAN;
    }

    public String getTONGTIENHD() {
        return tONGTIENHD;
    }

    public void setTONGTIENHD(String tONGTIENHD) {
        this.tONGTIENHD = tONGTIENHD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAHDTONG);
        parcel.writeString(mAKH);
        parcel.writeString(nGAYCK);
        parcel.writeString(nGANHANGCK);
        parcel.writeString(sTKCK);
        parcel.writeString(nGANHANGNHAN);
        parcel.writeString(sTKNHAN);
        parcel.writeString(sOTIENCHUYEN);
        parcel.writeString(gHICHUCK);
        parcel.writeString(hINHCK);
        parcel.writeString(tRANGTHAI);
        parcel.writeString(tENNGUOINHAN);
        parcel.writeString(tONGTIENHD);
    }
}