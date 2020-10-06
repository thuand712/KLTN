package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.ShowHinhSanPhamAdapter;
import ducthuan.com.lamdep.R;
import me.relex.circleindicator.CircleIndicator;

public class ShowHinhSanPhamActivity extends AppCompatActivity {

    ViewPager viewPager;
    CircleIndicator indicator;
    ShowHinhSanPhamAdapter showHinhSanPhamAdapter;
    Intent intent;
    ArrayList<String> dsHinh;

    Toolbar toolbar;

    ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_hinh_san_pham);



        intent = getIntent();
        if (intent.hasExtra("hinhs")) {
            dsHinh = intent.getStringArrayListExtra("hinhs");
            viewPager = findViewById(R.id.viewPager);
            indicator = findViewById(R.id.indicator);
            imgBack = findViewById(R.id.imgBack);




            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            getDataQuangCao();
        }
    }

    private void getDataQuangCao() {
        showHinhSanPhamAdapter = new ShowHinhSanPhamAdapter(ShowHinhSanPhamActivity.this, dsHinh);
        viewPager.setAdapter(showHinhSanPhamAdapter);
        indicator.setViewPager(viewPager);
    }
}
