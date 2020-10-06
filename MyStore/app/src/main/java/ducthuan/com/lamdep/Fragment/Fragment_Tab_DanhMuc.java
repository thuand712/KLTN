package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.TimKiemTrangChuActivity;
import ducthuan.com.lamdep.Adapter.DanhMucTabTaiKhoanAdapter;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_DanhMuc extends Fragment {
    View view;

    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    RecyclerView rvDanhMuc;
    DanhMucTabTaiKhoanAdapter loaiSanPhamAdapter;
    ArrayList<LoaiSanPham> loaiSanPhams;

    TextView txtTimKiem;

    TextView txtGioHang;
    boolean onpause = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_danh_muc,container,false);

        addControls();
        getDataLoaiSanPham();
        addEvents();

        return view;
    }


    private void getDataLoaiSanPham() {
        DataService dataService = APIService.getService();
        Call<List<LoaiSanPham>> callback = dataService.layDanhSachLoaiSanPham();
        callback.enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                loaiSanPhams = (ArrayList<LoaiSanPham>) response.body();
                if(loaiSanPhams.size()>0){
                    loaiSanPhamAdapter = new DanhMucTabTaiKhoanAdapter(getActivity(), loaiSanPhams);
                    rvDanhMuc.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvDanhMuc.setHasFixedSize(true);
                    rvDanhMuc.setNestedScrollingEnabled(true);
                    rvDanhMuc.setAdapter(loaiSanPhamAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable t) {

            }
        });
    }



    private void addEvents() {

        txtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimKiemTrangChuActivity.class);
                intent.putExtra("timkiem","tabdanhmuc");
                startActivity(intent);
            }
        });

    }

    private void addControls() {
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        rvDanhMuc = view.findViewById(R.id.rvDanhMuc);
        txtTimKiem = view.findViewById(R.id.txtTimKiem);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }


    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
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

    //khi back ve load lai du lieu o day




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_trang_chu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv= sharedPreferences.getString("manv","");
                if(manv.equals("")){
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), GioHangActivity.class);
                    intent.putExtra("trangchu",1);
                    startActivity(intent);
                }

            }
        });

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
