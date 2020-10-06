package ducthuan.com.lamdep.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoaiLamDep implements Parcelable {

@SerializedName("MALOAILAMDEP")
@Expose
private String mALOAILAMDEP;
@SerializedName("TIEUDELOAILAMDEP")
@Expose
private String tIEUDELOAILAMDEP;
@SerializedName("HINHLOAILAMDEP")
@Expose
private String hINHLOAILAMDEP;
@SerializedName("MALAMDEP")
@Expose
private String mALAMDEP;

    protected LoaiLamDep(Parcel in) {
        mALOAILAMDEP = in.readString();
        tIEUDELOAILAMDEP = in.readString();
        hINHLOAILAMDEP = in.readString();
        mALAMDEP = in.readString();
    }

    public static final Creator<LoaiLamDep> CREATOR = new Creator<LoaiLamDep>() {
        @Override
        public LoaiLamDep createFromParcel(Parcel in) {
            return new LoaiLamDep(in);
        }

        @Override
        public LoaiLamDep[] newArray(int size) {
            return new LoaiLamDep[size];
        }
    };

    public String getMALOAILAMDEP() {
return mALOAILAMDEP;
}

public void setMALOAILAMDEP(String mALOAILAMDEP) {
this.mALOAILAMDEP = mALOAILAMDEP;
}

public String getTIEUDELOAILAMDEP() {
return tIEUDELOAILAMDEP;
}

public void setTIEUDELOAILAMDEP(String tIEUDELOAILAMDEP) {
this.tIEUDELOAILAMDEP = tIEUDELOAILAMDEP;
}

public String getHINHLOAILAMDEP() {
return hINHLOAILAMDEP;
}

public void setHINHLOAILAMDEP(String hINHLOAILAMDEP) {
this.hINHLOAILAMDEP = hINHLOAILAMDEP;
}

public String getMALAMDEP() {
return mALAMDEP;
}

public void setMALAMDEP(String mALAMDEP) {
this.mALAMDEP = mALAMDEP;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mALOAILAMDEP);
        parcel.writeString(tIEUDELOAILAMDEP);
        parcel.writeString(hINHLOAILAMDEP);
        parcel.writeString(mALAMDEP);
    }
}