package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import ducthuan.com.lamdep.Adapter.SanPhamGoiYAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SanPhamGoiY extends Fragment {

    RecyclerView rvSanPhamGoiY;
    ArrayList<SanPham> sanPhams;
    SanPhamGoiYAdapter sanPhamGoiYAdapter;
    SharedPreferences sharedPreferences;
    String manv = "";
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goi_y_hom_nay, container, false);
        addControls();

        xuLyGoiYSanPham();

        return view;
    }

    private void xuLyGoiYSanPham() {

        if(manv.equals("")){
            getSPBanChay();
        }else{
            DataService dataService = APIService.getService();
            Call<String>call = dataService.kiemTraDaXemSPNaoChua(manv);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if(kq.equals("OK")){
                        getSPGoiY();
                    }else if(kq.equals("FAIL")){
                        getSPBanChay();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void getSPGoiY() {
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>> call = dataService.getSPGoiY(manv);
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if (sanPhams.size() > 0) {
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(getActivity(), sanPhams);
                    rvSanPhamGoiY.setAdapter(sanPhamGoiYAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }

    private void getSPBanChay(){
        sanPhams = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<SanPham>> call = dataService.getSPBanChay();
        call.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if (sanPhams.size() > 0) {
                    sanPhamGoiYAdapter = new SanPhamGoiYAdapter(getActivity(), sanPhams);
                    rvSanPhamGoiY.setAdapter(sanPhamGoiYAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });
    }


    private void addControls() {
        rvSanPhamGoiY = view.findViewById(R.id.rvSanPhamGoiY);
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        manv = sharedPreferences.getString("manv", "");

        rvSanPhamGoiY.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSanPhamGoiY.setHasFixedSize(true);
        rvSanPhamGoiY.setNestedScrollingEnabled(true);

    }


}
