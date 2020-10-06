package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import ducthuan.com.lamdep.Adapter.DangNhapDangKyViewPagerAdapter;
import ducthuan.com.lamdep.Fragment.Fragment_DangKy;
import ducthuan.com.lamdep.Fragment.Fragment_DangNhap;
import ducthuan.com.lamdep.R;

public class DangNhapActivity extends AppCompatActivity {
    TabLayout tabLayoutDangNhapDangKy;
    ViewPager viewPagerDangNhapDangKy;
    Toolbar toolbarDangNhapDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
    }

    private void AnhXa() {
        toolbarDangNhapDangKy = findViewById(R.id.toolbarDangNhapDangKy);
        setSupportActionBar(toolbarDangNhapDangKy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDangNhapDangKy.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        toolbarDangNhapDangKy.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayoutDangNhapDangKy = findViewById(R.id.tabLayoutDangNhapDangKy);
        viewPagerDangNhapDangKy = findViewById(R.id.viewPagerDangNhapDangKy);
        DangNhapDangKyViewPagerAdapter dangNhapDangKyViewPagerAdapter = new DangNhapDangKyViewPagerAdapter(getSupportFragmentManager());
        dangNhapDangKyViewPagerAdapter.addFragment(new Fragment_DangNhap(),"Đăng nhập");
        dangNhapDangKyViewPagerAdapter.addFragment(new Fragment_DangKy(),"Đăng ký");

        viewPagerDangNhapDangKy.setAdapter(dangNhapDangKyViewPagerAdapter);

        tabLayoutDangNhapDangKy.setupWithViewPager(viewPagerDangNhapDangKy);
    }
}
