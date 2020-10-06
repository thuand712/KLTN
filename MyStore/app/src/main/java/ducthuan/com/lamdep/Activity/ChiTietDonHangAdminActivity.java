package ducthuan.com.lamdep.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Adapter.ChiTietDonHangAdminAdapter;
import ducthuan.com.lamdep.Adapter.ChiTietDonHangShopAdapter;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangAdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<QuanLyDonHangShop>quanLyDonHangShops;
    ChiTietDonHangAdminAdapter chiTietDonHangShopAdapter;
    RecyclerView rvSanPham;


    Spinner spinnerTinhTrang;
    String []trangthais={"Chờ xác nhận","Đang giao","Đã giao","Đã hủy","Trả hàng"};
    ArrayAdapter adapter;


    TextView txtTenKH,txtSdt,txtDiaChi,txtPTVanChuyen,txtThanhToan,txtTenShop,txtGheThamShop,txtMaDH,txtNgayMua,txtNgayGiao,txtTongTien;
    TextView txtLoiNhan;
    Button btnCapNhapTrangThai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang_admin);
        Intent intent = getIntent();
        if(intent.hasExtra("donhang")){
            quanLyDonHangShops = intent.getParcelableArrayListExtra("donhang");
            addControls();
            addEvents();
        }else {
            Toast.makeText(this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtGheThamShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietDonHangAdminActivity.this,ShopActivity.class);
                intent.putExtra("tennv",quanLyDonHangShops.get(0).getTENSHOP());
                startActivity(intent);
            }
        });

        btnCapNhapTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mahd = txtMaDH.getText().toString();
                String trangthai = spinnerTinhTrang.getSelectedItem().toString();

                DataService dataService = APIService.getService();
                Call<String>callback = dataService.capNhapTrangThaiDonHang(mahd,quanLyDonHangShops.get(0).getMASP(),trangthai);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String ketqua = response.body();
                        if(ketqua.equals("OK")){
                            Toast.makeText(ChiTietDonHangAdminActivity.this, "Cập nhập trạng thái đơn hàng thành công !", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChiTietDonHangAdminActivity.this, "Cập nhập trạng thái đơn hàng thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ChiTietDonHangAdminActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }



    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        spinnerTinhTrang = findViewById(R.id.spinnerTinhTrang);
        btnCapNhapTrangThai = findViewById(R.id.btnCapNhapTrangThai);
        txtTenKH = findViewById(R.id.txtTenKH);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtPTVanChuyen = findViewById(R.id.txtPTVanChuyen);
        txtThanhToan = findViewById(R.id.txtThanhToan);
        txtTenShop = findViewById(R.id.txtTenShop);
        txtGheThamShop = findViewById(R.id.txtGheThamShop);
        txtMaDH = findViewById(R.id.txtMaDH);
        txtNgayMua = findViewById(R.id.txtNgayMua);
        txtNgayGiao = findViewById(R.id.txtNgayGiao);
        rvSanPham = findViewById(R.id.rvSanPham);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtLoiNhan = findViewById(R.id.txtLoiNhan);

        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        adapter = new ArrayAdapter(ChiTietDonHangAdminActivity.this,android.R.layout.simple_spinner_dropdown_item,trangthais);
        spinnerTinhTrang.setAdapter(adapter);


        //xử lý địa chỉ
        txtTenKH.setText(quanLyDonHangShops.get(0).getTENNGUOINHAN());
        txtSdt.setText(quanLyDonHangShops.get(0).getSODT());
        txtDiaChi.setText(quanLyDonHangShops.get(0).getDIACHI());

        //xử lý vận chuyển
        txtPTVanChuyen.setText(quanLyDonHangShops.get(0).getVANCHUYEN());
        txtNgayGiao.setText("Dự kiến giao hàng vào ngày "+quanLyDonHangShops.get(0).getNGAYGIAO());
        //xử lý thanh toán
        if(quanLyDonHangShops.get(0).getTHANHTOAN().equals("Chuyển khoản")){
            txtThanhToan.setText(quanLyDonHangShops.get(0).getTHANHTOAN()+" "+getResources().getString(R.string.endash)
                    +" "+ quanLyDonHangShops.get(0).getCHUYENKHOAN());
        }else {
            txtThanhToan.setText(quanLyDonHangShops.get(0).getTHANHTOAN());
        }
        //xử lý lời nhắn
        txtLoiNhan.setText(quanLyDonHangShops.get(0).getLOINHAN());

        StringTokenizer stringTokenizer = new StringTokenizer(quanLyDonHangShops.get(0).getTENSHOP(),"@");

        txtTenShop.setText(stringTokenizer.nextToken());

        //xu ly hien thi rv
        chiTietDonHangShopAdapter = new ChiTietDonHangAdminAdapter(ChiTietDonHangAdminActivity.this,quanLyDonHangShops);
        rvSanPham.setHasFixedSize(true);
        rvSanPham.setNestedScrollingEnabled(true);
        rvSanPham.setLayoutManager(new LinearLayoutManager(ChiTietDonHangAdminActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ChiTietDonHangAdminActivity.this,DividerItemDecoration.VERTICAL);
        rvSanPham.addItemDecoration(dividerItemDecoration);
        rvSanPham.setAdapter(chiTietDonHangShopAdapter);
        chiTietDonHangShopAdapter.notifyDataSetChanged();

        txtMaDH.setText(quanLyDonHangShops.get(0).getMAHD());
        txtNgayMua.setText(quanLyDonHangShops.get(0).getNGAYMUA());

        txtTongTien.setText(quanLyDonHangShops.get(0).getTONGTIEN());

        if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Chờ xác nhận")){
            spinnerTinhTrang.setSelection(0);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đang giao")){
            spinnerTinhTrang.setSelection(1);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đã giao")){
            spinnerTinhTrang.setSelection(2);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Đã hủy")){
            spinnerTinhTrang.setSelection(3);
        }else if(quanLyDonHangShops.get(0).getTRANGTHAI().equals("Trả hàng")){
            spinnerTinhTrang.setSelection(4);
        }

    }
}
