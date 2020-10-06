package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TapLuyen implements Parcelable {
    private String idtapluyen;
    private String tentapluyen;
    private String hinh;
    private String macha;
    private String laplai;
    private String thoigianlaplai;

    public TapLuyen() {
    }

    public TapLuyen(String idtapluyen, String tentapluyen, String hinh, String macha, String laplai, String thoigianlaplai) {
        this.idtapluyen = idtapluyen;
        this.tentapluyen = tentapluyen;
        this.hinh = hinh;
        this.macha = macha;
        this.laplai = laplai;
        this.thoigianlaplai = thoigianlaplai;
    }


    protected TapLuyen(Parcel in) {
        idtapluyen = in.readString();
        tentapluyen = in.readString();
        hinh = in.readString();
        macha = in.readString();
        laplai = in.readString();
        thoigianlaplai = in.readString();
    }

    public static final Creator<TapLuyen> CREATOR = new Creator<TapLuyen>() {
        @Override
        public TapLuyen createFromParcel(Parcel in) {
            return new TapLuyen(in);
        }

        @Override
        public TapLuyen[] newArray(int size) {
            return new TapLuyen[size];
        }
    };

    public String getIdtapluyen() {
        return idtapluyen;
    }

    public void setIdtapluyen(String idtapluyen) {
        this.idtapluyen = idtapluyen;
    }

    public String getTentapluyen() {
        return tentapluyen;
    }

    public void setTentapluyen(String tentapluyen) {
        this.tentapluyen = tentapluyen;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getMacha() {
        return macha;
    }

    public void setMacha(String macha) {
        this.macha = macha;
    }

    public String getLaplai() {
        return laplai;
    }

    public void setLaplai(String laplai) {
        this.laplai = laplai;
    }

    public String getThoigianlaplai() {
        return thoigianlaplai;
    }

    public void setThoigianlaplai(String thoigianlaplai) {
        this.thoigianlaplai = thoigianlaplai;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idtapluyen);
        parcel.writeString(tentapluyen);
        parcel.writeString(hinh);
        parcel.writeString(macha);
        parcel.writeString(laplai);
        parcel.writeString(thoigianlaplai);
    }
}
