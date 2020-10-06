package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.NhanVienAdapter;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminQLNDActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvNV;
    NhanVienAdapter adapter;
    ArrayList<NhanVien>nhanViens;
    ArrayList<NhanVien>tempNhanViens;
    TabLayout tabLayout;
    boolean onpause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qlnd);

        addControls();
        getTatCaNV();
        addEvents();

    }

    private void getTatCaNV() {
        nhanViens = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>call = dataService.getTatCaNV();
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                nhanViens = (ArrayList<NhanVien>) response.body();
                if(nhanViens.size()>0){
                    showAdmin();
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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: showAdmin();break;
                    case 1:showTKHoatDong();break;
                    case 2:showTKKhoa();break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showTKKhoa() {
        tempNhanViens = new ArrayList<>();
        for (int i = 0; i < nhanViens.size(); i++) {
            if(nhanViens.get(i).getTRANGTHAINV().equals("Khóa")){
                tempNhanViens.add(nhanViens.get(i));
            }
        }

        adapter = new NhanVienAdapter(AdminQLNDActivity.this,tempNhanViens);
        rvNV.setAdapter(adapter);
    }

    private void showTKHoatDong() {
        tempNhanViens = new ArrayList<>();
        for (int i = 0; i < nhanViens.size(); i++) {
            if(nhanViens.get(i).getTRANGTHAINV().equals("Đang hoạt động") && !nhanViens.get(i).getMALOAINV().equals("1")){
                tempNhanViens.add(nhanViens.get(i));
            }
        }

        adapter = new NhanVienAdapter(AdminQLNDActivity.this,tempNhanViens);
        rvNV.setAdapter(adapter);
    }

    private void showAdmin() {
        tempNhanViens = new ArrayList<>();
        for (int i = 0; i < nhanViens.size(); i++) {
            if(nhanViens.get(i).getMALOAINV().equals("1")){
                tempNhanViens.add(nhanViens.get(i));
            }
        }

        adapter = new NhanVienAdapter(AdminQLNDActivity.this,tempNhanViens);
        rvNV.setAdapter(adapter);
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvNV = findViewById(R.id.rvNV);
        tabLayout = findViewById(R.id.tabLayout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        rvNV.setHasFixedSize(true);
        rvNV.setNestedScrollingEnabled(true);
        rvNV.setLayoutManager(new LinearLayoutManager(AdminQLNDActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause == true){
            tabLayout.getTabAt(0).select();
            getTatCaNV();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}