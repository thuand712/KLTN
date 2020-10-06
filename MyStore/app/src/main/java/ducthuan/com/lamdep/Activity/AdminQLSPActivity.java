package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.SanPhamChoXacNhanAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminQLSPActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvSP;
    ProgressBar progressBar;
    ArrayList<SanPham>sanPhams;
    SanPhamChoXacNhanAdapter adapter;
    LinearLayout linearLayout;

    boolean onpause = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlsp_admin);
        addControls();
        getAllSPChoXacNhan();
        addEvents();
    }

    private void getAllSPChoXacNhan() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>>call = dataService.getSanPhamChoXacNhan(0);
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    rvSP.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    adapter = new SanPhamChoXacNhanAdapter(AdminQLSPActivity.this,sanPhams);
                    rvSP.setAdapter(adapter);
                }else {
                    rvSP.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
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

        rvSP = findViewById(R.id.rvSP);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.linearLayout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        rvSP.setHasFixedSize(true);
        rvSP.setNestedScrollingEnabled(true);
        rvSP.setLayoutManager(new GridLayoutManager(AdminQLSPActivity.this, 2));

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
            getAllSPChoXacNhan();
        }
    }
}