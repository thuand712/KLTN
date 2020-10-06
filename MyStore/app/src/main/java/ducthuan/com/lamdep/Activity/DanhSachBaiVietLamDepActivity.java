package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.BaiVietLamDepAdapter;
import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachBaiVietLamDepActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvBaiVietLamDep;
    Intent intent;
    LoaiLamDep loaiLamDep;
    BaiVietLamDepAdapter baiVietLamDepAdapter;
    ArrayList<BaiVietLamDep>baiVietLamDeps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_bai_viet_lam_dep);
        intent = getIntent();
        if(intent.hasExtra("loailamdep")){
            loaiLamDep = intent.getParcelableExtra("loailamdep");
            addControls();
            getDataDanhSachBaiVietLamDep();
            addEvents();
        }
    }

    private void getDataDanhSachBaiVietLamDep() {
        DataService dataService = APIService.getService();
        Call<List<BaiVietLamDep>>callback = dataService.getDanhSachBaiVietLamDep(loaiLamDep.getMALOAILAMDEP());
        callback.enqueue(new Callback<List<BaiVietLamDep>>() {
            @Override
            public void onResponse(Call<List<BaiVietLamDep>> call, Response<List<BaiVietLamDep>> response) {
                baiVietLamDeps = (ArrayList<BaiVietLamDep>) response.body();
                if(baiVietLamDeps.size()>0){
                    baiVietLamDepAdapter = new BaiVietLamDepAdapter(DanhSachBaiVietLamDepActivity.this, baiVietLamDeps);
                    rvBaiVietLamDep.setLayoutManager(new GridLayoutManager(DanhSachBaiVietLamDepActivity.this, 2));
                    rvBaiVietLamDep.setHasFixedSize(true);
                    rvBaiVietLamDep.setNestedScrollingEnabled(true);
                    rvBaiVietLamDep.setAdapter(baiVietLamDepAdapter);
                    baiVietLamDepAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BaiVietLamDep>> call, Throwable t) {

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
        rvBaiVietLamDep = findViewById(R.id.rvBaiVietLamDep);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        getSupportActionBar().setTitle(loaiLamDep.getTIEUDELOAILAMDEP());


    }
}
