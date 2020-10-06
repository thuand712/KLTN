package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.TimKiemHangDauActivity;
import ducthuan.com.lamdep.Adapter.SanPhamTimKiemAdapter;
import ducthuan.com.lamdep.Adapter.SanPhamTimKiemPhoBienAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.TimKiem;
import ducthuan.com.lamdep.Model.TimKiemPhoBien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_TimKiem extends Fragment {
    View view;
    RecyclerView rvTimKiemHangDau;
    ArrayList<TimKiem>timKiems;
    ArrayList<TimKiemPhoBien>timKiemPhoBiens;
    SanPhamTimKiemAdapter sanPhamTimKiemAdapter;
    SanPhamTimKiemPhoBienAdapter sanPhamTimKiemPhoBienAdapter;
    String manv = "";
    RelativeLayout relativeLayout;

    TextView txtXemThemTimKiem,txtTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tim_kiem, container,false);

        addControls();
        getDataSanPhamTimKiem();
        addEvents();

        return view;
    }

    private void addEvents() {
        txtXemThemTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manv.equals("")){
                    Intent intent = new Intent(getActivity(), TimKiemHangDauActivity.class);
                    intent.putParcelableArrayListExtra("timkiemphobien",timKiemPhoBiens);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), TimKiemHangDauActivity.class);
                    intent.putParcelableArrayListExtra("timkiem",timKiems);
                    startActivity(intent);
                }
            }
        });

    }

    private void addControls() {
        rvTimKiemHangDau = view.findViewById(R.id.rvTimKiemHangDau);
        txtXemThemTimKiem = view.findViewById(R.id.txtXemThemTimKiem);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        txtTitle = view.findViewById(R.id.txtTitle);

        manv = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE).getString("manv","");
        rvTimKiemHangDau.setLayoutManager(new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL,false));
        rvTimKiemHangDau.setHasFixedSize(true);
        rvTimKiemHangDau.setNestedScrollingEnabled(true);
    }

    private void getDataSanPhamTimKiem() {
        DataService dataService = APIService.getService();

        if(manv.equals("")){
            txtTitle.setText("TÌM KIẾM PHỔ BIẾN");
            timKiemPhoBiens = new ArrayList<>();
            Call<List<TimKiemPhoBien>>callback = dataService.getTimKiemPhoBien();
            callback.enqueue(new Callback<List<TimKiemPhoBien>>() {
                @Override
                public void onResponse(Call<List<TimKiemPhoBien>> call, Response<List<TimKiemPhoBien>> response) {
                    timKiemPhoBiens = (ArrayList<TimKiemPhoBien>) response.body();
                    if(timKiemPhoBiens.size()>0){
                        sanPhamTimKiemPhoBienAdapter = new SanPhamTimKiemPhoBienAdapter(getContext(),timKiemPhoBiens);
                        rvTimKiemHangDau.setAdapter(sanPhamTimKiemPhoBienAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<TimKiemPhoBien>> call, Throwable t) {

                }
            });
        }else {

            timKiems = new ArrayList<>();
            Call<List<TimKiem>>callback = dataService.getTimKiemHangDauNV(manv);
            callback.enqueue(new Callback<List<TimKiem>>() {
                @Override
                public void onResponse(Call<List<TimKiem>> call, Response<List<TimKiem>> response) {
                    timKiems = (ArrayList<TimKiem>) response.body();
                    if(timKiems.size()>0){
                        relativeLayout.setVisibility(View.VISIBLE);
                        sanPhamTimKiemAdapter = new SanPhamTimKiemAdapter(getContext(),timKiems);
                        rvTimKiemHangDau.setAdapter(sanPhamTimKiemAdapter);
                    }else {
                        relativeLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<TimKiem>> call, Throwable t) {

                }
            });
        }
    }
}
