package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ducthuan.com.lamdep.Model.ChuyenKhoan;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietChuyenKhoanActivity extends AppCompatActivity {

    Intent intent;
    ChuyenKhoan chuyenKhoan;
    Toolbar toolbar;
    ScrollView layoutChiTiet;
    TextView txtTrangThaiDH,txtMaDH,txtNgayCK,txtNganHangCK,txtSTKCK,txtNganHangNhan,txtSTKNhan,txtSoTienChuyen,txtGhiChu;
    ImageView imgAnhChiTiet;
    Button btnXacNhanCK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_chuyen_khoan);
        intent = getIntent();
        if(intent.hasExtra("chuyenkhoan")){
            chuyenKhoan = intent.getParcelableExtra("chuyenkhoan");
            Log.d("sss",chuyenKhoan.getTENNGUOINHAN());
            addControls();
            getChiTietChuyenKhoan();
            addEvents();
        }

    }

    private void getChiTietChuyenKhoan() {
        txtMaDH.setText(chuyenKhoan.getMAHDTONG());
        txtNgayCK.setText(chuyenKhoan.getNGAYCK());
        txtNganHangCK.setText(chuyenKhoan.getNGANHANGCK());
        txtSTKCK.setText(chuyenKhoan.getSTKCK());
        txtNganHangNhan.setText(chuyenKhoan.getNGANHANGNHAN());
        txtSTKNhan.setText(chuyenKhoan.getSTKNHAN());
        txtSoTienChuyen.setText(chuyenKhoan.getSOTIENCHUYEN());
        txtGhiChu.setText(chuyenKhoan.getGHICHUCK());
        Picasso.with(ChiTietChuyenKhoanActivity.this).load(TrangChuActivity.base_url+chuyenKhoan.getHINHCK())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgAnhChiTiet);
        if(!chuyenKhoan.getTRANGTHAI().equals("Chờ xác nhận")){
            txtTrangThaiDH.setText(getResources().getString(R.string.donhangdachuyenkhoan));
            btnXacNhanCK.setVisibility(View.GONE);
        }
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        layoutChiTiet = findViewById(R.id.layoutChiTiet);
        imgAnhChiTiet = findViewById(R.id.imgAnhChiTiet);
        btnXacNhanCK = findViewById(R.id.btnXacNhanCK);
        txtTrangThaiDH = findViewById(R.id.txtTrangThaiDH);
        txtMaDH = findViewById(R.id.txtMaDH);
        txtNgayCK = findViewById(R.id.txtNgayCK);
        txtNganHangCK = findViewById(R.id.txtNganHangCK);
        txtSTKCK = findViewById(R.id.txtSTKCK);
        txtNganHangNhan = findViewById(R.id.txtNganHangNhan);
        txtSTKNhan = findViewById(R.id.txtSTKNhan);
        txtSoTienChuyen = findViewById(R.id.txtSoTienChuyen);
        txtGhiChu = findViewById(R.id.txtGhiChu);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnXacNhanCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataService dataService = APIService.getService();
                Call<String>call = dataService.capNhapChuyenKhoan(chuyenKhoan.getMAHDTONG());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("OK")){
                            Toast.makeText(ChiTietChuyenKhoanActivity.this, "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ChiTietChuyenKhoanActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }
}