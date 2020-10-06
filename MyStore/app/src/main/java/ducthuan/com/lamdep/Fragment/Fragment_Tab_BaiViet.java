package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.BaiVietLamDepLuuTruActivity;
import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Adapter.LoaiLamDepAdapter;
import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_BaiViet extends Fragment implements View.OnClickListener,TabHost.TabContentFactory {

    SharedPreferences sharedPreferences;
    String manv = "";
    View view;
    TextView txtMucLuuTru;
    ArrayList<BaiVietLamDep>baiVietLamDeps;

    TabHost tabHost;
    ArrayList<LoaiLamDep>loaiLamDeps;
    LoaiLamDepAdapter loaiLamDepAdapter;
    RecyclerView rvLoaiLamDep;

    boolean onpause = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_bai_viet,container,false);
        addControls();
        getBaiVietLuuTru();
        addEvents();

        return view;
    }

    private void getBaiVietLuuTru() {
        if(!manv.equals("")){
            DataService dataService = APIService.getService();
            Call<List<BaiVietLamDep>>call = dataService.getBaiVietLamDepLuuTru(manv);
            call.enqueue(new Callback<List<BaiVietLamDep>>() {
                @Override
                public void onResponse(Call<List<BaiVietLamDep>> call, Response<List<BaiVietLamDep>> response) {
                    baiVietLamDeps = (ArrayList<BaiVietLamDep>) response.body();
                    if(baiVietLamDeps.size()>0){
                        txtMucLuuTru.setText("Mục lưu trữ ("+baiVietLamDeps.size()+")");
                    }else {
                        txtMucLuuTru.setText("Mục lưu trữ");
                    }
                }

                @Override
                public void onFailure(Call<List<BaiVietLamDep>> call, Throwable t) {

                }
            });
        }
    }

    private void addControls() {

        txtMucLuuTru = view.findViewById(R.id.txtMucLuuTru);
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");

        tabHost = view.findViewById(R.id.tabHost);
        //setup cho tabhost
        tabHost.setup();

        Resources res = getResources();
        Configuration cfg = res.getConfiguration();
        boolean hor = cfg.orientation == Configuration.ORIENTATION_PORTRAIT;

        if (hor) {
            TabWidget tw = tabHost.getTabWidget();
            tw.setOrientation(LinearLayout.VERTICAL);
        }

        tabHost.addTab(tabHost.newTabSpec("1")
                .setIndicator(createIndicatorView(tabHost, "Da đẹp", getResources().getDrawable(R.drawable.ic_tab_skin)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("2")
                .setIndicator(createIndicatorView(tabHost, "Trang điểm", getResources().getDrawable(R.drawable.ic_make_up)))
                .setContent(this));

        tabHost.addTab(tabHost.newTabSpec("3")
                .setIndicator(createIndicatorView(tabHost, "Tóc đẹp", getResources().getDrawable(R.drawable.ic_tab_hair)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("4")
                .setIndicator(createIndicatorView(tabHost, "Mặc đẹp", getResources().getDrawable(R.drawable.ic_tab_style)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("5")
                .setIndicator(createIndicatorView(tabHost, "Dáng đẹp", getResources().getDrawable(R.drawable.ic_tab_workout)))
                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec("6")
                .setIndicator(createIndicatorView(tabHost, "Tập luyện", getResources().getDrawable(R.drawable.ic_fitness)))
                .setContent(this));
    }

    private View createIndicatorView(TabHost tabHost, CharSequence label, Drawable icon) {

        //lấy bố trí của cái activity để đưa vào tabhost
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View tabIndicator = inflater.inflate(R.layout.tab_indicator,
                tabHost.getTabWidget(), // tab widget is the parent
                false); // no inflate params

        final TextView tv =  tabIndicator.findViewById(R.id.title);
        tv.setText(label);

        final ImageView iconView = tabIndicator.findViewById(R.id.icon);
        iconView.setImageDrawable(icon);

        return tabIndicator;
    }

    private void addEvents() {
        txtMucLuuTru.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.txtMucLuuTru:
                if(manv.equals("")){
                    startActivity(new Intent(getContext(), DangNhapActivity.class));
                }else {
                    if(baiVietLamDeps.size()>0){
                        intent = new Intent(getContext(), BaiVietLamDepLuuTruActivity.class);
                        intent.putParcelableArrayListExtra("baivietluutru",baiVietLamDeps);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(), "Chưa có bài viết nào được lưu trữ", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    @Override
    public View createTabContent(String tag) {

        loaiLamDeps = new ArrayList<>();
        rvLoaiLamDep = new RecyclerView(getContext());
        rvLoaiLamDep.setNestedScrollingEnabled(true);
        rvLoaiLamDep.setHasFixedSize(true);
        rvLoaiLamDep.setLayoutManager(new LinearLayoutManager(getContext()));

        DataService dataService = APIService.getService();
        Call<List<LoaiLamDep>>call = dataService.getLoaiLamDep(tag);
        call.enqueue(new Callback<List<LoaiLamDep>>() {
            @Override
            public void onResponse(Call<List<LoaiLamDep>> call, Response<List<LoaiLamDep>> response) {
                loaiLamDeps = (ArrayList<LoaiLamDep>) response.body();
                if(loaiLamDeps.size()>0){
                    loaiLamDepAdapter = new LoaiLamDepAdapter(getContext(),loaiLamDeps);
                    rvLoaiLamDep.setAdapter(loaiLamDepAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<LoaiLamDep>> call, Throwable t) {

            }
        });

        return rvLoaiLamDep;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause==true){
            getBaiVietLuuTru();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
