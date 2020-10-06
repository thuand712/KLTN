package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.BaiVietLamDepAdapter;
import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.R;

public class BaiVietLamDepLuuTruActivity extends AppCompatActivity {

    Intent intent;
    ArrayList<BaiVietLamDep>baiVietLamDeps;
    BaiVietLamDepAdapter baiVietLamDepAdapter;

    Toolbar toolbar;
    RecyclerView rvBaiVietLamDepLuuTru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_viet_lam_dep_luu_tru);

        intent = getIntent();
        if(intent.hasExtra("baivietluutru")){
            baiVietLamDeps = intent.getParcelableArrayListExtra("baivietluutru");
            addControls();
            addEvents();
        }

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvBaiVietLamDepLuuTru = findViewById(R.id.rvBaiVietLamDepLuuTru);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        baiVietLamDepAdapter = new BaiVietLamDepAdapter(BaiVietLamDepLuuTruActivity.this, baiVietLamDeps);
        rvBaiVietLamDepLuuTru.setLayoutManager(new GridLayoutManager(BaiVietLamDepLuuTruActivity.this, 2));
        rvBaiVietLamDepLuuTru.setHasFixedSize(true);
        rvBaiVietLamDepLuuTru.setNestedScrollingEnabled(true);
        rvBaiVietLamDepLuuTru.setAdapter(baiVietLamDepAdapter);
        baiVietLamDepAdapter.notifyDataSetChanged();

    }
}
