package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ducthuan.com.lamdep.R;

public class XacNhanChuyenKhoanThanhCongActivity extends AppCompatActivity {

    Intent intent;
    String mahdtong = "";

    TextView txtMaDH;
    Button btnTiepTucMuaSam,btnChiTietDonHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_chuyen_khoan_thanh_cong);
        intent = getIntent();
        if(intent.hasExtra("mahdtong")){
            mahdtong = intent.getStringExtra("mahdtong");
            addControls();
            addEvents();
        }

    }

    private void addEvents() {
        btnTiepTucMuaSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(XacNhanChuyenKhoanThanhCongActivity.this, TrangChuActivity.class));
            }
        });

        btnChiTietDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(XacNhanChuyenKhoanThanhCongActivity.this,DonMuaActivity.class));
            }
        });
    }

    private void addControls() {
        txtMaDH = findViewById(R.id.txtMaDH);
        btnChiTietDonHang = findViewById(R.id.btnChiTietDonHang);
        btnTiepTucMuaSam = findViewById(R.id.btnTiepTucMuaSam);

        txtMaDH.setText(Html.fromHtml("Xác nhận thanh toán đơn hàng mã <b>"+mahdtong+"</b> thành công."));

    }
}