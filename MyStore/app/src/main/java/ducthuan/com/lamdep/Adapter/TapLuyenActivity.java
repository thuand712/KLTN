package ducthuan.com.lamdep.Adapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ducthuan.com.lamdep.Model.TapLuyen;
import ducthuan.com.lamdep.R;

public class TapLuyenActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgHinh;
    Button btnBatDau;
    TextView txtLapLai;
    Intent intent;
    TapLuyen tapLuyen;

    int laplai = 0;
    int thoigianlaplai = 0;
    int thoigianbaitap = 0;

    boolean batdau = true;

    ArrayList<Bitmap>bitmaps;
    int vitri = 0;

    CountDownTimer countDownTimer;
    AssetManager assetManager;

    MediaPlayer mediaPlayer;
    MediaPlayer nhachoanthanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_luyen);

        intent = getIntent();
        if(intent.hasExtra("tapluyen")){
            tapLuyen = intent.getParcelableExtra("tapluyen");
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


        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(batdau){
                    btnBatDau.setText("Dừng lại");
                    batdau = false;
                    mediaPlayer.start();
                    countDownTimer.start();


                }else {
                    btnBatDau.setText("Bắt đầu");
                    countDownTimer.cancel();
                    batdau = true;
                }
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgHinh = findViewById(R.id.imgHinh);
        btnBatDau = findViewById(R.id.btnBatDau);
        txtLapLai = findViewById(R.id.txtLapLai);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        getSupportActionBar().setTitle(tapLuyen.getTentapluyen());

        laplai = Integer.parseInt(tapLuyen.getLaplai());
        thoigianlaplai = Integer.parseInt(tapLuyen.getThoigianlaplai());




        assetManager = getAssets();

        String []s = tapLuyen.getHinh().split("\\,");
        bitmaps = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            try {
                InputStream inputStream = assetManager.open(s[i]);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmaps.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(laplai == 0){
            txtLapLai.setText("Thực hiện theo bài tập "+(thoigianlaplai/1000)+"s");
            thoigianbaitap = thoigianlaplai;
        }else{
            txtLapLai.setText("Thực hiện theo bài tập "+tapLuyen.getLaplai()+" lần");
            thoigianbaitap = thoigianlaplai*laplai*bitmaps.size();
        }

        imgHinh.setImageBitmap(bitmaps.get(0));






        nhachoanthanh = MediaPlayer.create(getApplicationContext(),R.raw.td_cheer);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.coi);

        countDownTimer = new CountDownTimer(thoigianbaitap,500) {
            @Override
            public void onTick(long l) {
                imgHinh.setImageBitmap(bitmaps.get(vitri));
                vitri = vitri + 1;
                if(vitri == bitmaps.size()){
                    vitri = 0;
                }
            }

            @Override
            public void onFinish() {

                AlertDialog.Builder builder = new AlertDialog.Builder(TapLuyenActivity.this);
                builder.setView(R.layout.custom_dialog_tapluyen_hoanthanh);
                final AlertDialog dialog = builder.create();
                dialog.show();

                ImageView imgClose = dialog.findViewById(R.id.imgClose);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnBatDau.setText("Bắt đầu");
                batdau = true;
                nhachoanthanh.start();
            }
        };


    }

}
