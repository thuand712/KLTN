package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

import ducthuan.com.lamdep.Adapter.HienThiSanPhamTheoDanhMucAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPhamDaXemActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    RecyclerView rvSanPham;

    RecyclerView.LayoutManager layoutManager;
    HienThiSanPhamTheoDanhMucAdapter hienThiSanPhamTheoDanhMucAdapter;
    ArrayList<SanPham> sanPhams;
    TextView txtGioHang;
    boolean onpause;

    String manv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_da_xem);
        Intent intent = getIntent();
        if(intent.hasExtra("manv")){
            manv = intent.getStringExtra("manv");
            addControls();
            getSanPhamDaXem();
            addEvents();
        }
    }

    private void getSanPhamDaXem() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.getSanPhamDaXem(manv,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    layoutManager = new GridLayoutManager(SanPhamDaXemActivity.this, 2);
                    hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(SanPhamDaXemActivity.this, 1, sanPhams);
                    rvSanPham.setHasFixedSize(true);
                    rvSanPham.setNestedScrollingEnabled(true);
                    rvSanPham.setLayoutManager(layoutManager);
                    rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
                    hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
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
                    startActivity(new Intent(SanPhamDaXemActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(SanPhamDaXemActivity.this, GioHangActivity.class);
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
            getSanPhamDaXem();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }


}
