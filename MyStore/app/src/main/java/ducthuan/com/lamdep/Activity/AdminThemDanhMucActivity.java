package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminThemDanhMucActivity extends AppCompatActivity {
    public static final int icon_code = 001;
    public static final int hinh_code = 002;
    public static final int permission_code_icon = 003;
    public static final int permission_code_hinh = 004;
    String realpath_icon = "";
    String realpath_hinh = "";

    ProgressDialog progressDialog;

    Toolbar toolbar;
    EditText edTenDM;
    ImageView imgIcon,imgHinh;
    Button btnThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_them_danh_muc);
        addControls();
        addEvents();
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                xuLyThemDanhMuc();

            }
        });
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionIcon();
            }
        });

        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionHinh();
            }
        });
    }

    private void xuLyThemDanhMuc() {
        if(realpath_icon.equals("") && realpath_hinh.equals("") && edTenDM.getText().toString().trim().equals("")){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            File file1 = new File(realpath_icon);
            File file2 = new File(realpath_hinh);
            //lấy ra đường dẫn file này
            String filepath1 = file1.getAbsolutePath();
            String filepath2 = file2.getAbsolutePath();

            String[] mangtenfile1 = filepath1.split("\\.");
            filepath1 = mangtenfile1[0] + System.currentTimeMillis() + "." + mangtenfile1[1];

            String[] mangtenfile2 = filepath2.split("\\.");
            filepath2 = mangtenfile2[0] + System.currentTimeMillis() + "." + mangtenfile2[1];

            //lấy ra kiểm tra lại xem có phải là file hay hình ảnh
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);

            MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", filepath1, requestBody1);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", filepath2, requestBody2);
            DataService dataService = APIService.getService();
            Call<String> callback = dataService.upLoadHinhLoaiSP(body1,body2);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if (kq.equals("FAIL")) {
                        Toast.makeText(AdminThemDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                    } else {
                        String []s = kq.split(",");
                        String sicon = s[0].trim();
                        String shinh = s[1].trim();
                        DataService dataService1 = APIService.getService();
                        Call<String>call1 = dataService1.themLoaiSP(edTenDM.getText().toString().trim(),sicon,shinh);
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kqthem = response.body();
                                if(kqthem.equals("OK")){
                                    Toast.makeText(AdminThemDanhMucActivity.this, "Thêm thành công danh mục: "+edTenDM.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(AdminThemDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == icon_code) {
                Uri uri = data.getData();
                realpath_icon = getRealPathFromURI(uri);
                Picasso.with(getApplicationContext()).load(uri).into(imgIcon);
            }
            if(requestCode == hinh_code){
                Uri uri = data.getData();
                realpath_hinh = getRealPathFromURI(uri);
                Picasso.with(getApplicationContext()).load(uri).into(imgHinh);
            }
        }
    }

    public void requestPermissionIcon(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, permission_code_icon);

            } else {
                openGalleryIcon();
            }
        }
    }
    public void requestPermissionHinh(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, permission_code_hinh);

            } else {
                openGalleryHinh();
            }
        }
    }


    public void openGalleryIcon(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, icon_code);
    }
    public void openGalleryHinh(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, hinh_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(requestCode == permission_code_icon){
                openGalleryIcon();
            }else if(requestCode == permission_code_hinh){
                openGalleryHinh();
            }
        } else {
            Toast.makeText(this, "Từ chối cấp quyền...", Toast.LENGTH_SHORT).show();
        }
    }



    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        edTenDM = findViewById(R.id.edTenDM);
        imgIcon = findViewById(R.id.imgIcon);
        imgHinh = findViewById(R.id.imgHinh);
        btnThem = findViewById(R.id.btnThem);

        progressDialog = new ProgressDialog(AdminThemDanhMucActivity.this);
        progressDialog.setMessage("Loading, please wait!");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
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