package ducthuan.com.lamdep.Service;

import org.json.JSONArray;

import java.util.List;

import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.Model.ChiTietDonHang;
import ducthuan.com.lamdep.Model.ChiTietSanPham;
import ducthuan.com.lamdep.Model.ChuyenKhoan;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.Model.DoanhThu;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.TimKiem;
import ducthuan.com.lamdep.Model.TimKiemPhoBien;
import ducthuan.com.lamdep.Notifications.MyResponse;
import ducthuan.com.lamdep.Notifications.Sender;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DataService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-fJ7_8w:APA91bFMONTHmseJwWcDaPQp2N3EPHr1E4Sk3sn66s6oQ8ROzQZg2oUj87JDYpuLYpg8rbZa4oTZ6FcrYO9MQ6pEoen0m_96ZJZUVFV3LBsnSeqfGy0gZD6JWLjUjdiiSCTduUxIvuLV"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


    //kiểm tra đăng ký
    @FormUrlEncoded
    @POST("kiemtradangky.php")
    Call<String>kiemTraDangKy(@Field("name") String name,@Field("email") String email);

    //Kiem tra dang nhap
    @FormUrlEncoded
    @POST("kiemtradangnhap.php")
    Call<String>kiemTraDangNhap(@Field("email") String email);

    //get thông tin nhân viên
    @FormUrlEncoded
    @POST("getnhanvien.php")
    Call<List<NhanVien>>getNhanVien(@Field("tennv") String tennv);

    //Lay danh sach quang cao trang chu
    @GET("quangcaotheongay.php")
    Call<List<QuangCao>>layQuangCaoTheoNgay();

    //Lay danh sach san pham flashsale hom nay
    @FormUrlEncoded
    @POST("getsanphamflashsale.php")
    Call<List<SanPham>>layDanhSachSanPhamFlashSale(@Field("limit") int limit);

    //lay danh sach san pham flashsale khung gio ke tiep
    @FormUrlEncoded
    @POST("getsanphamflashsale8.php")
    Call<List<SanPham>>layDanhSachSanPhamFlashSale8(@Field("limit") int limit);

    //lay danh sach san pham flashsale khung gio ke tiep cua ke tiep
    @FormUrlEncoded
    @POST("getsanphamflashsale16.php")
    Call<List<SanPham>>layDanhSachSanPhamFlashSale16(@Field("limit") int limit);

    //Lay danh sach loaisp
    @GET("getloaisanpham.php")
    Call<List<LoaiSanPham>>layDanhSachLoaiSanPham();

    //Lay danh sach sp tim kiem
    @FormUrlEncoded
    @POST("getsanphamtimkiem.php")
    Call<List<SanPham>>getTopSPTimKiem(@Field("tensp") String tensp,@Field("limit") int limit);

    //get tìm kiếm phổ biến
    @GET("gettimkiemphobien.php")
    Call<List<TimKiemPhoBien>>getTimKiemPhoBien();

    //get tìm kiếm phổ biến
    @FormUrlEncoded
    @POST("gettimkiemhangdaunv.php")
    Call<List<TimKiem>>getTimKiemHangDauNV(@Field("manv") String manv);

    //Lay danh sach sp yeu thich
    @GET("getsanphamyeuthich.php")
    Call<List<SanPham>>layDanhSachSanPhamYeuThich();

    //get toàn bộ nhân viên
    @GET("gettatcanhanvien.php")
    Call<List<NhanVien>>getTatCaNV();

    //ADMIN QUẢN LÝ NGƯỜI DÙNG
    //khóa người dùng
    @FormUrlEncoded
    @POST("adminkhoanguoidung.php")
    Call<String>khoaNguoiDung(@Field("manv") String manv);

    //Mở người dùng
    @FormUrlEncoded
    @POST("adminmokhoanguoidung.php")
    Call<String>moKhoaNguoiDung(@Field("manv") String manv);

    //khóa người dùng
    @FormUrlEncoded
    @POST("adminthangcapadmin.php")
    Call<String>thangCapAdmin(@Field("manv") String manv);

    //Mở người dùng
    @FormUrlEncoded
    @POST("adminxuongkhachhang.php")
    Call<String>xuongKhachHang(@Field("manv") String manv);

    //Lay danh sach sp ban chay
    @GET("getsanphambanchay.php")
    Call<List<SanPham>>getSPBanChay();

    //Lay danh sach sp ban chay
    @FormUrlEncoded
    @POST("getsanphamgoiy.php")
    Call<List<SanPham>>getSPGoiY(@Field("manv") String manv);

    //kiem tra co sp da xem nao chua
    @FormUrlEncoded
    @POST("kiemtrasanphamdaxem.php")
    Call<String>kiemTraDaXemSPNaoChua(@Field("manv") String manv);


    //lay danh sach sp theo danh muc moi nhat
    @FormUrlEncoded
    @POST("getsanphamtheodanhmuc.php")
    Call<List<SanPham>>getThoiTrangNus(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp co the ban thich
    @FormUrlEncoded
    @POST("getspcothebanthich.php")
    Call<List<SanPham>>getSPCoTheBanThich(@Field("masp") String masp);

    //lay danh sach sp theo danh muc ban chay
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucbanchay.php")
    Call<List<SanPham>>getThoiTrangNusBanChay(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp theo danh muc gia tang
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucgiatang.php")
    Call<List<SanPham>>getThoiTrangNusGiaTang(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay danh sach sp theo danh muc gia giam
    @FormUrlEncoded
    @POST("getsanphamtheodanhmucgiagiam.php")
    Call<List<SanPham>>getThoiTrangNusGiaGiam(@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //lay chi tiet sp
    @FormUrlEncoded
    @POST("getchitietsanpham.php")
    Call<List<ChiTietSanPham>>getChiTietSanPham(@Field("masp") String masp);

    //them du lieu dang nhap facebook
    @FormUrlEncoded
    @POST("dangnhapfbgg.php")
    Call<String>dangNhapFBGG(@Field("name") String name,@Field("email") String email);

    //thêm đánh giá
    @FormUrlEncoded
    @POST("themdanhgia.php")
    Call<String>ketQuaThemDanhGia(@Field("masp") String masp,@Field("manv") String manv, @Field("tieude") String tieude,
                                  @Field("noidung") String noidung,@Field("sosao") String sosao);

    //get danh gia show chi tiet sp
    @FormUrlEncoded
    @POST("getdanhsachdanhgia.php")
    Call<List<DanhGia>>getDanhGiaCTSP(@Field("masp") String masp);

    //get danh gia theo khách hàng
    @FormUrlEncoded
    @POST("getdanhgiatheokhachhang.php")
    Call<List<DanhGia>>getDanhGiaTheoKH(@Field("manv") String manv);

    //get danh gia theo san pham
    @FormUrlEncoded
    @POST("gettrungbinhratingsanpham.php")
    Call<String>getDanhGiaTheoSP(@Field("masp") String masp);

    //Update them gio hang
    @FormUrlEncoded
    @POST("themgiohang.php")
    Call<String>themGioHang(@Field("masp") String masp,@Field("manv") String manv,@Field("mausac") String mausac,
                            @Field("kichthuoc") String kichthuoc,@Field("soluong") String soluong);

    //get so luong san pham trong gio hang
    @FormUrlEncoded
    @POST("getsoluongsanphamgiohang.php")
    Call<String>getSoLuongSPGioHang(@Field("manv") String manv);

    //get danh sach san pham trong gio hang
    @FormUrlEncoded
    @POST("getsanphamgiohang.php")
    Call<List<GioHang>>getDanhSachSPGioHang(@Field("manv") String manv);

    //Xoa sp trong gio hang
    @FormUrlEncoded
    @POST("xoasanphamgiohang.php")
    Call<String>xoaSanPhamGioHang(@Field("masp") String masp, @Field("manv") String manv);

    //cap nhap so luong gio hang
    @FormUrlEncoded
    @POST("capnhapgiohangtanggiamsoluong.php")
    Call<String>capNhapSLSPGioHang(@Field("masp") String masp, @Field("manv") String manv,@Field("soluong") int soluong);

    //them dia chi khach hang lan dau
    @FormUrlEncoded
    @POST("luuthongtinkhachhang.php")
    Call<String>luuDiaChiKhachHang(@Field("manv") String manv, @Field("tennv") String tennv,@Field("diachi") String diachi
            ,@Field("sodt") String sodt,@Field("email") String email);

    //them dia chi khach hang
    @FormUrlEncoded
    @POST("themdiachikhachhang.php")
    Call<String>themDiaChiKhachHang(@Field("manv") String manv, @Field("tennv") String tennv,@Field("diachi") String diachi
            ,@Field("sodt") String sodt,@Field("email") String email,@Field("macdinh") String macdinh);

    //Kiem tra dia chi khach hang co chua
    @FormUrlEncoded
    @POST("kiemtradiachikhachhang.php")
    Call<List<DiaChiKhachHang>>getDiaChiKhachHangs(@Field("manv") String manv);

    //get dia chi khach hang
    @FormUrlEncoded
    @POST("getdiachikhachhang.php")
    Call<List<DiaChiKhachHang>>getDanhSachDiaChiKhachHangs(@Field("manv") String manv);

    //set dia chi mac dinh
    @FormUrlEncoded
    @POST("setdiachimacdinh.php")
    Call<String>setDiaChiMacDinh(@Field("makh") String makh,@Field("madc") String madc);

    //xoa dia chi
    @FormUrlEncoded
    @POST("xoadiachikhachhang.php")
    Call<String>xoaDiaChiKhachHang(@Field("madc") String madc);


    //Cap nhap trang thai duoc chon san pham gio hang
    @FormUrlEncoded
    @POST("capnhapchongiohang.php")
    Call<String>capNhapChonSPGioHang(@Field("masp") String masp,@Field("manv") String manv,@Field("duocchon") String duocchon);

    //Cập nhập chọn tất cả sản phẩm trong giỏ hàng
    @FormUrlEncoded
    @POST("capnhapchontatcagiohang.php")
    Call<String>capNhapChonTatCaSPGioHang(@Field("manv") String manv,@Field("duocchon") String duocchon);

    //get sản phẩm được chọn
    @FormUrlEncoded
    @POST("getsanphamduocchon.php")
    Call<List<SanPham>>getSanPhamDuocChon(@Field("masp") String masp);

    //get sản phẩm mua ngay
    @FormUrlEncoded
    @POST("getsanphammuangay.php")
    Call<List<GioHang>>getSanPhamMuaNgay(@Field("masp") String masp,@Field("manv") String manv);

    //update phan loai san pham
    @FormUrlEncoded
    @POST("suaphanloaisanpham.php")
    Call<String>suaPhanLoaiSP(@Field("masp") String masp,@Field("manv") String manv,@Field("mausac") String mausac,@Field("kichthuoc") String kichthuoc);

    //get tinh trang thich cua san pham
    @FormUrlEncoded
    @POST("gettinhtrangthichsanpham.php")
    Call<String>getTinhTrangThichSP(@Field("masp") String masp,@Field("manv") String manv);

    //get tinh trang mua hang cua nguoi dung
    @FormUrlEncoded
    @POST("muahangmoiduocdanhgia.php")
    Call<String>getTinhTrangMuaHangCuaSP(@Field("manv") String manv,@Field("masp") String masp);

    //cap nhap luot thich san pham
    @FormUrlEncoded
    @POST("capnhapluotthichsanpham.php")
    Call<String>capNhapLuotThichSP(@Field("masp") String masp,@Field("manv") String manv);

    //get san pham khac cua shop
    @FormUrlEncoded
    @POST("getsanphamkhaccuashop.php")
    Call<List<SanPham>>getSanPhamKhacCuaShop(@Field("mashop") String mashop);

    //Them hoa don
    @FormUrlEncoded
    @POST("themhoadon.php")
    Call<String>themHoaDon(@Field("giohangs") JSONArray jsonArray, @Field("tongtiens") String tongTiens
            , @Field("mashops") String maShops, @Field("loinhans") String loinhans
            , @Field("manv") String manv, @Field("tennguoinhan") String tennguoinhan, @Field("sodt") String sodt
            , @Field("diachi") String diachi,@Field("tongtien") String tongtien,@Field("vanchuyen") String vanchuyen,@Field("thanhtoan") String thanhtoan);

    //Xac nhan chuyen khoan
    //Them hoa don
    @FormUrlEncoded
    @POST("xacnhanchuyenkhoan.php")
    Call<String>themChuyenKhoan(@Field("mahdtong") String mahdtong, @Field("makh") String makh
            , @Field("ngayck") String ngayck, @Field("nganhangck") String nganhangck
            , @Field("stkck") String stkck, @Field("nganhangnhan") String nganhangnhan, @Field("stknhan") String stknhan
            , @Field("sotienchuyen") String sotienchuyen,@Field("ghichuck") String ghichuck,@Field("hinhck") String hinhck);


    //Xoa san pham duoc mua gio hang khi thnh toan thanh cong
    @FormUrlEncoded
    @POST("xoasanphamduocmuatronggiohang.php")
    Call<String>xoaSPGioHangVaCapNhapLuotMua(@Field("manv") String manv,@Field("s1masp") String s1masp,@Field("s1slmua") String s1slmua);

    //get data man hinh thanh toan thanh cong
    @FormUrlEncoded
    @POST("getdatamanhinhdathangthanhcong.php")
    Call<List<ChiTietDonHang>>getDataDatHangThanhCong(@Field("mahdtong") String mahdtong);

    //get san pham theo shop
    @FormUrlEncoded
    @POST("getsanphamtheoshop.php")
    Call<List<SanPham>>getSanPhamTheoShop(@Field("manv") String manv, @Field("limit") int limit);

    //get san pham chờ xác nhận admin
    @FormUrlEncoded
    @POST("getsanphamchoxacnhan.php")
    Call<List<SanPham>>getSanPhamChoXacNhan(@Field("limit") int limit);

    //cập nhập trạng thái sp
    @FormUrlEncoded
    @POST("adminxacnhansanpham.php")
    Call<String>capNhapTrangThaiSP(@Field("masp") String masp);

    //get don hang shop
    @FormUrlEncoded
    @POST("getdonhangshop.php")
    Call<List<QuanLyDonHangShop>>getDonHangShop(@Field("manv") String manv);

    //get don hang admin
    @GET("getdonhangadmin.php")
    Call<List<QuanLyDonHangShop>>getDonHangAdmin();

    //get add chuyen khoan
    @GET("getchuyenkhoan.php")
    Call<List<ChuyenKhoan>>getAllChuyenKhoan();
    //cap nhap trang thai don hang
    @FormUrlEncoded
    @POST("capnhaptrangthaidonhang.php")
    Call<String>capNhapTrangThaiDonHang(@Field("mahd") String mahd,@Field("masp") String masp,@Field("trangthai") String trangthai);

    //cap nhap trang thai chuyen khoan
    @FormUrlEncoded
    @POST("capnhaptrangthaichuyenkhoan.php")
    Call<String>capNhapChuyenKhoan(@Field("mahdtong") String mahdtong);

    //get don hang khach hang
    @FormUrlEncoded
    @POST("getdonhangkhachhang.php")
    Call<List<QuanLyDonHangShop>>getDonHangKhachHang(@Field("manv") String manv);

    //get san pham shop moi nhat
    @FormUrlEncoded
    @POST("getsanphamshop.php")
    Call<List<SanPham>>getSanPhamShop(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop ban chay
    @FormUrlEncoded
    @POST("getsanphamshopbanchay.php")
    Call<List<SanPham>>getSanPhamShopBanChay(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop gia tang
    @FormUrlEncoded
    @POST("getsanphamshopgiatang.php")
    Call<List<SanPham>>getSanPhamShopGiaTang(@Field("tennv") String tennv,@Field("limit") int limit);

    //get san pham shop gia giam
    @FormUrlEncoded
    @POST("getsanphamshopgiagiam.php")
    Call<List<SanPham>>getSanPhamShopGiaGiam(@Field("tennv") String tennv,@Field("limit") int limit);

    //tim kiem san pham trang chu
    @FormUrlEncoded
    @POST("timkiemsanphamtrangchu.php")
    Call<List<SanPham>>timKiemSPTrangChu(@Field("tensp") String tensp,@Field("limit") int limit);

    //tim kiem san pham danh muc
    @FormUrlEncoded
    @POST("timkiemsanphamdanhmuc.php")
    Call<List<SanPham>>timKiemSPDanhMuc(@Field("tensp") String tensp,@Field("maloaisp") String maloaisp,@Field("limit") int limit);

    //tim kiem san pham shop
    @FormUrlEncoded
    @POST("timkiemsanphamshop.php")
    Call<List<SanPham>>timKiemSPShop(@Field("tensp") String tensp,@Field("manv") String manv,@Field("limit") int limit);

    //thêm tìm kiếm theo nhân viên
    @FormUrlEncoded
    @POST("themtimkiem.php")
    Call<String>themTimKiem(@Field("manv") String manv,@Field("noidung") String noidung);

    //thêm tìm kiếm phổ biến
    @FormUrlEncoded
    @POST("themtimkiemphobien.php")
    Call<String>themTimKiemPhoBien(@Field("noidung") String noidung);


    //them san pham da xem
    @FormUrlEncoded
    @POST("capnhapluotxem.php")
    Call<String>capNhapLuotXem(@Field("masp") String masp,@Field("manv") String manv);

    //get san pham da xem
    @FormUrlEncoded
    @POST("getsanphamdaxem.php")
    Call<List<SanPham>>getSanPhamDaXem(@Field("manv") String manv,@Field("limit") int limit);

    //get san pham yeu thich tab tai khoan
    @FormUrlEncoded
    @POST("getsanphamyeuthichtabtaikhoan.php")
    Call<List<SanPham>>getSanPhamYeuThichTabTaiKhoan(@Field("manv") String manv,@Field("limit") int limit);

    //gui file len sv
    @Multipart
    @POST("uploadhinhanh.php")
    Call<String>upLoadHinhAnh(@Part MultipartBody.Part hinh);

    //upload hình loaisp
    @Multipart
    @POST("uploadhinhloaisanpham.php")
    Call<String>upLoadHinhLoaiSP(@Part MultipartBody.Part icon,@Part MultipartBody.Part hinh);

    //them loai sp
    @FormUrlEncoded
    @POST("themloaisanpham.php")
    Call<String>themLoaiSP(@Field("loaisp") String loaisp,@Field("icon") String icon,@Field("hinh") String hinh);

    //xoa loai sp
    @FormUrlEncoded
    @POST("xoaloaisanpham.php")
    Call<String>xoaLoaiSP(@Field("maloaisp") String maloaisp,@Field("icon") String icon,@Field("hinh") String hinh);

    //xoa hinh loai sp
    @FormUrlEncoded
    @POST("xoahinhloaisanpham.php")
    Call<String>xoaHinhLoaiSP(@Field("icon") String icon,@Field("hinh") String hinh);

    //sua loai sp
    @FormUrlEncoded
    @POST("sualoaisanpham.php")
    Call<String>suaLoaiSP(@Field("maloaisp") String maloaisp,@Field("loaisp") String loaisp,@Field("icon") String icon,@Field("hinh") String hinh);

    //upload hinh chuyen khoan
    @Multipart
    @POST("uploadhinhanhchuyenkhoan.php")
    Call<String>upLoadHinhAnhCK(@Part MultipartBody.Part hinh);

    //update thong tin tai khoan
    @FormUrlEncoded
    @POST("capnhapthongtintaikhoan.php")
    Call<String>capNhapThongTinTaiKhoan(@Field("manv") String manv,@Field("hoten") String hoten,@Field("sodt") String sodt,@Field("ngaysinh") String ngaysinh,@Field("gioitinh") String gioitinh,@Field("hinh") String hinh,@Field("diachi") String diachi);

    //upload multiple
    @Multipart
    @POST("uploadhinhsanpham.php")
    Call<String> uploadMultipleFilesDynamic(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part List<MultipartBody.Part> files);

    //add product
    @FormUrlEncoded
    @POST("addproduct.php")
    Call<String>addProduct(@Field("tensp") String tensp,@Field("gia") String gia,@Field("khuyenmai") String khuyenmai,
                           @Field("anhlon") String anhlon,@Field("anhnho") String anhnho,@Field("thongtin") String thongtin,
                           @Field("soluong") String soluong,@Field("maloaisp") String maloaisp,@Field("manv") String manv,
                           @Field("mausac") String mausac,@Field("kichthuoc") String kichthuoc);

    //edit product
    @FormUrlEncoded
    @POST("editproduct.php")
    Call<String>editProduct(@Field("masp") String masp,@Field("tensp") String tensp,@Field("gia") String gia,
                           @Field("khuyenmai") String khuyenmai,@Field("thongtin") String thongtin,
                           @Field("soluong") String soluong,@Field("mausac") String mausac,
                            @Field("kichthuoc") String kichthuoc, @Field("maloaisp") String maloaisp);

    //delete product
    @FormUrlEncoded
    @POST("deleteproduct.php")
    Call<String>deleteProduct(@Field("masp") String masp);

    //get loai lam dep
    @FormUrlEncoded
    @POST("getloailamdep.php")
    Call<List<LoaiLamDep>>getLoaiLamDep(@Field("malamdep") String malamdep);

    //get danh sach bai viet lam dep
    @FormUrlEncoded
    @POST("getdanhsachbaivietlamdep.php")
    Call<List<BaiVietLamDep>>getDanhSachBaiVietLamDep(@Field("maloailamdep") String maloailamdep);

    //get tinh trang luu bai viet lam dep
    @FormUrlEncoded
    @POST("gettinhtrangluutrubaivietlamdep.php")
    Call<String>getTinhTrangLuuBaiVietLamDep(@Field("mabaiviet") String mabaiviet,@Field("manv") String manv);

    //cap nhap tinh trang luu bai viet lam dep
    @FormUrlEncoded
    @POST("capnhaptinhtrangluutrubaivietlamdep.php")
    Call<String>capNhapLuuTruBaiViet(@Field("mabaiviet") String mabaiviet,@Field("manv") String manv);

    //get danh sach bai viet lam dep luu tru
    @FormUrlEncoded
    @POST("getbaivietlamdepluutru.php")
    Call<List<BaiVietLamDep>>getBaiVietLamDepLuuTru(@Field("manv") String manv);

    //get doanh thu
    @FormUrlEncoded
    @POST("getdoanhthu.php")
    Call<List<DoanhThu>>getDoanhThu(@Field("manv") String manv,@Field("tungay") String tungay,@Field("denngay") String denngay);

    @GET("recommend/{id}/{numItem}")
    Call<List<SanPham>>getSPGoiYRS(@Path("id") String id, @Path("numItem") String numItem);

    //bat dau xem sp
   // @FormUrlEncoded
   // @POST("recommend/viewed/start/")
    @GET("recommend/viewed/start/{userId}/{itemId}")
    Call<List<SanPham>> startXemSPHistory(@Path("userId") String userId, @Path("itemId") String itemId);


    //ket thuc xem sp
    //@FormUrlEncoded
   //@POST("recommend/viewed/end")
    @GET("recommend/viewed/end/{userId}/{itemId}")
    Call<List<SanPham>> endXemSPHistory(@Path("userId") String userId,@Path("itemId") String itemId);

    //logout
    //@FormUrlEncoded
   // @POST("recommend/viewed/logout")
    @GET("recommend/viewed/logout/{userId}/{itemId}")
    Call<List<SanPham>> logoutXemSPHistory(@Path("userId") String userId,@Path("itemId") String itemId);


}
