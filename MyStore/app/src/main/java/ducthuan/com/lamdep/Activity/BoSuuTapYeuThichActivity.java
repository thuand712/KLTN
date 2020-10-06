package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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

import ducthuan.com.lamdep.Adapter.BoSuuTapYeuThichAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoSuuTapYeuThichActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView rvSanPham;
    public static BoSuuTapYeuThichAdapter boSuuTapYeuThichAdapter;
    public static ArrayList<SanPham> sanPhams;

    TextView txtGioHang;
    boolean onpause;
    String manv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_suu_tap_yeu_thich);

        manv = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("manv","");
        addControls();
        getSanPhamYeuThich();
        addEvents();


    }

    private void getSanPhamYeuThich() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>> callback = dataService.layDanhSachSanPhamYeuThich();
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if (sanPhams.size() > 0) {
                    boSuuTapYeuThichAdapter = new BoSuuTapYeuThichAdapter(BoSuuTapYeuThichActivity.this, sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(BoSuuTapYeuThichActivity.this));
                    rvSanPham.setNestedScrollingEnabled(true);
                    rvSanPham.setHasFixedSize(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(BoSuuTapYeuThichActivity.this, DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(boSuuTapYeuThichAdapter);
                    boSuuTapYeuThichAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

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
    }


    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvSanPham = findViewById(R.id.rvSanPham);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trang_chu, menu);

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv = sharedPreferences.getString("manv", "");
                if (manv.equals("")) {
                    startActivity(new Intent(BoSuuTapYeuThichActivity.this, DangNhapActivity.class));
                } else {
                    Intent intent = new Intent(BoSuuTapYeuThichActivity.this, GioHangActivity.class);
                    intent.putExtra("trangchu", 1);
                    startActivity(intent);
                }

            }
        });

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onpause == true) {
            getDataGioHang(txtGioHang);
            getSanPhamYeuThich();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
