package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.TimKiemAdapter;
import ducthuan.com.lamdep.Adapter.TimKiemHangDauAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.TimKiem;
import ducthuan.com.lamdep.Model.TimKiemHangDau;
import ducthuan.com.lamdep.Model.TimKiemPhoBien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemHangDauActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView rvTimKiem;
    public static RecyclerView rvSanPham;
    public static TimKiemHangDauAdapter timKiemHangDauAdapter;
    public static TimKiemAdapter timKiemAdapter;
    public static ArrayList<SanPham>sanPhams;
    ArrayList<TimKiem>timKiems;
    ArrayList<TimKiemPhoBien>timKiemPhoBiens;
    public static ArrayList<TimKiemHangDau>timKiemHangDaus;

    Intent intent;
    String manv = "";

    TextView txtGioHang;
    boolean onpause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_hang_dau);
        manv = getSharedPreferences("dangnhap", MODE_PRIVATE).getString("manv", "");
        intent = getIntent();
        if(manv.equals("")){
            timKiemHangDaus = new ArrayList<>();
            timKiemPhoBiens = intent.getParcelableArrayListExtra("timkiemphobien");
            for (int i = 0; i < timKiemPhoBiens.size(); i++) {
                if(i==0){
                    timKiemHangDaus.add(new TimKiemHangDau(timKiemPhoBiens.get(i).getHINH(),timKiemPhoBiens.get(i).getNOIDUNG(),true));
                }else {
                    timKiemHangDaus.add(new TimKiemHangDau(timKiemPhoBiens.get(i).getHINH(),timKiemPhoBiens.get(i).getNOIDUNG(),false));
                }
            }
        }else {
            timKiemHangDaus = new ArrayList<>();
            timKiems = intent.getParcelableArrayListExtra("timkiem");
            for (int i = 0; i < timKiems.size(); i++) {
                if (i == 0){
                    timKiemHangDaus.add(new TimKiemHangDau(timKiems.get(i).getHINH(),timKiems.get(i).getNOIDUNG(),true));
                }else {
                    timKiemHangDaus.add(new TimKiemHangDau(timKiems.get(i).getHINH(),timKiems.get(i).getNOIDUNG(),false));
                }
            }
        }
        addControls();
        getSanPhamTimKiem();
        addEvents();
    }

    public void getSanPhamTimKiem() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>>call = dataService.getTopSPTimKiem(timKiemHangDaus.get(0).getNoidung(),0);
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    timKiemHangDauAdapter = new TimKiemHangDauAdapter(TimKiemHangDauActivity.this,sanPhams);
                    rvSanPham.setAdapter(timKiemHangDauAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvSanPham = findViewById(R.id.rvSanPham);
        rvTimKiem = findViewById(R.id.rvTimKiem);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        rvTimKiem.setLayoutManager(new GridLayoutManager(TimKiemHangDauActivity.this,1,RecyclerView.HORIZONTAL,false));
        //rvTimKiem.setHasFixedSize(true);
        rvTimKiem.setNestedScrollingEnabled(true);
        timKiemAdapter = new TimKiemAdapter(TimKiemHangDauActivity.this,timKiemHangDaus);
        rvTimKiem.setAdapter(timKiemAdapter);


        rvSanPham.setLayoutManager(new LinearLayoutManager(TimKiemHangDauActivity.this));
        rvSanPham.setNestedScrollingEnabled(true);
        rvSanPham.setHasFixedSize(true);

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
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
                    if(sl.equals("")){
                        txtGH.setVisibility(View.GONE);
                    }else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trang_chu,menu);

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv= sharedPreferences.getString("manv","");
                if(manv.equals("")){
                    startActivity(new Intent(TimKiemHangDauActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(TimKiemHangDauActivity.this, GioHangActivity.class);
                    intent.putExtra("trangchu",1);
                    startActivity(intent);
                }

            }
        });

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause==true){
            getDataGioHang(txtGioHang);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }



}
