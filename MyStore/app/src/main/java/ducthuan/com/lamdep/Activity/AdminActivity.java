package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<NhanVien>nhanViens;


    CircleImageView imgHinhNV;
    TextView txtTenNV,txtEmailNV,txtNgayDangKy,txtQLDH,txtSPDaXacNhan,txtSPChoXacNhan,txtQLDM,txtQLND,txtDHCK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
            addControls();
            getAdmin();
            addEvents();
        }

    private void getAdmin() {
        String tennv = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("tennv","");
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>call = dataService.getNhanVien(tennv);
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                nhanViens = (ArrayList<NhanVien>) response.body();
                if(nhanViens.size()>0){
                    txtTenNV.setText(nhanViens.get(0).getTENNV());
                    txtEmailNV.setText(nhanViens.get(0).getTENDANGNHAP());
                    txtNgayDangKy.setText("Thành viên từ "+nhanViens.get(0).getNGAYDANGKY());
                    if(nhanViens.get(0).getHINH()!=null){
                        String hinh = nhanViens.get(0).getHINH();
                        Picasso.with(AdminActivity.this).load(TrangChuActivity.base_url+hinh).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhNV);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

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
        txtQLDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,QuanLyDonHangAdminActivity.class));
            }
        });

        txtDHCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminQuanLyChuyenKhoanActivity.class));
            }
        });
        txtSPDaXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AdminQLSPActivity.class));
            }
        });

        /*txtSPChoXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, QLSPAdminActivity.class));
            }
        });*/

        txtQLDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,AdminQLDMActivity.class));
            }
        });

        txtQLND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,AdminQLNDActivity.class));
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgHinhNV = findViewById(R.id.imgHinhNV);
        txtTenNV = findViewById(R.id.txtTenNV);
        txtEmailNV = findViewById(R.id.txtEmailNV);
        txtNgayDangKy = findViewById(R.id.txtNgayDangKy);
        txtQLDH = findViewById(R.id.txtQLDH);
        txtSPDaXacNhan = findViewById(R.id.txtSPDaXacNhan);
        txtSPChoXacNhan = findViewById(R.id.txtSPChoXacNhan);
        txtQLDM = findViewById(R.id.txtQLDM);
        txtQLND = findViewById(R.id.txtQLND);
        txtDHCK = findViewById(R.id.txtDHCK);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }
}
