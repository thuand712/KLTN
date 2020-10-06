package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.ChuyenKhoanAdapter;
import ducthuan.com.lamdep.Model.ChuyenKhoan;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminQuanLyChuyenKhoanActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    RecyclerView rvChuyenKhoan;
    ArrayList<ChuyenKhoan>chuyenKhoans;
    ArrayList<ChuyenKhoan>tempChuyenKhoans;
    ChuyenKhoanAdapter chuyenKhoanAdapter;
    LinearLayout linearLayout;
    boolean onpause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quan_ly_chuyen_khoan);
        addControls();
        getDataChuyenKhoan();
        addEvents();
    }

    private void getDataChuyenKhoan() {
        DataService dataService = APIService.getService();
        Call<List<ChuyenKhoan>>call = dataService.getAllChuyenKhoan();
        call.enqueue(new Callback<List<ChuyenKhoan>>() {
            @Override
            public void onResponse(Call<List<ChuyenKhoan>> call, Response<List<ChuyenKhoan>> response) {
                chuyenKhoans = (ArrayList<ChuyenKhoan>) response.body();
                if(chuyenKhoans.size()>0){
                    rvChuyenKhoan.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    showChuyenKhoanChoXacNhan();
                }else {
                    rvChuyenKhoan.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ChuyenKhoan>> call, Throwable t) {

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
                    case 0: showChuyenKhoanChoXacNhan();break;
                    case 1:showChuyenKhoanDaXacNhan();break;
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

    private void showChuyenKhoanDaXacNhan() {
        tempChuyenKhoans = new ArrayList<>();
        for (int i = 0; i < chuyenKhoans.size(); i++) {
            if(chuyenKhoans.get(i).getTRANGTHAI().equals("Đã xác nhận")){
                tempChuyenKhoans.add(chuyenKhoans.get(i));
            }
        }

        chuyenKhoanAdapter = new ChuyenKhoanAdapter(AdminQuanLyChuyenKhoanActivity.this,tempChuyenKhoans);
        rvChuyenKhoan.setAdapter(chuyenKhoanAdapter);
    }

    private void showChuyenKhoanChoXacNhan() {
        tempChuyenKhoans = new ArrayList<>();
        for (int i = 0; i < chuyenKhoans.size(); i++) {
            if(chuyenKhoans.get(i).getTRANGTHAI().equals("Chờ xác nhận")){
                tempChuyenKhoans.add(chuyenKhoans.get(i));
            }
        }

        chuyenKhoanAdapter = new ChuyenKhoanAdapter(AdminQuanLyChuyenKhoanActivity.this,tempChuyenKhoans);
        rvChuyenKhoan.setAdapter(chuyenKhoanAdapter);

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        rvChuyenKhoan = findViewById(R.id.rvChuyenKhoan);
        linearLayout = findViewById(R.id.linearLayout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        chuyenKhoans = new ArrayList<>();
        rvChuyenKhoan.setLayoutManager(new LinearLayoutManager(AdminQuanLyChuyenKhoanActivity.this));
        rvChuyenKhoan.setHasFixedSize(true);
        rvChuyenKhoan.setNestedScrollingEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause == true){
            tabLayout.getTabAt(0).select();
            getDataChuyenKhoan();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}