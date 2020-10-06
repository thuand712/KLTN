package ducthuan.com.lamdep.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Adapter.HienThiSanPhamTheoDanhMucAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemTrangChuActivity extends AppCompatActivity {

    ImageView imgBack;
    SearchView searchView;
    ImageView imgSpeed;
    RecyclerView rvSanPham;
    ArrayList<SanPham>sanPhams;
    HienThiSanPhamTheoDanhMucAdapter hienThiSanPhamTheoDanhMucAdapter;

    LinearLayout linearLayout;
    Button btnThuLaiTuKhoa;

    String timkiem = "";
    String manv = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_trang_chu);
        intent = getIntent();
        if(intent.hasExtra("timkiem")){
            timkiem = intent.getStringExtra("timkiem");
            addControls();
            addEvents();
        }else {
            Toast.makeText(this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
        }

    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //startActivity(new Intent(TimKiemTrangChuActivity.this, TrangChuActivity.class));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //thêm tìm kiếm theo nhân viên
                if(manv.equals("")){
                    themTimKiem(query);
                }else {
                    themTimKiemTheoNV(query);
                    themTimKiem(query);
                }
                //tim kiếm toàn bộ sản phẩm
                if(timkiem.equals("trangchu"))
                {
                    timKiemTrangChu(query);
                }else if(timkiem.equals("danhmuc")){
                    timKiemDanhMuc(query);
                }else if(timkiem.equals("shop")){
                    timKiemShop(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        imgSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bạn muốn mua sản phẩm gì?");
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(TimKiemTrangChuActivity.this, "Máy bạn chưa hỗ trợ tìm kiếm bằng giọng nói", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnThuLaiTuKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timkiem.equals("trangchu")){
                    Intent intent = new Intent(TimKiemTrangChuActivity.this,TimKiemTrangChuActivity.class);
                    intent.putExtra("timkiem","trangchu");
                    startActivity(intent);
                }

            }
        });

    }

    private void timKiemShop(String query) {
        String manv = intent.getStringExtra("manv");
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.timKiemSPShop(query,manv,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    linearLayout.setVisibility(View.GONE);
                    rvSanPham.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TimKiemTrangChuActivity.this,2);
                    hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(TimKiemTrangChuActivity.this,2,sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(TimKiemTrangChuActivity.this));
                    rvSanPham.setHasFixedSize(true);
                    rvSanPham.setNestedScrollingEnabled(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(TimKiemTrangChuActivity.this,DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
                    hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
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

    private void timKiemDanhMuc(String query) {
        String maloaisp = intent.getStringExtra("maloaisp");
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.timKiemSPDanhMuc(query,maloaisp,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    linearLayout.setVisibility(View.GONE);
                    rvSanPham.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TimKiemTrangChuActivity.this,2);
                    hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(TimKiemTrangChuActivity.this,2,sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(TimKiemTrangChuActivity.this));
                    rvSanPham.setHasFixedSize(true);
                    rvSanPham.setNestedScrollingEnabled(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(TimKiemTrangChuActivity.this,DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
                    hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
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

    private void timKiemTrangChu(String query) {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.timKiemSPTrangChu(query,0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    linearLayout.setVisibility(View.GONE);
                    rvSanPham.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TimKiemTrangChuActivity.this,2);
                    hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(TimKiemTrangChuActivity.this,2,sanPhams);
                    rvSanPham.setLayoutManager(new LinearLayoutManager(TimKiemTrangChuActivity.this));
                    rvSanPham.setHasFixedSize(true);
                    rvSanPham.setNestedScrollingEnabled(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(TimKiemTrangChuActivity.this,DividerItemDecoration.VERTICAL);
                    rvSanPham.addItemDecoration(dividerItemDecoration);
                    rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
                    hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String query = result.get(0);
            searchView.setQuery(query,false);
            if(manv.equals("")){
                themTimKiem(query);
            }else {
                themTimKiemTheoNV(query);
                themTimKiem(query);
            }
            if(timkiem.equals("trangchu")){
                timKiemTrangChu(query);
            }else if(timkiem.equals("danhmuc")){
                timKiemDanhMuc(query);
            }else if(timkiem.equals("shop")){
                timKiemShop(query);
            }
        }
    }

    private void themTimKiemTheoNV(String query) {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.themTimKiem(manv,query);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Log.d("sss","Thêm tìm kiếm thành công");
                }else {
                    Log.d("sss","Đã xảy ra lỗi, vui lòng kiểm tra lại!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void themTimKiem(String query) {
        DataService dataService = APIService.getService();
        Call<String>call = dataService.themTimKiemPhoBien(query);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("OK")){
                    Log.d("sss","Thêm tìm kiếm thành công");
                }else {
                    Log.d("sss","Đã xảy ra lỗi, vui lòng kiểm tra lại!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        searchView = findViewById(R.id.searchView);
        imgSpeed = findViewById(R.id.imgSpeed);
        rvSanPham = findViewById(R.id.rvSanPham);
        linearLayout = findViewById(R.id.linearLayout);
        btnThuLaiTuKhoa = findViewById(R.id.btnThuLaiTuKhoa);

        manv = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("manv","");

        if(timkiem.equals("danhmuc")){
            String danhmuc = intent.getStringExtra("danhmuc");
            searchView.setQueryHint("Tìm kiếm trong "+danhmuc);
        }else if(timkiem.equals("shop")){
            String tennv = intent.getStringExtra("tennv");
            searchView.setQueryHint("Tìm kiếm trong "+tennv);
        }

    }
}
