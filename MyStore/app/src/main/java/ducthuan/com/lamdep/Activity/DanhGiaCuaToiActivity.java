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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.DanhGiaAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhGiaCuaToiActivity extends AppCompatActivity {

    Intent intent;
    Toolbar toolbar;

    TabLayout tabLayout;
    RecyclerView rvDanhGia;
    ArrayList<DanhGia>danhGias;
    ArrayList<DanhGia>tempDanhGias;
    DanhGiaAdapter adapter;

    int i5,i4,i3,i2,i1;
    TextView txtGioHang;
    boolean onpause;

    String manv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia_cua_toi);
        intent = getIntent();
        if(intent.hasExtra("manv")){
            manv = intent.getStringExtra("manv");
            getTatCaDanhGia();
            addControls();
            addEvents();
        }
    }

    private void getTatCaDanhGia() {
        danhGias = new ArrayList<>();
        i1 = 0;i2 = 0;i3 = 0;i4 = 0;i5 = 0;
        DataService dataService = APIService.getService();
        Call<List<DanhGia>>call = dataService.getDanhGiaTheoKH(manv);
        call.enqueue(new Callback<List<DanhGia>>() {
            @Override
            public void onResponse(Call<List<DanhGia>> call, Response<List<DanhGia>> response) {
                danhGias = (ArrayList<DanhGia>) response.body();
                if(danhGias.size()>0){

                    for (int i = 0; i < danhGias.size(); i++) {
                        if(danhGias.get(i).getSOSAO().equals("1")){
                            i1++;
                        }
                        if(danhGias.get(i).getSOSAO().equals("2")){
                            i2++;
                        }
                        if(danhGias.get(i).getSOSAO().equals("3")){
                            i3++;
                        }
                        if(danhGias.get(i).getSOSAO().equals("4")){
                            i4++;
                        }
                        if(danhGias.get(i).getSOSAO().equals("5")){
                            i5++;
                        }
                    }

                    tabLayout.getTabAt(0).setText("Tất cả\n("+danhGias.size()+")");
                    tabLayout.getTabAt(1).setText("5 Sao\n("+i5+")");
                    tabLayout.getTabAt(2).setText("4 Sao\n("+i4+")");
                    tabLayout.getTabAt(3).setText("3 Sao\n("+i3+")");
                    tabLayout.getTabAt(4).setText("2 Sao\n("+i2+")");
                    tabLayout.getTabAt(5).setText("1 Sao\n("+i1+")");
                    adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,danhGias);
                    rvDanhGia.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<DanhGia>> call, Throwable t) {

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
                    case 0: getTatCaDanhGia();break;
                    case 1:showDG5Sao();break;
                    case 2:showDG4Sao();break;
                    case 3:showDG3Sao();break;
                    case 4:showDG2Sao();break;
                    case 5:showDG1Sao();break;
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

    private void showDG1Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("1")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG2Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("2")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG3Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("3")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG4Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("4")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG5Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("5")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaCuaToiActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void addControls() {

        toolbar = findViewById(R.id.toolbar);
        rvDanhGia = findViewById(R.id.rvDanhGia);
        tabLayout = findViewById(R.id.tabLayout);

        rvDanhGia.setLayoutManager(new LinearLayoutManager(DanhGiaCuaToiActivity.this));
        rvDanhGia.setHasFixedSize(true);
        rvDanhGia.setNestedScrollingEnabled(true);
        rvDanhGia.addItemDecoration(new DividerItemDecoration(DanhGiaCuaToiActivity.this,DividerItemDecoration.VERTICAL));

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
    }




    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

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
                if(manv.equals("")){
                    startActivity(new Intent(DanhGiaCuaToiActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(DanhGiaCuaToiActivity.this, GioHangActivity.class);
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
            tabLayout.getTabAt(0).select();
            getTatCaDanhGia();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }

}
