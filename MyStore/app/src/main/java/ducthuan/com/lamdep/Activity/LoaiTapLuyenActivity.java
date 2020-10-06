package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.LoaiTapLuyenAdapter;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.Model.TapLuyen;
import ducthuan.com.lamdep.R;

public class LoaiTapLuyenActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    Intent intent;
    LoaiLamDep loaiLamDep;
    ImageView imgHinhLoaiTapLuyen,imgBack;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView txtSoLuong,txtTitle;

    LoaiTapLuyenAdapter loaiTapLuyenAdapter;
    ArrayList<TapLuyen>tapLuyens;
    RecyclerView rvLoaiTapLuyen;

    String DATABASE_NAME = "dbtapluyen.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.transparent));
        }*/


        //thay đổi màu status bar
        /*Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.mau60000000));
        }*/

        setContentView(R.layout.activity_loai_tap_luyen);
        intent = getIntent();
        if (intent.hasExtra("loailamdep")) {
            loaiLamDep = intent.getParcelableExtra("loailamdep");

            saoChepDatabase();
            addControls();
            getDanhSachTapLuyen();
            addEvents();
        }
    }

    private void getDanhSachTapLuyen() {

        tapLuyens = new ArrayList<>();

        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        String malamdep = "";
        if(loaiLamDep.getTIEUDELOAILAMDEP().equals("Giảm cân")){
            malamdep = "1";
        }else if(loaiLamDep.getTIEUDELOAILAMDEP().equals("Tập bụng")){
            malamdep = "2";
        }else {
            malamdep = "3";
        }

        Cursor cursor = database.rawQuery("SELECT * FROM loaitapluyen WHERE MACHA = " + malamdep,null);

        while (cursor.moveToNext()){
            TapLuyen tapLuyen = new TapLuyen();
            String maloai = cursor.getString(0);
            String tenloai = cursor.getString(1);
            String hinh = cursor.getString(2);
            String macha = cursor.getString(3);
            String laplai = cursor.getString(4);
            String thoigianlaplai = cursor.getString(5);
            tapLuyen.setIdtapluyen(maloai);
            tapLuyen.setTentapluyen(tenloai);
            tapLuyen.setHinh(hinh);
            tapLuyen.setMacha(macha);
            tapLuyen.setLaplai(laplai);
            tapLuyen.setThoigianlaplai(thoigianlaplai);

            tapLuyens.add(tapLuyen);
        }
        cursor.close();

        txtSoLuong.setText(tapLuyens.size() + "\n" + "Bài tập");


        loaiTapLuyenAdapter = new LoaiTapLuyenAdapter(LoaiTapLuyenActivity.this,tapLuyens);
        rvLoaiTapLuyen.setHasFixedSize(true);
        rvLoaiTapLuyen.setNestedScrollingEnabled(true);
        rvLoaiTapLuyen.setLayoutManager(new LinearLayoutManager(LoaiTapLuyenActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(LoaiTapLuyenActivity.this,DividerItemDecoration.VERTICAL);
        rvLoaiTapLuyen.addItemDecoration(dividerItemDecoration);
        rvLoaiTapLuyen.setAdapter(loaiTapLuyenAdapter);
        loaiTapLuyenAdapter.notifyDataSetChanged();


    }


    private void addControls() {
        imgHinhLoaiTapLuyen = findViewById(R.id.imgHinhLoaiTapLuyen);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        rvLoaiTapLuyen = findViewById(R.id.rvLoaiTapLuyen);
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);

        txtTitle.setText(loaiLamDep.getTIEUDELOAILAMDEP());
        Picasso.with(LoaiTapLuyenActivity.this).load(TrangChuActivity.base_url + loaiLamDep.getHINHLOAILAMDEP())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhLoaiTapLuyen);

    }

    private void addEvents() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (collapsingToolbarLayout.getHeight() + i <= ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    txtSoLuong.setAlpha(0);
                } else {
                    txtSoLuong.setAlpha(1);
                }
            }
        });
    }



    //3 dòng lệnh coppy dataAssets vào hệ thống
    private void saoChepDatabase() {

        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Sao chép thành công CSDL vào hệ thống", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }


            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDatabasePath() {
        //dataDir tro den dung package
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }



}
