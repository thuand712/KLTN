package ducthuan.com.lamdep.Model;

public class BaiViet {

    private int id;
    private int hinh;
    private String ten;
    private boolean bg;

    public BaiViet() {
    }

    public BaiViet(int id, int hinh, String ten, boolean bg) {
        this.id = id;
        this.hinh = hinh;
        this.ten = ten;
        this.bg = bg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public boolean isBg() {
        return bg;
    }

    public void setBg(boolean bg) {
        this.bg = bg;
    }
}
