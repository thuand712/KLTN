package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.ObjectClass.AlarmReceiver;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiVietLamDepActivity extends AppCompatActivity {

    Intent intent;
    Intent intentBaoThuc;
    BaiVietLamDep baiVietLamDep;
    SharedPreferences sharedPreferences;
    String manv;

    ImageView imgBack, imgLuuTru;
    TextView txtTenBaiViet;
    LinearLayout content;
    LinearLayout linearLayout;
    ImageView imgChiaSeBaiViet;

    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_viet_lam_dep);
        intent = getIntent();
        if (intent.hasExtra("baivietlamdep")) {
            baiVietLamDep = intent.getParcelableExtra("baivietlamdep");
            createNotificationChannel();
            addControls();
            getTinhTrangLuuBaiViet();
            addEvents();
        }


    }

    private void getTinhTrangLuuBaiViet() {
        if(!manv.equals("")){
            DataService dataService = APIService.getService();
            Call<String>call = dataService.getTinhTrangLuuBaiVietLamDep(baiVietLamDep.getMABAIVIETLAMDEP(), manv);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if(kq.equals("1")){
                        imgLuuTru.setImageResource(R.drawable.ic_daluutru_white_24dp);
                    }else{
                        imgLuuTru.setImageResource(R.drawable.ic_luutru_white_24dp);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgLuuTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manv.equals("")) {
                    startActivity(new Intent(BaiVietLamDepActivity.this, DangNhapActivity.class));
                } else {
                    DataService dataService = APIService.getService();
                    Call<String>call = dataService.capNhapLuuTruBaiViet(baiVietLamDep.getMABAIVIETLAMDEP(),manv);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq = response.body();
                            if(kq.equals("DaLuu")){
                                Toast.makeText(BaiVietLamDepActivity.this, "Đã lưu bài viết vào mục lưu trữ", Toast.LENGTH_SHORT).show();
                            }
                            getTinhTrangLuuBaiViet();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaiVietLamDepActivity.this);
                builder.setView(R.layout.custom_dialog_nhac_nho_bai_viet);
                builder.setIcon(R.drawable.ic_dongho_black_24dp);
                builder.setTitle("Tạo nhắc nhở");
                final AlertDialog dialog = builder.create();
                dialog.show();
                final Button btnHenGio = dialog.findViewById(R.id.btnHenGio);
                final Button btnDungLai = dialog.findViewById(R.id.btnDungLai);
                final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
                calendar = Calendar.getInstance();
                //cho phep truy cap vao he thong bao dong cua may, ALARM SERVICE bao thuc
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                intentBaoThuc = new Intent(BaiVietLamDepActivity.this, AlarmReceiver.class);
                intentBaoThuc.putExtra("tenbaiviet",txtTenBaiViet.getText().toString());

                btnHenGio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                        int gio = timePicker.getCurrentHour();
                        int phut = timePicker.getCurrentMinute();
                        pendingIntent = PendingIntent.getBroadcast(BaiVietLamDepActivity.this,0,
                                intentBaoThuc,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                        dialog.dismiss();
                    }
                });

                btnDungLai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Ten kenh nhac nho";
            String description = "Kenh nhac nho";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addControls() {
        content = findViewById(R.id.content);
        imgBack = findViewById(R.id.imgBack);
        imgLuuTru = findViewById(R.id.imgLuuTru);
        txtTenBaiViet = findViewById(R.id.txtTenBaiViet);
        linearLayout = findViewById(R.id.linearLayout);
        imgChiaSeBaiViet = findViewById(R.id.imgChiaSeBaiViet);



        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        manv = sharedPreferences.getString("manv", "");


        if(baiVietLamDep.getMALOAILAMDEP().equals("13")){
            txtTenBaiViet.setText("Kiểu tóc");
        }else{
            txtTenBaiViet.setText(baiVietLamDep.getTIEUDEBAIVIETLAMDEP());
        }

        String[] chuoi = baiVietLamDep.getLINKBAIVIETLAMDEP().split("\\;");
        for (int i = 0; i < chuoi.length; i++) {
            if (chuoi[i].contains(".jpg") || chuoi[i].contains(".png")) {
                ImageView imageView = new ImageView(BaiVietLamDepActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,40);
                imageView.setLayoutParams(params);

                //full height cho imageview
                imageView.setAdjustViewBounds(true);
                Picasso.with(BaiVietLamDepActivity.this).load(TrangChuActivity.base_url+chuoi[i])
                        .placeholder(R.drawable.noimage).error(R.drawable.error).into(imageView);
                content.addView(imageView);
            } else {
                TextView textView = new TextView(BaiVietLamDepActivity.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.mauden));
                textView.setText(android.text.Html.fromHtml(chuoi[i]));
                content.addView(textView);
            }
        }


    }
}
