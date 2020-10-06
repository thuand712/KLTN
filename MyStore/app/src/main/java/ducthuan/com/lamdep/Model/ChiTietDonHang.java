package ducthuan.com.lamdep.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiTietDonHang {

@SerializedName("MAHDTONG")
@Expose
private String mAHDTONG;
@SerializedName("TONGTIEN")
@Expose
private String tONGTIEN;
@SerializedName("NGAYGIAO")
@Expose
private String nGAYGIAO;

public String getMAHDTONG() {
return mAHDTONG;
}

public void setMAHDTONG(String mAHDTONG) {
this.mAHDTONG = mAHDTONG;
}

public String getTONGTIEN() {
return tONGTIEN;
}

public void setTONGTIEN(String tONGTIEN) {
this.tONGTIEN = tONGTIEN;
}

public String getNGAYGIAO() {
return nGAYGIAO;
}

public void setNGAYGIAO(String nGAYGIAO) {
this.nGAYGIAO = nGAYGIAO;
}

}