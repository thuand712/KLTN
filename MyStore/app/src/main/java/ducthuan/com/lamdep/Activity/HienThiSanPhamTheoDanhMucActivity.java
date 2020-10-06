package ducthuan.com.lamdep.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ducthuan.com.lamdep.Adapter.HienThiSanPhamTheoDanhMucAdapter;
import ducthuan.com.lamdep.Model.LoaiSanPham;

import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HienThiSanPhamTheoDanhMucActivity extends AppCompatActivity {

    Spinner spinnerSapXep;
    String[] sapxeps = {"Mới nhất", "Bán chạy", "Giá (thấp đến cao)", "Giá (cao xuống thấp)"};
    ArrayAdapter<String> adapter;
    ImageButton imgLoaiLayoutHienThi;
    TextView txtLocSanPham;
    LoaiSanPham loaiSanPham;

    AppBarLayout appbarLayout;
    CollapsingToolbarLayout collapsingLoaiSanPham;
    ImageView imgHinhLoaiSanPham;
    Toolbar toolbarTenLoaiSanPham;
    ProgressBar progressBar;

    LinearLayout linearLayout;

    RecyclerView rvHienThiSPTheoLoaiSP;
    RecyclerView.LayoutManager layoutManager;
    public static HienThiSanPhamTheoDanhMucAdapter hienThiSanPhamTheoDanhMucAdapter;
    public static ArrayList<SanPham> sanPhams;

    int itemAnDauTien = 0, tongItem = 0, itemLoadTruoc = 4;
    boolean grid = true;
    boolean isLoading = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hien_thi_san_pham_theo_danh_muc);


        GetIntent();
        addControls();
        getSanPhams();
        addEvents();

    }


    private void getSanPhams() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>> callback = dataService.getThoiTrangNus(loaiSanPham.getMALOAISP(), 0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if (sanPhams.size() > 0) {
                    rvHienThiSPTheoLoaiSP.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    getSanPhamTheoDanhMuc(sanPhams);
                } else {
                    rvHienThiSPTheoLoaiSP.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
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
            layoutManager = new GridLayoutManager(HienThiSanPhamTheoDanhMucActivity.this, 2);
            hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(HienThiSanPhamTheoDanhMucActivity.this, 1, sanPhams);
        } else {
            imgLoaiLayoutHienThi.setImageResource(R.drawable.ic_gird_black_24dp);
            layoutManager = new LinearLayoutManager(HienThiSanPhamTheoDanhMucActivity.this);
            hienThiSanPhamTheoDanhMucAdapter = new HienThiSanPhamTheoDanhMucAdapter(HienThiSanPhamTheoDanhMucActivity.this, 2, sanPhams);
        }
        rvHienThiSPTheoLoaiSP.setHasFixedSize(true);
        rvHienThiSPTheoLoaiSP.setNestedScrollingEnabled(true);
        rvHienThiSPTheoLoaiSP.setLayoutManager(layoutManager);
        rvHienThiSPTheoLoaiSP.setAdapter(hienThiSanPhamTheoDanhMucAdapter);
        hienThiSanPhamTheoDanhMucAdapter.notifyDataSetChanged();

    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("itemloaisp")) {
            loaiSanPham = intent.getParcelableExtra("itemloaisp");
        }
    }

    private void addEvents() {
        spinnerSapXep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = spinnerSapXep.getSelectedItem().toString();
                if (s.equals("Bán chạy")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getThoiTrangNusBanChay(loaiSanPham.getMALOAISP(), 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {

                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(HienThiSanPhamTheoDanhMucActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });


                } else if (s.equals("Mới nhất")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getThoiTrangNus(loaiSanPham.getMALOAISP(), 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {

                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(HienThiSanPhamTheoDanhMucActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                } else if (s.equals("Giá (thấp đến cao)")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getThoiTrangNusGiaTang(loaiSanPham.getMALOAISP(), 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {

                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(HienThiSanPhamTheoDanhMucActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                } else if (s.equals("Giá (cao xuống thấp)")) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getThoiTrangNusGiaGiam(loaiSanPham.getMALOAISP(), 0);
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            sanPhams = (ArrayList<SanPham>) response.body();
                            if (sanPhams.size() > 0) {

                                getSanPhamTheoDanhMuc(sanPhams);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(HienThiSanPhamTheoDanhMucActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
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
        imgLoaiLayoutHienThi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid = !grid;
                getSanPhamTheoDanhMuc(sanPhams);
            }
        });

        txtLocSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HienThiSanPhamTheoDanhMucActivity.this);
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

        rvHienThiSPTheoLoaiSP.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (tongItem <= itemAnDauTien + itemLoadTruoc && isLoading == false) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getThoiTrangNus(loaiSanPham.getMALOAISP(), tongItem);
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
                                Toast.makeText(HienThiSanPhamTheoDanhMucActivity.this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
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
        spinnerSapXep = findViewById(R.id.spinerSapXep);
        imgLoaiLayoutHienThi = findViewById(R.id.imgLoaiLayoutHienThi);
        txtLocSanPham = findViewById(R.id.txtLocSanPham);
        toolbarTenLoaiSanPham = findViewById(R.id.toolbarTenLoaiSanPham);
        imgHinhLoaiSanPham = findViewById(R.id.imgHinhLoaiSanPham);
        collapsingLoaiSanPham = findViewById(R.id.collapsingLoaiSanPham);
        rvHienThiSPTheoLoaiSP = findViewById(R.id.rvHienThiSPTheoLoaiSP);
        appbarLayout = findViewById(R.id.appbarLayout);
        progressBar = findViewById(R.id.progressBar);
        linearLayout = findViewById(R.id.linearLayout);


        collapsingLoaiSanPham.setExpandedTitleColor(Color.WHITE);
        collapsingLoaiSanPham.setCollapsedTitleTextColor(Color.WHITE);

        //Chỉ hiển thị tiêu đề khi thanh toolbar được thu gọn
        collapsingLoaiSanPham.setTitle(" ");
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingLoaiSanPham.setTitle(loaiSanPham.getTENLOAISP());
                    isShow = true;
                } else if (isShow) {
                    collapsingLoaiSanPham.setTitle(" ");
                    isShow = false;
                }
            }
        });

        Picasso.with(this).load(TrangChuActivity.base_url + loaiSanPham.getHINHANH()).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhLoaiSanPham);
        setSupportActionBar(toolbarTenLoaiSanPham);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(loaiSanPham.getTENLOAISP());
        toolbarTenLoaiSanPham.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbarTenLoaiSanPham.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new ArrayAdapter<>(HienThiSanPhamTheoDanhMucActivity.this, android.R.layout.simple_spinner_item, sapxeps);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerSapXep.setAdapter(adapter);

        ViewGroup.LayoutParams spinnerLayoutParams = spinnerSapXep.getLayoutParams();
        spinnerLayoutParams.width -= 1;
        spinnerSapXep.setLayoutParams(spinnerLayoutParams);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timkiem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itSearchDM) {
            Intent intent = new Intent(getApplicationContext(), TimKiemTrangChuActivity.class);
            intent.putExtra("timkiem", "danhmuc");
            intent.putExtra("danhmuc", loaiSanPham.getTENLOAISP());
            intent.putExtra("maloaisp", loaiSanPham.getMALOAISP());
            startActivity(intent);
        }
        return true;
    }
}
