package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NhanVien implements Parcelable {

    @SerializedName("MANV")
    @Expose
    private String mANV;
    @SerializedName("TENNV")
    @Expose
    private String tENNV;
    @SerializedName("TENDANGNHAP")
    @Expose
    private String tENDANGNHAP;
    @SerializedName("MATKHAU")
    @Expose
    private String mATKHAU;
    @SerializedName("DIACHI")
    @Expose
    private String dIACHI;
    @SerializedName("NGAYSINH")
    @Expose
    private String nGAYSINH;
    @SerializedName("SODT")
    @Expose
    private String sODT;
    @SerializedName("GIOITINH")
    @Expose
    private String gIOITINH;
    @SerializedName("MALOAINV")
    @Expose
    private String mALOAINV;
    @SerializedName("HINH")
    @Expose
    private String hINH;
    @SerializedName("NGAYDANGKY")
    @Expose
    private String nGAYDANGKY;
    @SerializedName("TRANGTHAINV")
    @Expose
    private String tRANGTHAINV;


    protected NhanVien(Parcel in) {
        mANV = in.readString();
        tENNV = in.readString();
        tENDANGNHAP = in.readString();
        mATKHAU = in.readString();
        dIACHI = in.readString();
        nGAYSINH = in.readString();
        sODT = in.readString();
        gIOITINH = in.readString();
        mALOAINV = in.readString();
        hINH = in.readString();
        nGAYDANGKY = in.readString();
        tRANGTHAINV = in.readString();
    }

    public static final Creator<NhanVien> CREATOR = new Creator<NhanVien>() {
        @Override
        public NhanVien createFromParcel(Parcel in) {
            return new NhanVien(in);
        }

        @Override
        public NhanVien[] newArray(int size) {
            return new NhanVien[size];
        }
    };

    public String getMANV() {
        return mANV;
    }

    public void setMANV(String mANV) {
        this.mANV = mANV;
    }

    public String getTENNV() {
        return tENNV;
    }

    public void setTENNV(String tENNV) {
        this.tENNV = tENNV;
    }

    public String getTENDANGNHAP() {
        return tENDANGNHAP;
    }

    public void setTENDANGNHAP(String tENDANGNHAP) {
        this.tENDANGNHAP = tENDANGNHAP;
    }

    public String getMATKHAU() {
        return mATKHAU;
    }

    public void setMATKHAU(String mATKHAU) {
        this.mATKHAU = mATKHAU;
    }

    public String getDIACHI() {
        return dIACHI;
    }

    public void setDIACHI(String dIACHI) {
        this.dIACHI = dIACHI;
    }

    public String getNGAYSINH() {
        return nGAYSINH;
    }

    public void setNGAYSINH(String nGAYSINH) {
        this.nGAYSINH = nGAYSINH;
    }

    public String getSODT() {
        return sODT;
    }

    public void setSODT(String sODT) {
        this.sODT = sODT;
    }

    public String getGIOITINH() {
        return gIOITINH;
    }

    public void setGIOITINH(String gIOITINH) {
        this.gIOITINH = gIOITINH;
    }

    public String getMALOAINV() {
        return mALOAINV;
    }

    public void setMALOAINV(String mALOAINV) {
        this.mALOAINV = mALOAINV;
    }

    public String getHINH() {
        return hINH;
    }

    public void setHINH(String hINH) {
        this.hINH = hINH;
    }

    public String getNGAYDANGKY() {
        return nGAYDANGKY;
    }

    public void setNGAYDANGKY(String nGAYDANGKY) {
        this.nGAYDANGKY = nGAYDANGKY;
    }

    public String getTRANGTHAINV() {
        return tRANGTHAINV;
    }

    public void setTRANGTHAINV(String tRANGTHAINV) {
        this.tRANGTHAINV = tRANGTHAINV;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mANV);
        parcel.writeString(tENNV);
        parcel.writeString(tENDANGNHAP);
        parcel.writeString(mATKHAU);
        parcel.writeString(dIACHI);
        parcel.writeString(nGAYSINH);
        parcel.writeString(sODT);
        parcel.writeString(gIOITINH);
        parcel.writeString(mALOAINV);
        parcel.writeString(hINH);
        parcel.writeString(nGAYDANGKY);
        parcel.writeString(tRANGTHAINV);
    }
}