package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.DiaChiAdapter;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaChiActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String manv;
    public static ArrayList<DiaChiKhachHang>diaChiKhachHangs;
    public static DiaChiAdapter diaChiAdapter;
    RecyclerView rvDiaChi;
    Toolbar toolbar;
    Button btnXacNhan;
    FloatingActionButton floatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_chi);

            addControls();
            getDiaChiKhachHang();
            addEvents();
    }

    public void getDiaChiKhachHang(){
        DataService dataService = APIService.getService();
        Call<List<DiaChiKhachHang>>callback = dataService.getDanhSachDiaChiKhachHangs(manv);
        callback.enqueue(new Callback<List<DiaChiKhachHang>>() {
            @Override
            public void onResponse(Call<List<DiaChiKhachHang>> call, Response<List<DiaChiKhachHang>> response) {
                diaChiKhachHangs = (ArrayList<DiaChiKhachHang>) response.body();
                if(diaChiKhachHangs.size()>0){
                    diaChiAdapter = new DiaChiAdapter(DiaChiActivity.this,diaChiKhachHangs);
                    rvDiaChi.setHasFixedSize(true);
                    rvDiaChi.setLayoutManager(new LinearLayoutManager(DiaChiActivity.this));
                    rvDiaChi.setAdapter(diaChiAdapter);
                    diaChiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<DiaChiKhachHang>> call, Throwable t) {

            }
        });
    }


    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaChiActivity.this);
                builder.setTitle("Thêm địa chỉ nhận hàng");
                builder.setView(R.layout.custom_dialog_themdiachi);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                final EditText edTen = dialog.findViewById(R.id.edTen);
                final EditText edDiaChi = dialog.findViewById(R.id.edDiaChi);
                final EditText edSoDienThoai = dialog.findViewById(R.id.edSoDienThoai);
                final EditText edEmail = dialog.findViewById(R.id.edEmail);
                final CheckBox chkXacNhan = dialog.findViewById(R.id.chkXacNhan);
                Button btnThem = dialog.findViewById(R.id.btnThem);
                Button btnHuy = dialog.findViewById(R.id.btnHuy);

                btnThem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (edTen.getText().toString().trim().equals("")||edDiaChi.getText().toString().trim().equals("")||edSoDienThoai.getText().toString().trim().equals("")) {
                            Toast.makeText(DiaChiActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String manv = diaChiKhachHangs.get(0).getMAKH();
                        String tennv = edTen.getText().toString().trim();
                        final String diachi = edDiaChi.getText().toString().trim();
                        String sodt = edSoDienThoai.getText().toString().trim();
                        String email = edEmail.getText().toString().trim();
                        String macdinh = "";
                        if(chkXacNhan.isChecked()){
                            macdinh = "1";
                        }else{
                            macdinh = "0";
                        }
                        DataService dataService = APIService.getService();
                        Call<String>callback = dataService.themDiaChiKhachHang(manv,tennv,diachi,sodt,email,macdinh);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(DiaChiActivity.this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                                    getDiaChiKhachHang();
                                }else {
                                    Toast.makeText(DiaChiActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });



                    }
                });


                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = diaChiAdapter.getmSelectedItem();
                ArrayList<DiaChiKhachHang>dcselected = new ArrayList<>();
                dcselected.add(diaChiKhachHangs.get(i));
                Intent intent = getIntent();
                intent.putParcelableArrayListExtra("dcseleted",dcselected);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }

    private void addControls() {

        rvDiaChi = findViewById(R.id.rvDiaChi);
        toolbar = findViewById(R.id.toolbar);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        floatButton = findViewById(R.id.floatButton);

        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);


    }
}
