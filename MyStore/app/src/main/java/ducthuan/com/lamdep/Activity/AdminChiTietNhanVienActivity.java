package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminChiTietNhanVienActivity extends AppCompatActivity {

    MenuItem itKhoa,itMo,itLenAdmin,itXuongKH;
    Intent intent;
    NhanVien nhanVien;
    Toolbar toolbar;
    CircleImageView imgNV;
    TextView txtTenNV,txtEmail,txtSDT,txtNgaySinh,txtDiaChi,txtGioiTinh,txtNgayDangKy,txtLoaiNV,txtTrangThai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chi_tiet_nhan_vien);
        intent = getIntent();
        if(intent.hasExtra("nhanvien")){
            nhanVien = intent.getParcelableExtra("nhanvien");
            addControls();
            addEvents();
        }

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgNV = findViewById(R.id.imgNV);
        txtTenNV = findViewById(R.id.txtTenNV);
        txtEmail = findViewById(R.id.txtEmail);
        txtSDT = findViewById(R.id.txtSDT);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtGioiTinh = findViewById(R.id.txtGioiTinh);
        txtNgayDangKy = findViewById(R.id.txtNgayDangKy);
        txtLoaiNV = findViewById(R.id.txtLoaiNV);
        txtTrangThai = findViewById(R.id.txtTrangThai);

        if(nhanVien.getHINH()!=null){
            Picasso.with(AdminChiTietNhanVienActivity.this).load(TrangChuActivity.base_url+nhanVien.getHINH())
                    .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgNV);
        }
        txtTenNV.setText(nhanVien.getTENNV());
        txtEmail.setText(nhanVien.getTENDANGNHAP());
        if(nhanVien.getSODT()!=null){
            txtSDT.setText(nhanVien.getSODT());
        }
        if(nhanVien.getNGAYSINH()!=null){
            txtNgaySinh.setText(nhanVien.getNGAYSINH());
        }
        if(nhanVien.getDIACHI()!=null){
            txtDiaChi.setText(nhanVien.getDIACHI());
        }
        if(nhanVien.getGIOITINH()!=null){
            txtGioiTinh.setText(nhanVien.getGIOITINH());
        }
        txtNgayDangKy.setText(nhanVien.getNGAYDANGKY());
        if(nhanVien.getMALOAINV().equals("1")){
            txtLoaiNV.setText("Admin");
        }else {
            txtLoaiNV.setText("Khách hàng");
        }
        txtTrangThai.setText(nhanVien.getTRANGTHAINV());

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String emailnv = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("tennv","");
        if (emailnv.equals("admin@gmail.com") && !nhanVien.getTENDANGNHAP().equals("admin@gmail.com")) {
            getMenuInflater().inflate(R.menu.menu_nhanvien, menu);
            itKhoa = menu.findItem(R.id.itKhoa);
            itMo = menu.findItem(R.id.itMoKhoa);
            itLenAdmin = menu.findItem(R.id.itThangCapAdmin);
            itXuongKH = menu.findItem(R.id.itXuongKhachHang);

            if (nhanVien.getTRANGTHAINV().equals("Đang hoạt động")) {
                itMo.setVisible(false);
                itKhoa.setVisible(true);
            } else {
                itMo.setVisible(true);
                itKhoa.setVisible(false);
            }

            if (nhanVien.getMALOAINV().equals("1")) {
                itLenAdmin.setVisible(false);
                itXuongKH.setVisible(true);
            } else {
                itLenAdmin.setVisible(true);
                itXuongKH.setVisible(false);
            }
        }else if(!emailnv.equals("admin@gmail.com") && !emailnv.equals(nhanVien.getTENDANGNHAP())
                && !nhanVien.getTENDANGNHAP().equals("admin@gmail.com") && nhanVien.getMALOAINV().equals("2")){
            getMenuInflater().inflate(R.menu.menu_nhanvien, menu);
            itKhoa = menu.findItem(R.id.itKhoa);
            itMo = menu.findItem(R.id.itMoKhoa);
            itLenAdmin = menu.findItem(R.id.itThangCapAdmin);
            itXuongKH = menu.findItem(R.id.itXuongKhachHang);

            if (nhanVien.getTRANGTHAINV().equals("Đang hoạt động")) {
                itMo.setVisible(false);
                itKhoa.setVisible(true);
            } else {
                itMo.setVisible(true);
                itKhoa.setVisible(false);
            }

            itLenAdmin.setVisible(false);
            itXuongKH.setVisible(false);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itKhoa: khoaNguoiDung(); break;
            case R.id.itMoKhoa: moNguoiDung();break;
            case R.id.itThangCapAdmin: lenAdmin();break;
            case R.id.itXuongKhachHang: xuongKH();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void khoaNguoiDung() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.khoaNguoiDung(nhanVien.getMANV());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Khóa thành công người dùng: "+nhanVien.getTENDANGNHAP(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void moNguoiDung() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.moKhoaNguoiDung(nhanVien.getMANV());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Mở khóa thành công người dùng: "+nhanVien.getTENDANGNHAP(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void lenAdmin() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.thangCapAdmin(nhanVien.getMANV());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Thăng cấp lên Admin thành công người dùng: "+nhanVien.getTENDANGNHAP(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void xuongKH() {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.xuongKhachHang(nhanVien.getMANV());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Bỏ quyền Admin thành công người dùng: "+nhanVien.getTENDANGNHAP(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminChiTietNhanVienActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}