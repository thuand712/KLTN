package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XacNhanChuyenKhoanActivity extends AppCompatActivity {

    public static  final int PERMISSION_CODE = 001;
    Intent intent;
    String mahdtong = "";
    String realpath = "";
    String makh = "";

    Toolbar toolbar;
    EditText edMaDH,edNganHangCK,edSoTKNganHangChuyen,edSoTienChuyen,edGhiChu,edNganHangNhan,edSTKNhan;
    TextView edNgayCK;
    ImageView imgAnhChiTiet;
    Button btnXacNhanThanhToan;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_chuyen_khoan);

        intent = getIntent();
        if(intent.hasExtra("mahdtong")){
            mahdtong = intent.getStringExtra("mahdtong");
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

        imgAnhChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyCapQuyen();
            }
        });

        btnXacNhanThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(realpath.equals("")){
                    Toast.makeText(XacNhanChuyenKhoanActivity.this, getResources().getString(R.string.vuilongchonanh), Toast.LENGTH_SHORT).show();
                }else {
                    xuLyXacNhanThanhToan();
                    Toast.makeText(XacNhanChuyenKhoanActivity.this, getResources().getString(R.string.xacnhanthanhtoanthanhcong), Toast.LENGTH_SHORT).show();
                }
            }
        });

        edNgayCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgayChuyenKhoan();
            }
        });

    }

    private void chonNgayChuyenKhoan() {
        final Calendar calendar = Calendar.getInstance();
        int nam = calendar.get(Calendar.YEAR);
        int thang = calendar.get(Calendar.MONTH);
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(XacNhanChuyenKhoanActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                edNgayCK.setText(dateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    private void xuLyXacNhanThanhToan() {

        progressDialog.show();
        File file = new File(realpath);
        String filepath = file.getAbsolutePath();
        String[] mangtenfile = filepath.split("\\.");
        filepath = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", filepath, requestBody);
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.upLoadHinhAnhCK(body);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if (kq.equals("Fail")) {
                    Toast.makeText(XacNhanChuyenKhoanActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                }else {
                    String ngayck = edNgayCK.getText().toString().trim();
                    String nganhangck = edNganHangCK.getText().toString().trim();
                    String stkck = edSoTKNganHangChuyen.getText().toString().trim();
                    String nganhangnhan = edNganHangNhan.getText().toString().trim();
                    String stknhan = edSTKNhan.getText().toString().trim();
                    String sotienchuyen = edSoTienChuyen.getText().toString().trim();
                    String ghichu = edGhiChu.getText().toString().trim();
                    String hinhck = "hinhanh/hinhchuyenkhoan/"+kq;
                    if(nganhangck.equals("") || stkck.equals("") || sotienchuyen.equals("") || ghichu.equals("")){
                        Toast.makeText(XacNhanChuyenKhoanActivity.this, "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                    }else {
                        DataService dataService1 = APIService.getService();
                        Call<String>call1 = dataService1.themChuyenKhoan(mahdtong,makh,ngayck,nganhangck,stkck,nganhangnhan,stknhan,sotienchuyen,ghichu,hinhck);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(XacNhanChuyenKhoanActivity.this, "Xác nhận chuyển khoản thành công !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(XacNhanChuyenKhoanActivity.this,XacNhanChuyenKhoanThanhCongActivity.class);
                                    intent.putExtra("mahdtong",mahdtong);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(XacNhanChuyenKhoanActivity.this, "Xác nhận thất bại, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if(requestCode == 001){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgAnhChiTiet.setImageBitmap(photo);
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                realpath = getRealPathFromURI(tempUri);
            }else if(requestCode == 002){
                Uri uri = data.getData();
                realpath = getRealPathFromURI(uri);
                Picasso.with(getApplicationContext()).load(uri).into(imgAnhChiTiet);
            }
        }
    }

    private void xuLyCapQuyen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);

            } else {
                handleDialogChoosePhoto();
            }
        }
    }

    public void openCamera(){
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamera, 001);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 002);
    }
    private void handleDialogChoosePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(XacNhanChuyenKhoanActivity.this);
        builder.setTitle("Choose photo from");
        builder.setView(R.layout.custom_dialog_themhinhanh);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView txtCamera = dialog.findViewById(R.id.txtMayAnh);
        TextView txtGallery = dialog.findViewById(R.id.txtHinhAnh);

        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                dialog.dismiss();
            }
        });
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            handleDialogChoosePhoto();
        }else {
            Toast.makeText(this, "Bạn không cho phép truy cập vào Camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        edMaDH = findViewById(R.id.edMaDH);
        edNgayCK = findViewById(R.id.edNgayCK);
        edNganHangCK = findViewById(R.id.edNganHangCK);
        edSoTKNganHangChuyen = findViewById(R.id.edSoTKNganHangChuyen);
        edSoTienChuyen = findViewById(R.id.edSoTienChuyen);
        edGhiChu = findViewById(R.id.edGhiChu);
        imgAnhChiTiet = findViewById(R.id.imgAnhChiTiet);
        btnXacNhanThanhToan = findViewById(R.id.btnXacNhanThanhToan);
        edNganHangNhan = findViewById(R.id.edNganHangNhan);
        edSTKNhan = findViewById(R.id.edSTKNhan);

        progressDialog = new ProgressDialog(XacNhanChuyenKhoanActivity.this);
        progressDialog.setMessage("Loading, please wait!");

        makh = getSharedPreferences("dangnhap",MODE_PRIVATE).getString("manv","");


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        edMaDH.setText(mahdtong);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}