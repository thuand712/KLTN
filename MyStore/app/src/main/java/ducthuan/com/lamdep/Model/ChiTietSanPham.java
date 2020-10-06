package ducthuan.com.lamdep.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiTietSanPham {

@SerializedName("TENCHITIET")
@Expose
private String tENCHITIET;
@SerializedName("GIATRI")
@Expose
private String gIATRI;

public String getTENCHITIET() {
return tENCHITIET;
}

public void setTENCHITIET(String tENCHITIET) {
this.tENCHITIET = tENCHITIET;
}

public String getGIATRI() {
return gIATRI;
}

public void setGIATRI(String gIATRI) {
this.gIATRI = gIATRI;
}

}