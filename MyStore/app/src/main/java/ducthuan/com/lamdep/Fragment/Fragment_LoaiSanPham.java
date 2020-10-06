package ducthuan.com.lamdep.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.LoaiSanPhamAdapter;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_LoaiSanPham extends Fragment {

    RecyclerView rvDanhMuc;
    LoaiSanPhamAdapter loaiSanPhamAdapter;
    ArrayList<LoaiSanPham>loaiSanPhams;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai_san_pham, container, false);
        rvDanhMuc = view.findViewById(R.id.rvDanhMuc);

        getDataLoaiSanPham();

        return view;
    }

    private void getDataLoaiSanPham() {
        DataService dataService = APIService.getService();
        Call<List<LoaiSanPham>>callback = dataService.layDanhSachLoaiSanPham();
        callback.enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                loaiSanPhams = (ArrayList<LoaiSanPham>)  response.body();
                if(loaiSanPhams.size()>0){
                    loaiSanPhamAdapter = new LoaiSanPhamAdapter(getActivity(), loaiSanPhams);
                    rvDanhMuc.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));
                    rvDanhMuc.setHasFixedSize(true);
                    rvDanhMuc.setAdapter(loaiSanPhamAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable t) {

            }
        });
    }
}
