package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Adapter.DanhGiaAdapter;
import ducthuan.com.lamdep.Adapter.SanPhamGoiYAdapter;
import ducthuan.com.lamdep.Adapter.SanPhamKhacCuaShopAdapter;
import ducthuan.com.lamdep.Adapter.SlideViewPagerAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.ChiTietSanPham;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingCTSP;
    AppBarLayout appbarLayout;

    LinearLayout linearLayout;
    RelativeLayout relativeLayoutTime;

    ViewPager viewPagerCTSP;
    CircleIndicator indicatorChiTietSP;
    SlideViewPagerAdapter slideViewPagerAdapter;
    ArrayList<String> dsHinh = new ArrayList<>();

    ArrayList<String> dsMauSac = new ArrayList<>();
    ArrayList<String> dsKichThuoc = new ArrayList<>();

    SanPham sanPham;

    TextView txtTimeGio,txtTimePhut, txtTimeGiay;
    TextView txtTenSanPham, txtPhanTramKM, txtGiaSanPham, txtGiaSPChuaKM, txtSoSao, txtLuotMuaCTSP, txtTitleDGSP, txtGioHang;
    TextView txtVietDanhGia, txtXemTatCaNhanXet, txtTenCHDongGoi, txtThongTinChiTiet, txtXemThemCoTheBanThich
            , txtXemThemSanPhamKhac, txtXemThemChiTiet;
    LinearLayout lnThongSoKyThuat, lnXemThemChiTiet;
    ImageView imgYeuThich, imXemThemChiTiet;

    RecyclerView recyclerDanhGiaChiTiet, rvCoTheBanThich, rvCacSPKhacCuaShop;
    ArrayList<SanPham>sanPhamKhacShop;
    SanPhamKhacCuaShopAdapter sanPhamKhacCuaShopAdapter;
    SanPhamGoiYAdapter sanPhamGoiYAdapter;
    ArrayList<SanPham>sanPhamCoTheBanThich;

    DanhGiaAdapter danhGiaAdapter;
    ArrayList<DanhGia> danhGias;

    RatingBar ratingBar;

    Button btnMuaNgay,btnXemShop;
    ImageButton imThemGioHang,btnChatShop;


    boolean sochitiet = false;
    SharedPreferences sharedPreferences;
    String kiemtratrangthai = "";
    String tennv = "";
    String spfs = "";
    String userid = "";

    int tongsl = 1;
    int conlai = 0;

    boolean onpause = false;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        GetIntent();
        addControls();
        dongHoFlashSale();
        xuLyHienThiRating();
        xuLyHienThiVietDanhGia();
        getSanPhamKhacCuaShop();
        getCoTheBanThich();
        addEvents();
    }

    private void xuLyHienThiRating() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.getDanhGiaTheoSP(sanPham.getMASP());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("0")){
                    ratingBar.setVisibility(View.GONE);
                    txtSoSao.setText("Chưa có đánh giá");
                }else{
                    float rt = Float.parseFloat(kq);
                    ratingBar.setRating(rt);
                    ratingBar.setVisibility(View.VISIBLE);
                    txtSoSao.setText(kq);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void addControls() {
        //actionBar
        toolbar = findViewById(R.id.toolbar);
        collapsingCTSP = findViewById(R.id.collapsingCTSP);
        btnXemShop = findViewById(R.id.btnXemShop);
        btnChatShop = findViewById(R.id.btnChatShop);
        relativeLayoutTime = findViewById(R.id.relativeLayoutTime);

        //time flash sale
        txtTimeGio = findViewById(R.id.txtTimeGio);
        txtTimePhut = findViewById(R.id.txtTimePhut);
        txtTimeGiay = findViewById(R.id.txtTimeGiay);
        //
        txtTenSanPham = findViewById(R.id.txtTenSanPham);
        txtPhanTramKM = findViewById(R.id.txtPhanTramKM);
        txtGiaSanPham = findViewById(R.id.txtGiaSanPham);
        txtGiaSPChuaKM = findViewById(R.id.txtGiaSPChuaKM);
        txtSoSao = findViewById(R.id.txtSoSao);
        txtTitleDGSP = findViewById(R.id.txtTitleDGSP);
        txtLuotMuaCTSP = findViewById(R.id.txtLuotMuaCTSP);
        txtVietDanhGia = findViewById(R.id.txtVietDanhGia);
        txtXemTatCaNhanXet = findViewById(R.id.txtXemTatCaNhanXet);
        txtTenCHDongGoi = findViewById(R.id.txtTenCHDongGoi);
        txtThongTinChiTiet = findViewById(R.id.txtThongTinChiTiet);
        lnThongSoKyThuat = findViewById(R.id.lnThongSoKyThuat);
        imgYeuThich = findViewById(R.id.imgYeuThich);
        recyclerDanhGiaChiTiet = findViewById(R.id.recyclerDanhGiaChiTiet);
        btnMuaNgay = findViewById(R.id.btnMuaNgay);
        imThemGioHang = findViewById(R.id.imThemGioHang);

        txtXemThemCoTheBanThich = findViewById(R.id.txtXemThemCoTheBanThich);
        txtXemThemSanPhamKhac = findViewById(R.id.txtXemThemSanPhamKhac);

        txtXemThemChiTiet = findViewById(R.id.txtXemThemChiTiet);
        imXemThemChiTiet = findViewById(R.id.imXemThemChiTiet);
        lnXemThemChiTiet = findViewById(R.id.lnXemThemChiTiet);
        linearLayout = findViewById(R.id.linearLayout);


        rvCoTheBanThich = findViewById(R.id.rvCoTheBanThich);
        rvCacSPKhacCuaShop = findViewById(R.id.rvCacSPKhacCuaShop);
        ratingBar = findViewById(R.id.ratingBar);

        appbarLayout = findViewById(R.id.appbarLayout);

        if(spfs.equals("123") || sanPham.getMATHUONGHIEU().equals("123")){
            relativeLayoutTime.setVisibility(View.VISIBLE);
        }

        //get share preferrent
        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        kiemtratrangthai = sharedPreferences.getString("manv", "");
        tennv = sharedPreferences.getString("tennv","");
        if(!kiemtratrangthai.equals("")){
            getTinhTrangYeuThich();
        }

        //khởi tạo controls
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        //màu khi mở rộng
        collapsingCTSP.setExpandedTitleColor(Color.WHITE);
        //màu khi thu gọn
        collapsingCTSP.setCollapsedTitleTextColor(Color.WHITE);

        //Chỉ hiển thị tiêu đề khi thanh toolbar được thu gọn
        collapsingCTSP.setTitle(" ");
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingCTSP.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingCTSP.setTitle("");
                    isShow = false;
                }
            }
        });

        //xu ly tach mau va kich thuoc
        String[] sMau = sanPham.getMAUSAC().split(",");
        for (int i = 0; i < sMau.length; i++) {
            dsMauSac.add(sMau[i]);
        }

        String[] sKT = sanPham.getKICHTHUOC().split(",");
        for (int i = 0; i < sKT.length; i++) {
            dsKichThuoc.add(sKT[i]);
        }

        //viewPager
        viewPagerCTSP = findViewById(R.id.viewPagerCTSP);
        indicatorChiTietSP = findViewById(R.id.indicatorChiTietSP);
        if (dsHinh.size() > 0) {
            slideViewPagerAdapter = new SlideViewPagerAdapter(ChiTietSanPhamActivity.this, dsHinh);
            viewPagerCTSP.setAdapter(slideViewPagerAdapter);
            indicatorChiTietSP.setViewPager(viewPagerCTSP);
        }

        //hien thi san pham
        txtTenSanPham.setText(sanPham.getTENSP().trim());
        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int giasp = Integer.parseInt(sanPham.getGIA());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        if (km == 0) {
            txtPhanTramKM.setVisibility(View.GONE);
            txtGiaSPChuaKM.setVisibility(View.GONE);
            txtGiaSanPham.setText(decimalFormat.format(giasp) + "đ");
        } else if (km > 0) {
            int gkm = (giasp / 100) * (100 - km);
            txtPhanTramKM.setText("-" + km + "%");
            txtGiaSPChuaKM.setText(decimalFormat.format(giasp) + "đ");
            txtGiaSPChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtGiaSanPham.setText(decimalFormat.format(gkm) + "đ");
        }
        txtLuotMuaCTSP.setText("Đã bán " + sanPham.getLUOTMUA());

        //phần đóng gói
        StringTokenizer stringTokenizer = new StringTokenizer(sanPham.getTENNV(),"@");
        txtTenCHDongGoi.setText(stringTokenizer.nextToken());
        if(tennv.equals(sanPham.getTENNV())){
            linearLayout.setVisibility(View.GONE);
        }

        //phần chi tiết sp
        if (sanPham.getTHONGTIN().length() < 100) {
            txtThongTinChiTiet.setText(sanPham.getTHONGTIN());
            lnXemThemChiTiet.setVisibility(View.GONE);
        } else {
            txtThongTinChiTiet.setText(sanPham.getTHONGTIN().substring(0, 100));
            lnXemThemChiTiet.setVisibility(View.VISIBLE);
            lnXemThemChiTiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sochitiet = !sochitiet;
                    if (sochitiet) {
                        txtThongTinChiTiet.setText(sanPham.getTHONGTIN());
                        txtXemThemChiTiet.setText("Thu gọn");
                        imXemThemChiTiet.setImageResource(R.drawable.ic_keyboard_arrow_up_print_24dp);
                        HienThiThongTinKyThuat();
                    } else {
                        txtThongTinChiTiet.setText(sanPham.getTHONGTIN().substring(0, 100));
                        txtXemThemChiTiet.setText("Xem thêm");
                        imXemThemChiTiet.setImageResource(R.drawable.ic_keyboard_arrow_down_print_24dp);
                        lnThongSoKyThuat.setVisibility(View.GONE);
                    }
                }
            });
        }

        //Phan danh gia
        DataService dataGetDGs = APIService.getService();
        Call<List<DanhGia>> callback = dataGetDGs.getDanhGiaCTSP(sanPham.getMASP());
        callback.enqueue(new Callback<List<DanhGia>>() {
            @Override
            public void onResponse(Call<List<DanhGia>> call, Response<List<DanhGia>> response) {
                danhGias = (ArrayList<DanhGia>) response.body();

                if (danhGias.size() > 0) {

                    txtXemTatCaNhanXet.setText("Xem tất cả (" + danhGias.size() + ")");
                    ArrayList<DanhGia> danhGias1 = new ArrayList<>();
                    danhGias1.add(danhGias.get(0));
                    danhGiaAdapter = new DanhGiaAdapter(ChiTietSanPhamActivity.this, danhGias1);
                    recyclerDanhGiaChiTiet.setLayoutManager(new LinearLayoutManager(ChiTietSanPhamActivity.this));
                    recyclerDanhGiaChiTiet.setHasFixedSize(true);
                    recyclerDanhGiaChiTiet.setNestedScrollingEnabled(true);
                    recyclerDanhGiaChiTiet.setAdapter(danhGiaAdapter);

                    txtXemTatCaNhanXet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChiTietSanPhamActivity.this, DanhGiaActivity.class);
                            intent.putExtra("danhgias", danhGias);
                            startActivity(intent);
                        }
                    });
                } else {
                    txtTitleDGSP.setText("Chưa có đánh giá");
                    txtXemTatCaNhanXet.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<DanhGia>> call, Throwable t) {

            }
        });

    }


    private void dongHoFlashSale() {

        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        int giay = calendar.get(Calendar.SECOND);
        int tongs = 0;
        if(gio >= 0 && gio <= 7){
            tongs = 8*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 8 && gio <= 15){
            tongs = 16*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 16 && gio <= 23){
            tongs = 24*60*60 - (gio*60*60 + phut * 60 + giay);
        }

        new CountDownTimer(tongs * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                txtTimeGio.setText(String.format("%02d", hours));
                txtTimePhut.setText(String.format("%02d", minutes));
                txtTimeGiay.setText(String.format("%02d", seconds));
            }

            public void onFinish() {
                this.start();
            }
        }.start();
    }

    private void xuLyHienThiVietDanhGia() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.getTinhTrangMuaHangCuaSP(kiemtratrangthai,sanPham.getMASP());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                Log.d("kqdg",kq);
                if(kq.equals("Đã giao")){
                    txtVietDanhGia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void getCoTheBanThich() {

        sanPhamCoTheBanThich = new ArrayList<>();
        rvCoTheBanThich.setLayoutManager(new GridLayoutManager(ChiTietSanPhamActivity.this,2,RecyclerView.VERTICAL,false));
        rvCoTheBanThich.setHasFixedSize(true);
        rvCoTheBanThich.setNestedScrollingEnabled(true);

        DataService dataService = APIService.getService();
        Call<List<SanPham>>call = dataService.getSPCoTheBanThich(sanPham.getMASP());
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamCoTheBanThich = (ArrayList<SanPham>) response.body();
                if(sanPhamCoTheBanThich.size()>0){
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(ChiTietSanPhamActivity.this,sanPhamCoTheBanThich);
                    rvCoTheBanThich.setAdapter(sanPhamGoiYAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }

    private void getSanPhamKhacCuaShop() {

        sanPhamKhacShop = new ArrayList<>();
        rvCacSPKhacCuaShop.setLayoutManager(new GridLayoutManager(ChiTietSanPhamActivity.this,1,RecyclerView.HORIZONTAL,false));
        rvCacSPKhacCuaShop.setHasFixedSize(true);
        rvCacSPKhacCuaShop.setNestedScrollingEnabled(true);
        DataService dataService = APIService.getService();
        Call<List<SanPham>>call = dataService.getSanPhamKhacCuaShop(sanPham.getMANV());
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamKhacShop = (ArrayList<SanPham>) response.body();
                if(sanPhamKhacShop.size()>0){
                    sanPhamKhacCuaShopAdapter = new SanPhamKhacCuaShopAdapter(ChiTietSanPhamActivity.this,sanPhamKhacShop);
                    rvCacSPKhacCuaShop.setAdapter(sanPhamKhacCuaShopAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });


    }

    private void HienThiThongTinKyThuat() {
        //get data chi tiết sản phẩm
        DataService dataCTSP = APIService.getService();
        Call<List<ChiTietSanPham>> callback = dataCTSP.getChiTietSanPham(sanPham.getMASP());
        callback.enqueue(new Callback<List<ChiTietSanPham>>() {
            @Override
            public void onResponse(Call<List<ChiTietSanPham>> call, Response<List<ChiTietSanPham>> response) {
                ArrayList<ChiTietSanPham> chiTietSanPhams = (ArrayList<ChiTietSanPham>) response.body();
                if (chiTietSanPhams.size() > 0) {
                    lnThongSoKyThuat.setVisibility(View.VISIBLE);
                    for (int i = 0; i < chiTietSanPhams.size(); i++) {
                        LinearLayout linearLayout = new LinearLayout(ChiTietSanPhamActivity.this);
                        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);


                        TextView txtTenThongSoKyThuat = new TextView(ChiTietSanPhamActivity.this);
                        txtTenThongSoKyThuat.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                        txtTenThongSoKyThuat.setTypeface(null, Typeface.BOLD);
                        txtTenThongSoKyThuat.setText(chiTietSanPhams.get(i).getTENCHITIET());

                        TextView txtGiaTriThongSo = new TextView(ChiTietSanPhamActivity.this);
                        txtGiaTriThongSo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                        txtGiaTriThongSo.setText(chiTietSanPhams.get(i).getGIATRI());

                        linearLayout.addView(txtTenThongSoKyThuat);
                        linearLayout.addView(txtGiaTriThongSo);
                        lnThongSoKyThuat.addView(linearLayout);
                    }
                } else {
                    lnThongSoKyThuat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietSanPham>> call, Throwable t) {

            }
        });

    }


    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //xử lý click yêu thích
        imgYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kiemtratrangthai.equals("")){
                    startActivity(new Intent(ChiTietSanPhamActivity.this,DangNhapActivity.class));
                }else{
                    DataService dataService = APIService.getService();
                    Call<String>callback = dataService.capNhapLuotThichSP(sanPham.getMASP(),kiemtratrangthai);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq = response.body();
                            if(kq.equals("DaThich")){
                                Toast.makeText(ChiTietSanPhamActivity.this, "Đã thích", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ChiTietSanPhamActivity.this, "Bỏ thích", Toast.LENGTH_SHORT).show();
                            }

                            getTinhTrangYeuThich();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });


        //đến shop bán hàng
        btnXemShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSanPhamActivity.this,ShopActivity.class);
                intent.putExtra("tennv",sanPham.getTENNV());
                startActivity(intent);
            }
        });



        txtVietDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietSanPhamActivity.this);
                builder.setTitle("Viết đánh giá");
                builder.setIcon(R.drawable.write_rating_pink_24dp);
                builder.setView(R.layout.custom_layout_danhgia);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                //anh xa view
                final RatingBar ratingBarDG = dialog.findViewById(R.id.ratingBarDG);
                final TextInputLayout txtTieuDeDG = dialog.findViewById(R.id.txtTieuDeDG);
                final TextInputLayout txtNoiDungDG = dialog.findViewById(R.id.txtNoiDungDG);
                Button btnDongYDG = dialog.findViewById(R.id.btnDongYDG);
                Button btnHuyDG = dialog.findViewById(R.id.btnHuyDG);

                //sư kiện hủy đánh giá
                btnHuyDG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                //sự kiện đồng ý đánh giá
                btnDongYDG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!kiemTraTieuDeNoiDung(txtTieuDeDG) | !kiemTraTieuDeNoiDung(txtNoiDungDG)) {
                            return;
                        }

                        String tieude = txtTieuDeDG.getEditText().getText().toString().trim();
                        String noidung = txtNoiDungDG.getEditText().getText().toString().trim();
                        float fsosao = ratingBarDG.getRating();
                        String masp = sanPham.getMASP();
                        String manv = sharedPreferences.getString("manv", "");
                        DataService dataDG = APIService.getService();
                        Call<String> callback = dataDG.ketQuaThemDanhGia(masp, manv, tieude, noidung, String.valueOf(fsosao));
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketquathemdg = response.body();
                                if (ketquathemdg.equals("ThemThanhCong") || ketquathemdg.equals("SuaThanhCong")) {
                                    Toast.makeText(ChiTietSanPhamActivity.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();

                                    //Phan danh gia
                                    DataService dataGetDGs = APIService.getService();
                                    Call<List<DanhGia>> callback = dataGetDGs.getDanhGiaCTSP(sanPham.getMASP());
                                    callback.enqueue(new Callback<List<DanhGia>>() {
                                        @Override
                                        public void onResponse(Call<List<DanhGia>> call, Response<List<DanhGia>> response) {
                                            danhGias = (ArrayList<DanhGia>) response.body();

                                            if (danhGias.size() > 0) {
                                                txtTitleDGSP.setText("Đánh giá sản phẩm");
                                                txtXemTatCaNhanXet.setVisibility(View.VISIBLE);
                                                txtXemTatCaNhanXet.setText("Xem tất cả (" + danhGias.size() + ")");
                                                ArrayList<DanhGia> danhGias1 = new ArrayList<>();
                                                danhGias1.add(danhGias.get(0));
                                                danhGiaAdapter = new DanhGiaAdapter(ChiTietSanPhamActivity.this, danhGias1);
                                                recyclerDanhGiaChiTiet.setLayoutManager(new LinearLayoutManager(ChiTietSanPhamActivity.this));
                                                recyclerDanhGiaChiTiet.setHasFixedSize(true);
                                                recyclerDanhGiaChiTiet.setNestedScrollingEnabled(true);
                                                recyclerDanhGiaChiTiet.setAdapter(danhGiaAdapter);

                                                txtXemTatCaNhanXet.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(ChiTietSanPhamActivity.this, DanhGiaActivity.class);
                                                        intent.putExtra("danhgias", danhGias);
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                txtTitleDGSP.setText("Chưa có đánh giá");
                                                txtXemTatCaNhanXet.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<DanhGia>> call, Throwable t) {

                                        }
                                    });


                                } else {
                                    Toast.makeText(ChiTietSanPhamActivity.this, "Đánh giá thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                        dialog.dismiss();
                    }
                });

            }
        });

        //Them gio hang
        imThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dau su kien click
                sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
                String kiemtratrangthai = sharedPreferences.getString("manv", "");
                if (kiemtratrangthai.equals("") || kiemtratrangthai.equals(null)) {
                    startActivity(new Intent(ChiTietSanPhamActivity.this, DangNhapActivity.class));
                } else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChiTietSanPhamActivity.this);
                    builder1.setView(R.layout.custom_layout_themgiohang);
                    final AlertDialog dialog1 = builder1.create();
                    dialog1.show();

                    //anh xa
                    ImageView imgHinhSP = dialog1.findViewById(R.id.imgHinhSP);
                    ImageView imgThoat = dialog1.findViewById(R.id.imgThoat);
                    TextView txtGiaSP = dialog1.findViewById(R.id.txtGiaSP);
                    TextView txtSoLuongSP = dialog1.findViewById(R.id.txtSoLuongSP);
                    final RadioGroup rgMau = dialog1.findViewById(R.id.rgMau);
                    final RadioGroup rgKichThuoc = dialog1.findViewById(R.id.rgKichThuoc);
                    final ImageButton btnGiam = dialog1.findViewById(R.id.btnGiam);
                    final ImageButton btnTang = dialog1.findViewById(R.id.btnTang);
                    final TextView txtSoLuong = dialog1.findViewById(R.id.txtSoLuong);
                    Button btnThem = dialog1.findViewById(R.id.btnThem);

                    //xu ly
                    Picasso.with(ChiTietSanPhamActivity.this).load(TrangChuActivity.base_url + sanPham.getANHLON())
                            .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhSP);
                    txtGiaSP.setText(txtGiaSanPham.getText());
                    int ssll = Integer.parseInt(sanPham.getSOLUONG());
                    int llmm = Integer.parseInt(sanPham.getLUOTMUA());
                    conlai = ssll - llmm;
                    txtSoLuongSP.setText("Kho: " + conlai);

                    if(conlai == 0){
                        btnThem.setEnabled(false);
                        btnThem.setText("Hết hàng");
                    }

                    //xử lý thêm màu sắc kích thước
                    themMauSac(rgMau);
                    themKichThuoc(rgKichThuoc);


                    //xu ly tang giam so luong
                    tongsl = Integer.parseInt(txtSoLuong.getText().toString());
                    btnGiam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongsl > 2) {
                                btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
                                tongsl = tongsl - 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                            } else {
                                tongsl = 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                btnGiam.setImageResource(R.drawable.ic_tru_xam_24dp);
                            }
                            btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
                        }
                    });

                    btnTang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongsl >= conlai - 1) {
                                btnTang.setImageResource(R.drawable.ic_cong_xam_24dp);
                                tongsl = conlai;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                Toast.makeText(ChiTietSanPhamActivity.this, "Rất tiếc shop chỉ còn " + (tongsl) + " sản phẩm", Toast.LENGTH_SHORT).show();
                            } else {
                                tongsl += 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
                            }
                            btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
                        }
                    });


                    btnThem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String maspgh = sanPham.getMASP();
                            sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
                            String manvgh = sharedPreferences.getString("manv", "");

                            int vitrimausac = rgMau.getCheckedRadioButtonId();
                            RadioButton radioButton = rgMau.findViewById(vitrimausac);
                            String mausac = radioButton.getText().toString();

                            int vitrikichthuoc = rgKichThuoc.getCheckedRadioButtonId();
                            RadioButton radioButtonkt = rgKichThuoc.findViewById(vitrikichthuoc);
                            String kichthuoc = radioButtonkt.getText().toString();

                            String soluong = txtSoLuong.getText().toString();

                            DataService dataThemGioHang = APIService.getService();
                            Call<String> callbackThemGioHang = dataThemGioHang.themGioHang(maspgh, manvgh, mausac, kichthuoc, soluong);
                            callbackThemGioHang.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq = response.body();
                                    if (kq.equals("ThemThanhCong") || kq.equals("SuaThanhCong")) {
                                        getDataGioHang(txtGioHang);
                                        Toast.makeText(ChiTietSanPhamActivity.this, "Thêm thành công sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChiTietSanPhamActivity.this, "Thêm sản phẩm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog1.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {


                                }
                            });


                        }
                    });


                    imgThoat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    //cuoi su kien click
                }
            }
        });


        //xử lý mua ngay
        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dau su kien click
                sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
                String kiemtratrangthai = sharedPreferences.getString("manv", "");
                if (kiemtratrangthai.equals("")) {
                    startActivity(new Intent(ChiTietSanPhamActivity.this, DangNhapActivity.class));
                } else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChiTietSanPhamActivity.this);
                    builder1.setView(R.layout.custom_layout_themgiohang);
                    final AlertDialog dialog1 = builder1.create();
                    dialog1.show();

                    //anh xa
                    ImageView imgHinhSP = dialog1.findViewById(R.id.imgHinhSP);
                    ImageView imgThoat = dialog1.findViewById(R.id.imgThoat);
                    TextView txtGiaSP = dialog1.findViewById(R.id.txtGiaSP);
                    TextView txtSoLuongSP = dialog1.findViewById(R.id.txtSoLuongSP);
                    final RadioGroup rgMau = dialog1.findViewById(R.id.rgMau);
                    final RadioGroup rgKichThuoc = dialog1.findViewById(R.id.rgKichThuoc);
                    final ImageButton btnGiam = dialog1.findViewById(R.id.btnGiam);
                    final ImageButton btnTang = dialog1.findViewById(R.id.btnTang);
                    final TextView txtSoLuong = dialog1.findViewById(R.id.txtSoLuong);
                    Button btnThem = dialog1.findViewById(R.id.btnThem);
                    btnThem.setText("Mua ngay");

                    //xử lý thêm màu sắc kích thước
                    themMauSac(rgMau);
                    themKichThuoc(rgKichThuoc);

                    //xu ly
                    Picasso.with(ChiTietSanPhamActivity.this).load(TrangChuActivity.base_url + sanPham.getANHLON())
                            .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhSP);
                    txtGiaSP.setText(txtGiaSanPham.getText());
                    int ssll = Integer.parseInt(sanPham.getSOLUONG());
                    int llmm = Integer.parseInt(sanPham.getLUOTMUA());
                    conlai = ssll - llmm;
                    txtSoLuongSP.setText("Kho: " + conlai);
                    if(conlai == 0){
                        btnThem.setEnabled(false);
                        btnThem.setText("Hết hàng");
                    }
                    //xu ly tang giam so luong
                    tongsl = Integer.parseInt(txtSoLuong.getText().toString());
                    btnGiam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongsl > 2) {
                                btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
                                tongsl = tongsl - 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                            } else {
                                tongsl = 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                btnGiam.setImageResource(R.drawable.ic_tru_xam_24dp);
                            }
                            btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
                        }
                    });

                    btnTang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongsl >= conlai - 1) {
                                btnTang.setImageResource(R.drawable.ic_cong_xam_24dp);
                                tongsl = conlai;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                Toast.makeText(ChiTietSanPhamActivity.this, "Rất tiếc shop chỉ còn " + (tongsl) + " sản phẩm", Toast.LENGTH_SHORT).show();
                            } else {
                                tongsl += 1;
                                txtSoLuong.setText(String.valueOf(tongsl));
                                btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
                            }
                            btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
                        }
                    });


                    btnThem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String maspgh = sanPham.getMASP();
                            sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
                            String manvgh = sharedPreferences.getString("manv", "");

                            int vitrimausac = rgMau.getCheckedRadioButtonId();
                            RadioButton radioButton = rgMau.findViewById(vitrimausac);
                            String mausac = radioButton.getText().toString();

                            int vitrikichthuoc = rgKichThuoc.getCheckedRadioButtonId();
                            RadioButton radioButtonkt = rgKichThuoc.findViewById(vitrikichthuoc);
                            String kichthuoc = radioButtonkt.getText().toString();

                            String soluong = txtSoLuong.getText().toString();

                            DataService dataThemGioHang = APIService.getService();
                            Call<String> callbackThemGioHang = dataThemGioHang.themGioHang(maspgh, manvgh, mausac, kichthuoc, soluong);
                            callbackThemGioHang.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq = response.body();
                                    if (kq.equals("ThemThanhCong") || kq.equals("SuaThanhCong")) {
                                        xuLyMuaNgay();

                                    } else {
                                        Toast.makeText(ChiTietSanPhamActivity.this, "Thêm sản phẩm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog1.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {


                                }
                            });


                        }
                    });


                    imgThoat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.dismiss();
                        }
                    });

                    //cuoi su kien click
                }

            }
        });

        txtXemThemSanPhamKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSanPhamActivity.this,ShopActivity.class);
                intent.putExtra("tennv",sanPham.getTENNV());
                startActivity(intent);
            }
        });

        btnChatShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tennv.equals("")){
                    startActivity(new Intent(ChiTietSanPhamActivity.this,DangNhapActivity.class));
                }else {

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                User user = snapshot.getValue(User.class);
                                if(user.getEmail().equals(sanPham.getTENNV())){
                                    userid = user.getId();
                                    break;
                                }
                            }
                            Intent intent = new Intent(ChiTietSanPhamActivity.this,ChatActivity.class);
                            intent.putExtra("userid",userid);
                            startActivity(intent);
                            databaseReference.removeEventListener(this);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

    }

    private void themKichThuoc(RadioGroup rgKichThuoc) {
        for (int i = 0; i < dsKichThuoc.size(); i++) {
            RadioButton rd1 = new RadioButton(ChiTietSanPhamActivity.this);
            rd1.setId(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,5,0);
            rd1.setLayoutParams(layoutParams);
            rd1.setText(dsKichThuoc.get(i));
            rd1.setButtonDrawable(null);
            rd1.setPadding(10,0,10,0);
            rd1.setTextColor(getResources().getColor(R.color.mauden));
            rd1.setBackground(getResources().getDrawable(R.drawable.custom_radio_button_select));
            if(i == 0){
                rd1.setChecked(true);
                rd1.setTextColor(getResources().getColor(R.color.mauhong));
            }
            rgKichThuoc.addView(rd1);
        }

        rgKichThuoc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = null;

                for (int i1 = 0; i1 < dsKichThuoc.size(); i1++) {
                    if(i1 == i){
                        button = radioGroup.findViewById(i1);
                        button.setChecked(true);
                        button.setTextColor(getResources().getColor(R.color.mauhong));
                    }else {
                        button = radioGroup.findViewById(i1);
                        button.setChecked(false);
                        button.setTextColor(getResources().getColor(R.color.mauden));
                    }
                }
            }
        });

    }

    private void themMauSac(RadioGroup rgMau) {
        for (int i = 0; i < dsMauSac.size(); i++) {
            RadioButton rd1 = new RadioButton(ChiTietSanPhamActivity.this);
            rd1.setId(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,5,0);
            rd1.setLayoutParams(layoutParams);
            rd1.setText(dsMauSac.get(i));
            rd1.setButtonDrawable(null);
            rd1.setPadding(10,0,10,0);
            rd1.setTextColor(getResources().getColor(R.color.mauden));
            rd1.setBackground(getResources().getDrawable(R.drawable.custom_radio_button_select));
            if(i == 0){
                rd1.setChecked(true);
                rd1.setTextColor(getResources().getColor(R.color.mauhong));
            }
            rgMau.addView(rd1);
        }

        rgMau.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = null;

                for (int i1 = 0; i1 < dsMauSac.size(); i1++) {
                    if(i1 == i){
                        button = radioGroup.findViewById(i1);
                        button.setChecked(true);
                        button.setTextColor(getResources().getColor(R.color.mauhong));
                    }else {
                        button = radioGroup.findViewById(i1);
                        button.setChecked(false);
                        button.setTextColor(getResources().getColor(R.color.mauden));
                    }
                }
            }
        });

    }

    private void xuLyMuaNgay() {

        final String manv = sharedPreferences.getString("manv", "");
        final DataService dataService = APIService.getService();
        Call<List<DiaChiKhachHang>> callback = dataService.getDiaChiKhachHangs(manv);
        callback.enqueue(new Callback<List<DiaChiKhachHang>>() {
            @Override
            public void onResponse(Call<List<DiaChiKhachHang>> call, Response<List<DiaChiKhachHang>> response) {
                final ArrayList<DiaChiKhachHang> diaChiKhachHangs = (ArrayList<DiaChiKhachHang>) response.body();

                Call<List<GioHang>> callback = dataService.getSanPhamMuaNgay(sanPham.getMASP(), manv);
                callback.enqueue(new Callback<List<GioHang>>() {
                    @Override
                    public void onResponse(Call<List<GioHang>> call, Response<List<GioHang>> response) {
                        ArrayList<GioHang> gioHangDuocChon = (ArrayList<GioHang>) response.body();

                        if (gioHangDuocChon.size() > 0) {
                            if (diaChiKhachHangs.size() > 0) {
                                Intent intent = new Intent(ChiTietSanPhamActivity.this, XacNhanThongTinMuaHangActivity.class);
                                intent.putParcelableArrayListExtra("giohang", gioHangDuocChon);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ChiTietSanPhamActivity.this, DiaChiNhanHangActivity.class);
                                intent.putParcelableArrayListExtra("giohang", gioHangDuocChon);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(ChiTietSanPhamActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<GioHang>> call, Throwable t) {
                        Log.d("sss",t.toString());
                    }
                });


            }

            @Override
            public void onFailure(Call<List<DiaChiKhachHang>> call, Throwable t) {

            }
        });


    }

    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");
        if (manv.equals("")) {
            txtGH.setVisibility(View.GONE);
        } else {
            DataService dataService = APIService.getService();
            Call<String> callSLSP = dataService.getSoLuongSPGioHang(manv);
            callSLSP.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sl = response.body();

                    if (sl.equals("")) {
                        txtGH.setVisibility(View.GONE);
                    } else {
                        txtGH.setVisibility(View.VISIBLE);
                        txtGH.setText(sl);
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
    }

    public boolean kiemTraTieuDeNoiDung(TextInputLayout txtTDND) {
        String s = txtTDND.getEditText().getText().toString().trim();
        if (s.equals("")) {
            txtTDND.setError("Vui lòng nhập nội dung đánh giá");
            return false;
        } else {
            txtTDND.setError(null);
            txtTDND.setErrorEnabled(false);
            return true;
        }
    }




    private void getTinhTrangYeuThich() {
        //yêu thích
        DataService dataService = APIService.getService();
        Call<String>callback = dataService.getTinhTrangThichSP(sanPham.getMASP(), kiemtratrangthai);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("1")){
                    imgYeuThich.setImageResource(R.drawable.ic_liked_48dp);
                }else {
                    imgYeuThich.setImageResource(R.drawable.ic_like_48dp);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chitietsanpham, menu);

        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        final String manv = sharedPreferences.getString("manv", "");

        if(manv.equals(sanPham.getMANV())){
            MenuItem item = menu.findItem(R.id.itToCaoSP);
            item.setTitle("Sửa sản phẩm");
            item.setIcon(R.drawable.ic_edit_black_24dp);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(ChiTietSanPhamActivity.this,SuaSanPhamShopCuaToiActivity.class);
                    intent.putExtra("itemsp",sanPham);
                    startActivity(intent);
                    return false;
                }
            });
        }

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (manv.equals("")) {
                    startActivity(new Intent(ChiTietSanPhamActivity.this, DangNhapActivity.class));
                } else {
                    startActivity(new Intent(ChiTietSanPhamActivity.this, GioHangActivity.class));
                }
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onpause == true) {
            getDataGioHang(txtGioHang);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itTrangChu:
                startActivity(new Intent(ChiTietSanPhamActivity.this, TrangChuActivity.class));
                break;
        }

        return true;
    }


    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("itemsp")) {
            sanPham = intent.getParcelableExtra("itemsp");
            dsHinh.add(sanPham.getANHLON());

            String anhnhos = sanPham.getANHNHO();
            StringTokenizer stringTokenizer = new StringTokenizer(anhnhos, ",");
            while (stringTokenizer.hasMoreTokens()) {
                dsHinh.add(stringTokenizer.nextToken());
            }

            sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
            String manv = sharedPreferences.getString("manv","");
            if(!manv.equals("")){
                DataService dataService = APIService.getService();
                Call<String>callback = dataService.capNhapLuotXem(sanPham.getMASP(),manv);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
            String maNVPost = manv.equals("") ? "0" : manv;
            Call<List<SanPham>> startXemSP = APIService.getRSService().startXemSPHistory(maNVPost, sanPham.getMASP());
            startXemSP.enqueue(new Callback<List<SanPham>>() {
                @Override
                public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                   // System.out.println("SUCCEED");
                }
                @Override
                public void onFailure(Call<List<SanPham>> call, Throwable t) {
                    //System.out.println("FAILED");
                }
            });
        }
        if(intent.hasExtra("spfs")){
            spfs = intent.getStringExtra("spfs");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String manvGet = sharedPreferences.getString("manv","");

        if (manvGet.equals("")) manvGet = "0";
        //ket thuc xem sp
        Call<List<SanPham>> startXemSP = APIService.getRSService().endXemSPHistory(manvGet, sanPham.getMASP());
        startXemSP.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                // System.out.println("SUCCEED");
            }
            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                //System.out.println("FAILED");
            }
        });


    }
}
