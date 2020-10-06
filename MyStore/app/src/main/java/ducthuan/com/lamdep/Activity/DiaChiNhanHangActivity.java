package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaChiNhanHangActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    Toolbar toolbar;
    EditText edTen,edDiaChi,edSoDienThoai,edEmail;
    CheckBox chkXacNhan;
    Button btnTiepTuc;
    SharedPreferences sharedPreferences;
    ArrayList<GioHang>gioHangs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_chi_nhan_hang);

        Intent intent = getIntent();
        if(intent.hasExtra("giohang")){
            gioHangs = intent.getParcelableArrayListExtra("giohang");
            addControls();
            addEvents();
        }
        else {
            Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chkXacNhan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    btnTiepTuc.setEnabled(true);
                    btnTiepTuc.setBackgroundResource(R.drawable.custom_border_button);
                }else {
                    btnTiepTuc.setEnabled(false);
                    btnTiepTuc.setBackgroundResource(R.drawable.custom_button_nen_xam_bogoc);
                }
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edTen.getText().toString().trim().equals("")||edDiaChi.getText().toString().trim().equals("")||edSoDienThoai.getText().toString().trim().equals("")) {
                    Toast.makeText(DiaChiNhanHangActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                String manv = sharedPreferences.getString("manv","");
                String tennv = edTen.getText().toString().trim();
                String diachi = edDiaChi.getText().toString().trim();
                String sodt = edSoDienThoai.getText().toString().trim();
                String email = edEmail.getText().toString().trim();

                DataService dataService = APIService.getService();
                Call<String>callback = dataService.luuDiaChiKhachHang(manv,tennv,diachi,sodt,email);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("OK")){

                            Intent intent = new Intent(DiaChiNhanHangActivity.this, XacNhanThongTinMuaHangActivity.class);
                            intent.putParcelableArrayListExtra("giohang",gioHangs);
                            startActivity(intent);

                        }else {
                            Toast.makeText(DiaChiNhanHangActivity.this, "Đã xảy ra lỗi vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });



            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        edTen = findViewById(R.id.edTen);
        edDiaChi = findViewById(R.id.edDiaChi);
        edSoDienThoai = findViewById(R.id.edSoDienThoai);
        edEmail = findViewById(R.id.edEmail);
        chkXacNhan = findViewById(R.id.chkXacNhan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        progressDialog = new ProgressDialog(DiaChiNhanHangActivity.this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        String name = sharedPreferences.getString("tennv","");
        edTen.setText(name);



    }
}
