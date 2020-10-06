package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.DanhGiaAdapter;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.R;

public class DanhGiaActivity extends AppCompatActivity {

    Intent intent;

    ArrayList<DanhGia>danhGias;
    DanhGiaAdapter adapter;
    ArrayList<DanhGia>tempDanhGias;

    Toolbar toolbar;
    TabLayout tabLayout;
    RecyclerView rvDanhGia;

    int i5 = 0,i4 = 0,i3 = 0,i2 = 0,i1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        intent = getIntent();
        if(intent.hasExtra("danhgias")){
            danhGias = intent.getParcelableArrayListExtra("danhgias");
            addControls();
            getTatCaDanhGia();
            addEvents();
        }
    }

    private void getTatCaDanhGia() {
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,danhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void addEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: getTatCaDanhGia();break;
                    case 1:showDG5Sao();break;
                    case 2:showDG4Sao();break;
                    case 3:showDG3Sao();break;
                    case 4:showDG2Sao();break;
                    case 5:showDG1Sao();break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showDG1Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("1")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG2Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("2")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG3Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("3")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG4Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("4")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }

    private void showDG5Sao() {
        tempDanhGias = new ArrayList<>();
        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("5")){
                tempDanhGias.add(danhGias.get(i));
            }
        }
        adapter = new DanhGiaAdapter(DanhGiaActivity.this,tempDanhGias);
        rvDanhGia.setAdapter(adapter);
    }
    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        rvDanhGia = findViewById(R.id.rvDanhGia);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvDanhGia.setLayoutManager(new LinearLayoutManager(DanhGiaActivity.this));
        rvDanhGia.setHasFixedSize(true);
        rvDanhGia.setNestedScrollingEnabled(true);
        rvDanhGia.addItemDecoration(new DividerItemDecoration(DanhGiaActivity.this,DividerItemDecoration.VERTICAL));

        for (int i = 0; i < danhGias.size(); i++) {
            if(danhGias.get(i).getSOSAO().equals("1")){
                i1++;
            }
            if(danhGias.get(i).getSOSAO().equals("2")){
                i2++;
            }
            if(danhGias.get(i).getSOSAO().equals("3")){
                i3++;
            }
            if(danhGias.get(i).getSOSAO().equals("4")){
                i4++;
            }
            if(danhGias.get(i).getSOSAO().equals("5")){
                i5++;
            }
        }
        tabLayout.getTabAt(0).setText("Tất cả\n("+danhGias.size()+")");
        tabLayout.getTabAt(1).setText("5 Sao\n("+i5+")");
        tabLayout.getTabAt(2).setText("4 Sao\n("+i4+")");
        tabLayout.getTabAt(3).setText("3 Sao\n("+i3+")");
        tabLayout.getTabAt(4).setText("2 Sao\n("+i2+")");
        tabLayout.getTabAt(5).setText("1 Sao\n("+i1+")");
    }
}
