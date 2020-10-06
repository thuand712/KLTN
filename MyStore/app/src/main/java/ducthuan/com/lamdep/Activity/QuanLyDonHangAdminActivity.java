package ducthuan.com.lamdep.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.QuanLyDonHangAdminAdapter;
import ducthuan.com.lamdep.Adapter.QuanLyDonHangShopAdapter;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyDonHangAdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout linearLayout;
    TabLayout tabLayout;

    RecyclerView rvDonHang;
    ArrayList<QuanLyDonHangShop> quanLyDonHangShops;
    QuanLyDonHangAdminAdapter quanLyDonHangShopAdapter;
    ArrayList<QuanLyDonHangShop>temps;
    ArrayList<String>madhs;

    boolean onpause = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_don_hang_admin);

        addControls();
        getDataDonHangs();
        addEvents();
    }

    private void getDataDonHangs() {
        DataService dataService = APIService.getService();
        Call<List<QuanLyDonHangShop>> callback = dataService.getDonHangAdmin();
        callback.enqueue(new Callback<List<QuanLyDonHangShop>>() {
            @Override
            public void onResponse(Call<List<QuanLyDonHangShop>> call, Response<List<QuanLyDonHangShop>> response) {
                quanLyDonHangShops = (ArrayList<QuanLyDonHangShop>) response.body();
                if (quanLyDonHangShops.size() > 0) {
                    linearLayout.setVisibility(View.GONE);
                    rvDonHang.setVisibility(View.VISIBLE);
                    xyLyHienThiDonHang();
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                    rvDonHang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<QuanLyDonHangShop>> call, Throwable t) {

            }
        });

    }

    private void xyLyHienThiDonHang() {
        madhs = new ArrayList<>();

        madhs.add(quanLyDonHangShops.get(0).getMAHD());
        for(int i=1;i<quanLyDonHangShops.size();i++){
            int dem = 0;
            for(int j=0;j<madhs.size();j++){
                if(quanLyDonHangShops.get(i).getMAHD().equals(madhs.get(j))){
                    dem++;
                }
            }
            if(dem==0){
                madhs.add(quanLyDonHangShops.get(i).getMAHD());
            }
        }

        quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,quanLyDonHangShops,madhs);
        rvDonHang.setAdapter(quanLyDonHangShopAdapter);
    }


    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getDataDonHangs(); ;break;
                    case 1: xyLyChoXacNhan(); break;
                    case 2: xyLyDangGiao(); break;
                    case 3: xyLyDaGiao(); break;
                    case 4: xyLyDaHuy(); break;
                    case 5: xyLyTraHang(); break;
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

    private void xyLyTraHang() {
        temps = new ArrayList<>();
        madhs = new ArrayList<>();

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getTRANGTHAI().equals("Trả hàng")){
                temps.add(quanLyDonHangShops.get(i));
            }
        }
        if(temps.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            madhs.add(temps.get(0).getMAHD());

            for(int i=1;i<temps.size();i++){
                int dem = 0;
                for(int j=0;j<madhs.size();j++){
                    if(temps.get(i).getMAHD().equals(madhs.get(j))){
                        dem++;
                    }
                }
                if(dem==0){
                    madhs.add(temps.get(i).getMAHD());
                }
            }

            quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,temps,madhs);
            rvDonHang.setAdapter(quanLyDonHangShopAdapter);
        }
    }

    private void xyLyDaHuy() {
        temps = new ArrayList<>();
        madhs = new ArrayList<>();

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getTRANGTHAI().equals("Đã hủy")){
                temps.add(quanLyDonHangShops.get(i));
            }
        }
        if(temps.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            madhs.add(temps.get(0).getMAHD());

            for(int i=1;i<temps.size();i++){
                int dem = 0;
                for(int j=0;j<madhs.size();j++){
                    if(temps.get(i).getMAHD().equals(madhs.get(j))){
                        dem++;
                    }
                }
                if(dem==0){
                    madhs.add(temps.get(i).getMAHD());
                }
            }

            quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,temps,madhs);
            rvDonHang.setAdapter(quanLyDonHangShopAdapter);
        }

    }

    private void xyLyDaGiao() {
        temps = new ArrayList<>();
        madhs = new ArrayList<>();

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getTRANGTHAI().equals("Đã giao")){
                temps.add(quanLyDonHangShops.get(i));
            }
        }
        if(temps.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            madhs.add(temps.get(0).getMAHD());

            for(int i=1;i<temps.size();i++){
                int dem = 0;
                for(int j=0;j<madhs.size();j++){
                    if(temps.get(i).getMAHD().equals(madhs.get(j))){
                        dem++;
                    }
                }
                if(dem==0){
                    madhs.add(temps.get(i).getMAHD());
                }
            }

            quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,temps,madhs);
            rvDonHang.setAdapter(quanLyDonHangShopAdapter);
        }
    }

    private void xyLyDangGiao() {
        temps = new ArrayList<>();
        madhs = new ArrayList<>();

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getTRANGTHAI().equals("Đang giao")){
                temps.add(quanLyDonHangShops.get(i));
            }
        }
        if(temps.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            madhs.add(temps.get(0).getMAHD());

            for(int i=1;i<temps.size();i++){
                int dem = 0;
                for(int j=0;j<madhs.size();j++){
                    if(temps.get(i).getMAHD().equals(madhs.get(j))){
                        dem++;
                    }
                }
                if(dem==0){
                    madhs.add(temps.get(i).getMAHD());
                }
            }

            quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,temps,madhs);
            rvDonHang.setAdapter(quanLyDonHangShopAdapter);
        }
    }

    private void xyLyChoXacNhan() {
        temps = new ArrayList<>();
        madhs = new ArrayList<>();

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getTRANGTHAI().equals("Chờ xác nhận")){
                temps.add(quanLyDonHangShops.get(i));
            }
        }
        if(temps.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
            rvDonHang.setVisibility(View.GONE);
        }else {
            linearLayout.setVisibility(View.GONE);
            rvDonHang.setVisibility(View.VISIBLE);
            madhs.add(temps.get(0).getMAHD());

            for(int i=1;i<temps.size();i++){
                int dem = 0;
                for(int j=0;j<madhs.size();j++){
                    if(temps.get(i).getMAHD().equals(madhs.get(j))){
                        dem++;
                    }
                }
                if(dem==0){
                    madhs.add(temps.get(i).getMAHD());
                }
            }

            quanLyDonHangShopAdapter = new QuanLyDonHangAdminAdapter(QuanLyDonHangAdminActivity.this,temps,madhs);
            rvDonHang.setAdapter(quanLyDonHangShopAdapter);
        }
    }



    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.linearLayout);
        rvDonHang = findViewById(R.id.rvDonHang);
        tabLayout = findViewById(R.id.tabLayout);

        rvDonHang.setHasFixedSize(true);
        rvDonHang.setNestedScrollingEnabled(true);
        rvDonHang.setLayoutManager(new LinearLayoutManager(QuanLyDonHangAdminActivity.this));

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause == true){
            tabLayout.getTabAt(0).select();
            getDataDonHangs();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}
