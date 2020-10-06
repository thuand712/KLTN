package ducthuan.com.lamdep.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuangCao {

@SerializedName("MAQUANGCAO")
@Expose
private String mAQUANGCAO;
@SerializedName("MATHUONGHIEU")
@Expose
private String mATHUONGHIEU;
@SerializedName("HINHQUANGCAO")
@Expose
private String hINHQUANGCAO;

public String getMAQUANGCAO() {
return mAQUANGCAO;
}

public void setMAQUANGCAO(String mAQUANGCAO) {
this.mAQUANGCAO = mAQUANGCAO;
}

public String getMATHUONGHIEU() {
return mATHUONGHIEU;
}

public void setMATHUONGHIEU(String mATHUONGHIEU) {
this.mATHUONGHIEU = mATHUONGHIEU;
}

public String getHINHQUANGCAO() {
return hINHQUANGCAO;
}

public void setHINHQUANGCAO(String hINHQUANGCAO) {
this.hINHQUANGCAO = hINHQUANGCAO;
}

}