package ducthuan.com.lamdep.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Adapter.DoanhThuAdapter;
import ducthuan.com.lamdep.Model.DoanhThu;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoanhThuActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String manv = "";

    Toolbar toolbar;
    TextView txtTongTien, txtThoiGian, txtTuNgay, txtDenNgay;
    LinearLayout linearLayout;
    RecyclerView rvDoanhThu;

    Calendar calendar;
    Date ngayHienTai;

    ArrayList<DoanhThu>doanhThus;
    DoanhThuAdapter doanhThuAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);
        addControls();
        hanlerDoanhThu();
        addEvents();
    }

    private void hanlerDoanhThu() {

        progressDialog.show();
        rvDoanhThu.setHasFixedSize(true);
        rvDoanhThu.setNestedScrollingEnabled(true);
        rvDoanhThu.setLayoutManager(new LinearLayoutManager(DoanhThuActivity.this));
        doanhThus = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<DoanhThu>>call = dataService.getDoanhThu(manv,txtTuNgay.getText().toString(),txtDenNgay.getText().toString());
        call.enqueue(new Callback<List<DoanhThu>>() {
            @Override
            public void onResponse(Call<List<DoanhThu>> call, Response<List<DoanhThu>> response) {
                doanhThus = (ArrayList<DoanhThu>) response.body();
                if(doanhThus.size()>0){
                    int tongtien = 0;
                    for (int i = 0; i < doanhThus.size(); i++) {
                        String s = doanhThus.get(i).getTONGTIEN().replace(".","");
                        String s1 = s.replace("đ","");

                        tongtien += Integer.parseInt(s1);
                    }

                    rvDoanhThu.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    doanhThuAdapter = new DoanhThuAdapter(DoanhThuActivity.this,doanhThus);
                    rvDoanhThu.setAdapter(doanhThuAdapter);

                    //Format gia tien
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
                    decimalFormatSymbols.setGroupingSeparator('.');
                    decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
                    txtTongTien.setText(decimalFormat.format(tongtien)+"đ");

                }else {
                    rvDoanhThu.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    txtTongTien.setText("0đ");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DoanhThu>> call, Throwable t) {
                Log.d("kiemtra",t.toString());
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

        txtTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hanlerTuNgay();
            }
        });

        txtDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hanlerDenNgay();
            }
        });

    }

    private void hanlerDenNgay() {
        calendar = Calendar.getInstance();
        final int nam = calendar.get(Calendar.YEAR);
        final int thang = calendar.get(Calendar.MONTH);
        final int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        //5 tham số truyền vào lưu ý
        DatePickerDialog date = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date1 = dateFormat.parse(day + "/" + (month+1) + "/" + year);
                            Date date2 = dateFormat.parse(ngay + "/" + (thang+1) + "/" + nam);
                            Date date3 = dateFormat.parse(txtTuNgay.getText().toString());
                            if (date1.compareTo(date2) > 0) {
                                Toast.makeText(DoanhThuActivity.this, "Ngày chọn nhỏ hơn: " + dateFormat.format(ngayHienTai), Toast.LENGTH_SHORT).show();
                                return;
                            }else if(date1.compareTo(date3)<=0){
                                Toast.makeText(DoanhThuActivity.this, "Ngày chọn lớn hơn: " + txtTuNgay.getText(), Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                //set thời gian bằng thời gian người dùng chọn
                                calendar.set(year, month, day);
                                txtDenNgay.setText(dateFormat.format(calendar.getTime()));
                                showTxtThoiGian();
                                hanlerDoanhThu();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, nam, thang, ngay); //Lưu ý: truyền tháng 1 sẽ hiển thị tháng 2
        date.show();
    }

    private void hanlerTuNgay() {
        calendar = Calendar.getInstance();
        final int nam = calendar.get(Calendar.YEAR);
        final int thang = calendar.get(Calendar.MONTH);
        final int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        //5 tham số truyền vào lưu ý
        DatePickerDialog date = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date date1 = dateFormat.parse(day + "/" + (month+1) + "/" + year);
                            Date date2 = dateFormat.parse(txtDenNgay.getText().toString());

                            Log.d("sss",date1 +"-"+date2);
                            if (date1.compareTo(date2) <= 0) {
                                //set thời gian bằng thời gian người dùng chọn
                                calendar.set(year, month, day);
                                txtTuNgay.setText(dateFormat.format(calendar.getTime()));
                                showTxtThoiGian();
                                hanlerDoanhThu();

                            } else {
                                Toast.makeText(DoanhThuActivity.this, "Ngày chọn nhỏ hơn: " + txtDenNgay.getText(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, nam, thang, ngay); //Lưu ý: truyền tháng 1 sẽ hiển thị tháng 2
        date.show();

    }

    public void showTxtThoiGian() {
        txtThoiGian.setText("Đã thanh toán (" + txtTuNgay.getText() + " " + getResources().getString(R.string.endash) + " "
                + txtDenNgay.getText() + ")");
    }

    private void addControls() {

        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        manv = sharedPreferences.getString("manv","");
        progressDialog = new ProgressDialog(DoanhThuActivity.this);
        progressDialog.setMessage("Loading, please wait!");

        toolbar = findViewById(R.id.toolbar);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtTuNgay = findViewById(R.id.txtTuNgay);
        txtDenNgay = findViewById(R.id.txtDenNgay);
        linearLayout = findViewById(R.id.linearLayout);
        rvDoanhThu = findViewById(R.id.rvDoanhThu);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        int nam = calendar1.get(Calendar.YEAR);
        int thang = calendar1.get(Calendar.MONTH);
        int ngay = calendar1.get(Calendar.DAY_OF_MONTH);

        try {
            ngayHienTai = sdf.parse(ngay + "/" + (thang + 1) + "/" + nam);

            calendar1.setTime(ngayHienTai);
            txtDenNgay.setText(sdf.format(calendar1.getTime()));
            calendar1.roll(Calendar.DATE, -3);
            txtTuNgay.setText(sdf.format(calendar1.getTime()));
            showTxtThoiGian();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
