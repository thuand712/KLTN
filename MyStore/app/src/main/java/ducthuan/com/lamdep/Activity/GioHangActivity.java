package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Adapter.GioHangAdapter;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    public static GioHangAdapter gioHangAdapter;
    public static ArrayList<GioHang> gioHangs;
    public static RecyclerView rvGioHang;
    public static TextView txtTongTien;
    public static LinearLayout lnGioHangTrong;
    public static TextView txtMuaSamNgay;
    public static RelativeLayout relativeLayout;
    public static CheckBox chkTatCaSP;

    static Button btnMuaHang;
    String duocchon = "0";
    boolean onpause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);


        addControls();
        getDataGioHang();
        addEvents();

    }

    private void getDataGioHang() {

        progressDialog.show();
        sharedPreferences = getSharedPreferences("dangnhap", MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");

        DataService dataService = APIService.getService();
        Call<List<GioHang>> callback = dataService.getDanhSachSPGioHang(manv);
        callback.enqueue(new Callback<List<GioHang>>() {
            @Override
            public void onResponse(Call<List<GioHang>> call, Response<List<GioHang>> response) {
                gioHangs = (ArrayList<GioHang>) response.body();
                if (gioHangs.size() > 0) {
                    gioHangAdapter = new GioHangAdapter(GioHangActivity.this, gioHangs);
                    rvGioHang.setLayoutManager(new LinearLayoutManager(GioHangActivity.this));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(GioHangActivity.this, DividerItemDecoration.VERTICAL);
                    rvGioHang.addItemDecoration(dividerItemDecoration);
                    rvGioHang.setNestedScrollingEnabled(true);
                    rvGioHang.setHasFixedSize(true);
                    rvGioHang.setAdapter(gioHangAdapter);
                    gioHangAdapter.notifyDataSetChanged();

                    progressDialog.dismiss();

                    getTongTien();
                    getTrangThaiChonTatCa();
                    capNhapChonTatCa();


                } else {
                    progressDialog.dismiss();
                    rvGioHang.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    lnGioHangTrong.setVisibility(View.VISIBLE);
                    txtMuaSamNgay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(GioHangActivity.this, TrangChuActivity.class));
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<GioHang>> call, Throwable t) {

            }
        });
    }

    public static void getTrangThaiChonTatCa() {
        int dem=0;
        for(int i = 0; i< gioHangs.size();i++){
            if(gioHangs.get(i).getDUOCCHON().equals("1")){
                dem++;
            }
        }
        if(dem==gioHangs.size()){
            chkTatCaSP.setChecked(true);
        }else {
            chkTatCaSP.setChecked(false);
        }
        if(dem==0){
            btnMuaHang.setEnabled(false);
            btnMuaHang.setBackgroundResource(R.drawable.custom_button_nen_xam_bogoc);
        }else if(dem>0){
            btnMuaHang.setEnabled(true);
            btnMuaHang.setBackgroundResource(R.drawable.custom_border_button);
        }


    }

    private void capNhapChonTatCa() {
        chkTatCaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkTatCaSP.isChecked()){
                    duocchon="1";
                }else{
                    duocchon="0";
                }
                sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
                String manv=sharedPreferences.getString("manv","");
                DataService dataService = APIService.getService();
                Call<String>callback = dataService.capNhapChonTatCaSPGioHang(manv,duocchon);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if (kq.equals("OK")){
                            for(int i = 0;i<gioHangs.size();i++){
                                gioHangs.get(i).setDUOCCHON(duocchon);
                            }
                            gioHangAdapter.notifyDataSetChanged();
                            getTongTien();
                            getTrangThaiChonTatCa();
                        }else {
                            Toast.makeText(GioHangActivity.this, "Thao tác lỗi, vui lòng thử lại sau !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(GioHangActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


    public static void getTongTien(){
        long tong = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        for (int i = 0; i < gioHangs.size(); i++) {
            if(gioHangs.get(i).getDUOCCHON().equals("1")){
                int giaspchuakm = Integer.parseInt(gioHangs.get(i).getGIASP());
                int km = Integer.parseInt(gioHangs.get(i).getKHUYENMAI());
                int sl = Integer.parseInt(gioHangs.get(i).getSOLUONG());
                tong += (sl * (giaspchuakm / 100) * (100 - km));
            }

        }
        txtTongTien.setText(decimalFormat.format(tong) + "đ");
    }


    //

    private void addEvents() {
        //su kien back toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                String manv = sharedPreferences.getString("manv", "");
                DataService dataService = APIService.getService();
                Call<List<DiaChiKhachHang>> callback = dataService.getDiaChiKhachHangs(manv);
                callback.enqueue(new Callback<List<DiaChiKhachHang>>() {
                    @Override
                    public void onResponse(Call<List<DiaChiKhachHang>> call, Response<List<DiaChiKhachHang>> response) {
                        ArrayList<DiaChiKhachHang> diaChiKhachHangs = (ArrayList<DiaChiKhachHang>) response.body();
                        ArrayList<GioHang>gioHangDuocChon = new ArrayList<>();
                        for(int i = 0; i < gioHangs.size();i++)
                        {
                            if(gioHangs.get(i).getDUOCCHON().equals("1")){
                                gioHangDuocChon.add(gioHangs.get(i));
                            }
                        }
                        if (diaChiKhachHangs.size() > 0) {
                            Intent intent = new Intent(GioHangActivity.this, XacNhanThongTinMuaHangActivity.class);
                            intent.putParcelableArrayListExtra("giohang", gioHangDuocChon);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GioHangActivity.this, DiaChiNhanHangActivity.class);
                            intent.putParcelableArrayListExtra("giohang", gioHangDuocChon);
                            startActivity(intent);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<DiaChiKhachHang>> call, Throwable t) {

                    }
                });

            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        rvGioHang = findViewById(R.id.rvGioHang);
        txtTongTien = findViewById(R.id.txtTongTien);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        lnGioHangTrong = findViewById(R.id.lnGioHangTrong);
        txtMuaSamNgay = findViewById(R.id.txtMuaSamNgay);
        relativeLayout = findViewById(R.id.relativeLayout);
        chkTatCaSP = findViewById(R.id.chkTatCaSP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        progressDialog = new ProgressDialog(GioHangActivity.this);
        progressDialog.setMessage("Loading, please wait!");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause==true){
            getDataGioHang();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}
