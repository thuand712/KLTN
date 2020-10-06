package ducthuan.com.lamdep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.BoSuuTapYeuThichActivity;
import ducthuan.com.lamdep.Adapter.SanPhamYeuThichAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SanPhamYeuThich extends Fragment {

    RecyclerView rvSanPhamYeuThich;
    ArrayList<SanPham>sanPhamYeuThiches;
    SanPhamYeuThichAdapter sanPhamYeuThichAdapter;

    TextView txtSanPhamYeuThich;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_san_pham_yeu_thich, container,false);

        rvSanPhamYeuThich = view.findViewById(R.id.rvSanPhamYeuThich);
        txtSanPhamYeuThich = view.findViewById(R.id.txtSanPhamYeuThich);
        getDataSanPhamYeuThich();

        txtSanPhamYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BoSuuTapYeuThichActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getDataSanPhamYeuThich() {

        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamYeuThich();
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamYeuThiches = (ArrayList<SanPham>) response.body();
                if(sanPhamYeuThiches.size()>0){
                    sanPhamYeuThichAdapter = new SanPhamYeuThichAdapter(getActivity(), sanPhamYeuThiches);
                    rvSanPhamYeuThich.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                    rvSanPhamYeuThich.setHasFixedSize(true);
                    rvSanPhamYeuThich.setAdapter(sanPhamYeuThichAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

            }
        });

    }
}
