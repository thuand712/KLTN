package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoanhThu implements Parcelable {

@SerializedName("MAHDTONG")
@Expose
private String mAHDTONG;
@SerializedName("TENNGUOINHAN")
@Expose
private String tENNGUOINHAN;
@SerializedName("NGAYGIAO")
@Expose
private String nGAYGIAO;
@SerializedName("TONGTIEN")
@Expose
private String tONGTIEN;

    protected DoanhThu(Parcel in) {
        mAHDTONG = in.readString();
        tENNGUOINHAN = in.readString();
        nGAYGIAO = in.readString();
        tONGTIEN = in.readString();
    }

    public static final Creator<DoanhThu> CREATOR = new Creator<DoanhThu>() {
        @Override
        public DoanhThu createFromParcel(Parcel in) {
            return new DoanhThu(in);
        }

        @Override
        public DoanhThu[] newArray(int size) {
            return new DoanhThu[size];
        }
    };

    public String getMAHDTONG() {
return mAHDTONG;
}

public void setMAHDTONG(String mAHDTONG) {
this.mAHDTONG = mAHDTONG;
}

public String getTENNGUOINHAN() {
return tENNGUOINHAN;
}

public void setTENNGUOINHAN(String tENNGUOINHAN) {
this.tENNGUOINHAN = tENNGUOINHAN;
}

public String getNGAYGIAO() {
return nGAYGIAO;
}

public void setNGAYGIAO(String nGAYGIAO) {
this.nGAYGIAO = nGAYGIAO;
}

public String getTONGTIEN() {
return tONGTIEN;
}

public void setTONGTIEN(String tONGTIEN) {
this.tONGTIEN = tONGTIEN;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAHDTONG);
        parcel.writeString(tENNGUOINHAN);
        parcel.writeString(nGAYGIAO);
        parcel.writeString(tONGTIEN);
    }
}