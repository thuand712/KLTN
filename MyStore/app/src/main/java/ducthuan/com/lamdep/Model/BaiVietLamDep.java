package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaiVietLamDep implements Parcelable {

@SerializedName("MABAIVIETLAMDEP")
@Expose
private String mABAIVIETLAMDEP;
@SerializedName("TIEUDEBAIVIETLAMDEP")
@Expose
private String tIEUDEBAIVIETLAMDEP;
@SerializedName("HINHBAIVIETLAMDEP")
@Expose
private String hINHBAIVIETLAMDEP;
@SerializedName("MALOAILAMDEP")
@Expose
private String mALOAILAMDEP;
@SerializedName("LINKBAIVIETLAMDEP")
@Expose
private String lINKBAIVIETLAMDEP;

    protected BaiVietLamDep(Parcel in) {
        mABAIVIETLAMDEP = in.readString();
        tIEUDEBAIVIETLAMDEP = in.readString();
        hINHBAIVIETLAMDEP = in.readString();
        mALOAILAMDEP = in.readString();
        lINKBAIVIETLAMDEP = in.readString();
    }

    public static final Creator<BaiVietLamDep> CREATOR = new Creator<BaiVietLamDep>() {
        @Override
        public BaiVietLamDep createFromParcel(Parcel in) {
            return new BaiVietLamDep(in);
        }

        @Override
        public BaiVietLamDep[] newArray(int size) {
            return new BaiVietLamDep[size];
        }
    };

    public String getMABAIVIETLAMDEP() {
return mABAIVIETLAMDEP;
}

public void setMABAIVIETLAMDEP(String mABAIVIETLAMDEP) {
this.mABAIVIETLAMDEP = mABAIVIETLAMDEP;
}

public String getTIEUDEBAIVIETLAMDEP() {
return tIEUDEBAIVIETLAMDEP;
}

public void setTIEUDEBAIVIETLAMDEP(String tIEUDEBAIVIETLAMDEP) {
this.tIEUDEBAIVIETLAMDEP = tIEUDEBAIVIETLAMDEP;
}

public String getHINHBAIVIETLAMDEP() {
return hINHBAIVIETLAMDEP;
}

public void setHINHBAIVIETLAMDEP(String hINHBAIVIETLAMDEP) {
this.hINHBAIVIETLAMDEP = hINHBAIVIETLAMDEP;
}

public String getMALOAILAMDEP() {
return mALOAILAMDEP;
}

public void setMALOAILAMDEP(String mALOAILAMDEP) {
this.mALOAILAMDEP = mALOAILAMDEP;
}

public String getLINKBAIVIETLAMDEP() {
return lINKBAIVIETLAMDEP;
}

public void setLINKBAIVIETLAMDEP(String lINKBAIVIETLAMDEP) {
this.lINKBAIVIETLAMDEP = lINKBAIVIETLAMDEP;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mABAIVIETLAMDEP);
        parcel.writeString(tIEUDEBAIVIETLAMDEP);
        parcel.writeString(hINHBAIVIETLAMDEP);
        parcel.writeString(mALOAILAMDEP);
        parcel.writeString(lINKBAIVIETLAMDEP);
    }
}