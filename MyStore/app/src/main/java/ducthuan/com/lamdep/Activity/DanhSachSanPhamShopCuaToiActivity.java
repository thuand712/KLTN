package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.ShopCuaToiAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachSanPhamShopCuaToiActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String manv = "";
    Toolbar toolbar;
    RecyclerView rvDanhSachSanPham;
    public static ShopCuaToiAdapter shopCuaToiAdapter;
    public static ArrayList<SanPham>sanPhams;

    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;

    int itemAnDauTien = 0, tongItem = 0, itemLoadTruoc = 4;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham_shop_cua_toi);

        addControls();
        getDanhSachSanPham();
        addEvents();

    }

    private void getDanhSachSanPham() {
        progressBar.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<List<SanPham>> callback = dataService.getSanPhamTheoShop(manv,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    getSanPhamTheoDanhMuc(sanPhams);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }


    private void getSanPhamTheoDanhMuc(ArrayList<SanPham> sanPhams) {

        shopCuaToiAdapter = new ShopCuaToiAdapter(DanhSachSanPhamShopCuaToiActivity.this,sanPhams);
        layoutManager = new GridLayoutManager(DanhSachSanPhamShopCuaToiActivity.this,2);
        rvDanhSachSanPham.setLayoutManager(layoutManager);
        rvDanhSachSanPham.setHasFixedSize(true);
        rvDanhSachSanPham.setNestedScrollingEnabled(true);
        rvDanhSachSanPham.setAdapter(shopCuaToiAdapter);

    }


    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvDanhSachSanPham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                tongItem = layoutManager.getItemCount();
                itemAnDauTien = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();


                if (tongItem <= itemAnDauTien + itemLoadTruoc && isLoading == false) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamTheoShop(manv, tongItem);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            ArrayList<SanPham> sanPhamload = (ArrayList<SanPham>) response.body();
                            if (sanPhamload.size() > 0) {
                                isLoading = false;
                                sanPhams.addAll(sanPhamload);
                                shopCuaToiAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(DanhSachSanPhamShopCuaToiActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                }
            }
        });


    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvDanhSachSanPham = findViewById(R.id.rvDanhSachSanPham);
        progressBar = findViewById(R.id.progressBar);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timkiem_danhsachsanpham_shopcuatoi,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itTimKiem:
                Toast.makeText(this, "Tìm kiếm trong shop của tôi", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
