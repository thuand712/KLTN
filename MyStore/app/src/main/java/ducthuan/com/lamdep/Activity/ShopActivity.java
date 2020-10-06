package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Adapter.HienThiSanPhamTheoDanhMucAdapter;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    Spinner spinnerSapXep;
    String[] sapxeps = {"Mới nhất", "Bán chạy", "Giá (thấp đến cao)", "Giá (cao xuống thấp)"};
    ArrayAdapter<String> adapter;
    ImageButton imgLoaiLayoutHienThi;

    ArrayList<NhanVien>nhanViens;

    ImageView imgBack,imgHinhShop;
    TextView txtTenShop,txtLuotDanhGia,txtSearch,txtLocSanPham;
    RatingBar ratingBar;

    RecyclerView rvSanPham;
    RecyclerView.LayoutManager layoutManager;
    public static HienThiSanPhamTheoDanhMucAdapter hienThiSanPhamTheoDanhMucAdapter;
    public static ArrayList<SanPham> sanPhams;

    NestedScrollView nestedScrollView;

    int itemAnDauTien = 0, tongItem = 0, itemLoadTruoc = 4;
    boolean grid = true;
    boolean isLoading = false;
    ProgressBar progressBar;

    TextView txtGioHang;
    boolean onpause = false;

    String tennv = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Intent intent = getIntent();
        if(intent.hasExtra("tennv")){
            tennv = intent.getStringExtra("tennv");
            addControls();
            getNhanVien();
            getSanPhams();
            addEvents();
        }


    }

    private void getNhanVien() {
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>callback = dataService.getNhanVien(tennv);
        callback.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                nhanViens = (ArrayList<NhanVien>) response.body();
                if(nhanViens.size()>0){
                    if(nhanViens.get(0).getHINH() != null){
                        Picasso.with(ShopActivity.this).load(TrangChuActivity.base_url+nhanViens.get(0).getHINH())
                                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhShop);
                    }
                    StringTokenizer stringTokenizer = new StringTokenizer(nhanViens.get(0).getTENDANGNHAP(),"@");

                    txtTenShop.setText(stringTokenizer.nextToken());
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });


    }


    private void getSanPhams() {
        progressBar.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<List<SanPham>> callback = dataService.getSanPhamShop(tennv, 0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if (sanPhams.size() > 0) {
                    getSanPhamTheoDanhMuc(sanPhams);
                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }

    private void getSanPhamTheoDanhMuc(ArrayList<SanPham> sanPhams) {
        if (grid) {
            imgLoaiLayoutHienThi.setImageResource(R.drawable.ic_linear_black_24dp);
            layoutManager = new GridLayoutManager(ShopActivity.this, 2);
            hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(ShopActivity.this, 1, sanPhams);
        } else {
            imgLoaiLayoutHienThi.setImageResource(R.drawable.ic_gird_black_24dp);
            layoutManager = new LinearLayoutManager(ShopActivity.this);
            hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(ShopActivity.this, 2, sanPhams);
        }
        rvSanPham.setHasFixedSize(true);
        rvSanPham.setNestedScrollingEnabled(true);
        rvSanPham.setLayoutManager(layoutManager);
        rvSanPham.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
        hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();

    }



    private void addEvents() {

        spinnerSapXep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = spinnerSapXep.getSelectedItem().toString();
                if (s.equals("Bán chạy")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamShopBanChay(tennv, 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {
                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                } else if (s.equals("Mới nhất")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamShop(tennv, 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {
                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                } else if (s.equals("Giá (thấp đến cao)")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamShopGiaTang(tennv, 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {
                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                } else if (s.equals("Giá (cao xuống thấp)")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamShopGiaGiam(tennv, 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {
                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimKiemTrangChuActivity.class);
                intent.putExtra("timkiem","shop");
                intent.putExtra("tennv",txtTenShop.getText().toString());
                intent.putExtra("manv",nhanViens.get(0).getMANV());
                startActivity(intent);
            }
        });

        txtLocSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
                builder.setTitle("Lọc");
                builder.setIcon(R.drawable.ic_filter_black_36dp);
                builder.setView(R.layout.custom_dialog_locsanpham);
                final AlertDialog dialog = builder.create();
                dialog.show();
                TextView txtApDung = dialog.findViewById(R.id.txtApDung);
                txtApDung.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });



        imgLoaiLayoutHienThi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid = !grid;
                getSanPhamTheoDanhMuc(sanPhams);
            }
        });


        rvSanPham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                tongItem = layoutManager.getItemCount();
                if (layoutManager instanceof LinearLayoutManager) {
                    itemAnDauTien = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                } else if (layoutManager instanceof GridLayoutManager) {
                    itemAnDauTien = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                }

                Log.d("kiemtra",tongItem + itemAnDauTien+"");
                if (tongItem <= itemAnDauTien + itemLoadTruoc && isLoading == false) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamShop(tennv, tongItem);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            ArrayList<SanPham> sanPhamload = (ArrayList<SanPham>) response.body();
                            if (sanPhamload.size() > 0) {
                                isLoading = false;
                                sanPhams.addAll(sanPhamload);
                                hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ShopActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
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
        spinnerSapXep = findViewById(R.id.spinerSapXep);
        progressBar = findViewById(R.id.progressBar);
        imgLoaiLayoutHienThi = findViewById(R.id.imgLoaiLayoutHienThi);
        imgBack = findViewById(R.id.imgBack);
        imgHinhShop = findViewById(R.id.imgHinhShop);
        txtTenShop = findViewById(R.id.txtTenShop);
        rvSanPham = findViewById(R.id.rvSanPham);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        txtSearch = findViewById(R.id.txtSearch);
        txtLocSanPham = findViewById(R.id.txtLocSanPham);

        adapter = new ArrayAdapter<>(ShopActivity.this, android.R.layout.simple_spinner_item, sapxeps);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerSapXep.setAdapter(adapter);
        ViewGroup.LayoutParams spinnerLayoutParams = spinnerSapXep.getLayoutParams();
        spinnerLayoutParams.width -= 1;
        spinnerSapXep.setLayoutParams(spinnerLayoutParams);

        setSupportActionBar(toolbar);

    }


    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
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

                    if (sl.equals("")) {
                        txtGH.setVisibility(View.GONE);
                    } else {
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
        getMenuInflater().inflate(R.menu.menu_shop,menu);
        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
                String manv = sharedPreferences.getString("manv", "");
                if (manv.equals("")) {
                    startActivity(new Intent(ShopActivity.this, DangNhapActivity.class));
                } else {
                    startActivity(new Intent(ShopActivity.this, GioHangActivity.class));
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itGioHang: break;
            case R.id.itTrangChu:
                startActivity(new Intent(getApplicationContext(),TrangChuActivity.class));
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause == true)
        {
            getDataGioHang(txtGioHang);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}
