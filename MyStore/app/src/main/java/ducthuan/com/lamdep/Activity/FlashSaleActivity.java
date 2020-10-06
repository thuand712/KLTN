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
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ducthuan.com.lamdep.Adapter.FlashSaleAdapter;
import ducthuan.com.lamdep.Adapter.FlashSaleNgayKeTiepAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashSaleActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    TabLayout tabLayout;
    TextView txtTimeGio,txtTimePhut, txtTimeGiay ;
    LinearLayout linearLayout;

    CountDownTimer countDownTimer;

    RecyclerView rvSanPham;
    FlashSaleAdapter flashSaleAdapter;
    FlashSaleNgayKeTiepAdapter flashSaleNgayKeTiepAdapter;
    ArrayList<SanPham>sanPhams;
    TextView txtGioHang,txtBatDauKetThuc;
    boolean onpause = false;
    String manv = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale);
        manv = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("manv","");
        addControls();
        getSanPhamFlashSale();
        addEvents();

    }

    private void getSanPhamFlashSale() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSale(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    rvSanPham.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    flashSaleAdapter = new FlashSaleAdapter(FlashSaleActivity.this,sanPhams);
                    rvSanPham.setAdapter(flashSaleAdapter);
                }else{
                    rvSanPham.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void getSPKhungGio8() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSale8(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    rvSanPham.setEnabled(false);
                    rvSanPham.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    flashSaleNgayKeTiepAdapter = new FlashSaleNgayKeTiepAdapter(FlashSaleActivity.this,sanPhams);
                    rvSanPham.setAdapter(flashSaleNgayKeTiepAdapter);
                }else {
                    rvSanPham.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void getSPKhungGio16() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSale16(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    rvSanPham.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    rvSanPham.setEnabled(false);
                    flashSaleNgayKeTiepAdapter = new FlashSaleNgayKeTiepAdapter(FlashSaleActivity.this,sanPhams);
                    rvSanPham.setAdapter(flashSaleNgayKeTiepAdapter);
                }else {
                    rvSanPham.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    public int getTongs(){
        int tongs = 0;
        //xu ly dong ho hien tai
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        int giay = calendar.get(Calendar.SECOND);

        if(gio >= 0 && gio <= 7){
            tongs = 8*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 8 && gio <= 15){
            tongs = 16*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 16 && gio <= 23){
            tongs = 24*60*60 - (gio*60*60 + phut * 60 + giay);
        }

        return tongs;
    }

    public int getTongs16(){
        int tongs = 0;
        //xu ly dong ho hien tai
        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        int giay = calendar.get(Calendar.SECOND);

        if(gio >= 0 && gio <= 7){
            tongs = 8*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 8 && gio <= 15){
            tongs = 16*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 16 && gio <= 23){
            tongs = 24*60*60 - (gio*60*60 + phut * 60 + giay);
        }
        tongs += 8*60*60;

        return tongs;
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);

        txtTimeGio = findViewById(R.id.txtTimeGio);
        txtTimePhut = findViewById(R.id.txtTimePhut);
        txtTimeGiay = findViewById(R.id.txtTimeGiay);
        rvSanPham = findViewById(R.id.rvSanPham);
        tabLayout = findViewById(R.id.tabLayout);
        txtBatDauKetThuc = findViewById(R.id.txtBatDauKetThuc);
        linearLayout = findViewById(R.id.linearLayout);

        dongHoFlashSale(getTongs());

        rvSanPham.setLayoutManager(new LinearLayoutManager(FlashSaleActivity.this));
        rvSanPham.setNestedScrollingEnabled(true);
        rvSanPham.setHasFixedSize(true);
        rvSanPham.addItemDecoration(new DividerItemDecoration(FlashSaleActivity.this,DividerItemDecoration.VERTICAL));

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        TabLayout.Tab item1 = tabLayout.getTabAt(0);
        TabLayout.Tab item2 = tabLayout.getTabAt(1);
        TabLayout.Tab item3 = tabLayout.getTabAt(2);

        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);

        if(gio >= 0 && gio <= 7){
            TextView temp = item1.getCustomView().findViewById(R.id.txtThoiGian);
            temp.setText("00:00");
            TextView temp8 = item2.getCustomView().findViewById(R.id.txtThoiGian);
            temp8.setText("08:00");
            TextView temp16 = item3.getCustomView().findViewById(R.id.txtThoiGian);
            temp16.setText("16:00");
        }else if(gio >= 8 && gio <= 15){
            TextView temp = item1.getCustomView().findViewById(R.id.txtThoiGian);
            temp.setText("08:00");
            TextView temp8 = item2.getCustomView().findViewById(R.id.txtThoiGian);
            temp8.setText("16:00");
            TextView temp16 = item3.getCustomView().findViewById(R.id.txtThoiGian);
            temp16.setText("00:00");
            TextView temp16mt = item3.getCustomView().findViewById(R.id.txtMieuTa);
            temp16mt.setText("Ngày mai");
        }else if(gio >= 16 && gio <= 23){
            TextView temp = item1.getCustomView().findViewById(R.id.txtThoiGian);
            temp.setText("16:00");
            TextView temp8 = item2.getCustomView().findViewById(R.id.txtThoiGian);
            temp8.setText("00:00");
            TextView temp8mt = item2.getCustomView().findViewById(R.id.txtMieuTa);
            temp8mt.setText("Ngày mai");
            TextView temp16 = item3.getCustomView().findViewById(R.id.txtThoiGian);
            temp16.setText("08:00");
            TextView temp16mt = item3.getCustomView().findViewById(R.id.txtMieuTa);
            temp16mt.setText("Ngày mai");
        }
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
                    case 0:
                        countDownTimer.cancel();
                        txtBatDauKetThuc.setText("KẾT THÚC TRONG");
                        dongHoFlashSale(getTongs());
                        getSanPhamFlashSale();
                        break;
                    case 1:
                        countDownTimer.cancel();
                        txtBatDauKetThuc.setText("BẮT ĐẦU TRONG");
                        dongHoFlashSale(getTongs());
                        getSPKhungGio8();
                        break;
                    case 2:
                        countDownTimer.cancel();
                        txtBatDauKetThuc.setText("BẮT ĐẦU TRONG");
                        dongHoFlashSale(getTongs16());
                        getSPKhungGio16();
                        break;
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

    private void dongHoFlashSale(int tongs) {

        countDownTimer = new CountDownTimer(tongs * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                txtTimeGio.setText(String.format("%02d", hours));
                txtTimePhut.setText(String.format("%02d", minutes));
                txtTimeGiay.setText(String.format("%02d", seconds));
            }

            public void onFinish() {
                this.start();
            }
        }.start();
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
                    startActivity(new Intent(FlashSaleActivity.this, DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(FlashSaleActivity.this, GioHangActivity.class);
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
