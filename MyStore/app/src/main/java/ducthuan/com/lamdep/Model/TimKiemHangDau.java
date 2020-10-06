package ducthuan.com.lamdep.Model;

public class TimKiemHangDau {
    private String hinh;
    private String noidung;
    private boolean check;

    public TimKiemHangDau() {
    }

    public TimKiemHangDau(String hinh, String noidung, boolean check) {
        this.hinh = hinh;
        this.noidung = noidung;
        this.check = check;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
