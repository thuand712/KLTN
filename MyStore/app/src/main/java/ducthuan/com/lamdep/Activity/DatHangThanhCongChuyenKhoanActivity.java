package ducthuan.com.lamdep.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Model.ChiTietDonHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatHangThanhCongChuyenKhoanActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnTiepTucMuaSam, btnChiTietDonHang;
    TextView txtShowMaTienDH,txtMaDH,txtTongTien,txtXacNhanCK;
    String mahdtong = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang_thanh_cong_chuyen_khoan);
        Intent intent = getIntent();
        if(intent.hasExtra("mahdtong")){
            mahdtong = intent.getStringExtra("mahdtong");

            addControls();
            GetData();
            addEvents();
        }
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<ChiTietDonHang>>callback = dataService.getDataDatHangThanhCong(mahdtong);
        callback.enqueue(new Callback<List<ChiTietDonHang>>() {
            @Override
            public void onResponse(Call<List<ChiTietDonHang>> call, Response<List<ChiTietDonHang>> response) {
                ArrayList<ChiTietDonHang>chiTietDonHangs = (ArrayList<ChiTietDonHang>) response.body();
                if (chiTietDonHangs.size()>0){
                    txtShowMaTienDH.setText(Html.fromHtml("Bạn đã đặt thành công đơn hàng mã " +
                            "<b>"+chiTietDonHangs.get(0).getMAHDTONG()+"</b>, trị giá <b>"+chiTietDonHangs.get(0).getTONGTIEN()+"</b> thanh toán chuyển khoản."));
                    txtMaDH.setText("Thanh toán đơn hàng "+chiTietDonHangs.get(0).getMAHDTONG());
                    txtTongTien.setText(chiTietDonHangs.get(0).getTONGTIEN());
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietDonHang>> call, Throwable t) {

            }
        });

    }

    private void addEvents() {

        btnTiepTucMuaSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatHangThanhCongChuyenKhoanActivity.this, TrangChuActivity.class));
            }
        });

        btnChiTietDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatHangThanhCongChuyenKhoanActivity.this,DonMuaActivity.class));
            }
        });

        txtXacNhanCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DatHangThanhCongChuyenKhoanActivity.this,XacNhanChuyenKhoanActivity.class);
                intent.putExtra("mahdtong",mahdtong);
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        btnTiepTucMuaSam = findViewById(R.id.btnTiepTucMuaSam);
        btnChiTietDonHang = findViewById(R.id.btnChiTietDonHang);
        txtShowMaTienDH = findViewById(R.id.txtShowMaTienDH);
        txtMaDH = findViewById(R.id.txtMaDH);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtXacNhanCK = findViewById(R.id.txtXacNhanCK);

        txtXacNhanCK.setText(Html.fromHtml("Sau khi chuyển khoản, bạn vui lòng <font color=\"blue\"><u>xác nhận chuyển khoản</u></font> nhé."));

    }
}
