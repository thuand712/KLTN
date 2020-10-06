package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Model.ChiTietDonHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatHangMoMoThanhCongActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnTiepTucMuaSam, btnChiTietDonHang;
    TextView txtMaDonHang,txtNgayGiaoHang;
    String mahdtong = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang_mo_mo_thanh_cong);
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
                    txtMaDonHang.setText(chiTietDonHangs.get(0).getMAHDTONG());
                    txtNgayGiaoHang.setText(chiTietDonHangs.get(0).getNGAYGIAO());
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietDonHang>> call, Throwable t) {

            }
        });

    }

    private void addEvents() {
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatHangThanhCongActivity.this, GioHangActivity.class));
            }
        });*/

        btnTiepTucMuaSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatHangMoMoThanhCongActivity.this, TrangChuActivity.class));
            }
        });

        btnChiTietDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatHangMoMoThanhCongActivity.this,DonMuaActivity.class));
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        btnTiepTucMuaSam = findViewById(R.id.btnTiepTucMuaSam);
        btnChiTietDonHang = findViewById(R.id.btnChiTietDonHang);
        txtMaDonHang = findViewById(R.id.txtMaDonHang);
        txtNgayGiaoHang = findViewById(R.id.txtNgayGiaoHang);

        //setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);


    }
}
