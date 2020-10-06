package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.AdminLoaiSanPhamAdapter;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminQLDMActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvDanhMuc;
    ArrayList<LoaiSanPham>loaiSanPhams;
    AdminLoaiSanPhamAdapter adapter;

    boolean onpause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qldm);

        addControls();
        getDanhMuc();
        addEvents();
    }

    private void getDanhMuc() {
        loaiSanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<LoaiSanPham>>call = dataService.layDanhSachLoaiSanPham();
        call.enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                loaiSanPhams = (ArrayList<LoaiSanPham>) response.body();
                if (loaiSanPhams.size()>0){
                    adapter = new AdminLoaiSanPhamAdapter(AdminQLDMActivity.this,loaiSanPhams);
                    rvDanhMuc.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable t) {

            }
        });
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminQLDMActivity.this,AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void addControls() {

        toolbar = findViewById(R.id.toolbar);
        rvDanhMuc = findViewById(R.id.rvDanhMuc);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        rvDanhMuc.setNestedScrollingEnabled(true);
        rvDanhMuc.setHasFixedSize(true);
        rvDanhMuc.setLayoutManager(new LinearLayoutManager(AdminQLDMActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itAdd: startActivity(new Intent(AdminQLDMActivity.this,AdminThemDanhMucActivity.class));break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause == true){
            getDanhMuc();
        }
    }
}