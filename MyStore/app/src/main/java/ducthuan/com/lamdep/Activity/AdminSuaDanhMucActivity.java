package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSuaDanhMucActivity extends AppCompatActivity {
    public static final int icon_code = 001;
    public static final int hinh_code = 002;
    public static final int permission_code_icon = 003;
    public static final int permission_code_hinh = 004;
    String realpath_icon = "";
    String realpath_hinh = "";

    Toolbar toolbar;
    Intent intent;
    LoaiSanPham loaiSanPham;

    EditText edTenDM;
    ImageView imgIcon,imgHinh;
    Button btnXoa;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sua_danh_muc);
        intent = getIntent();
        if(intent.hasExtra("loaisp")){
            loaiSanPham = intent.getParcelableExtra("loaisp");
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
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSuaDanhMucActivity.this);
                builder.setMessage(Html.fromHtml("Việc xóa danh mục <b>"+loaiSanPham.getTENLOAISP()+"</b> sẽ xóa tất cả sản phẩm trong doanh mục này." +
                        "<br>Bạn chắc chắn muốn xóa danh mục <b>"+loaiSanPham.getTENLOAISP()+"</b> không?"));
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                        DataService dataService = APIService.getService();
                        Call<String> call = dataService.xoaLoaiSP(loaiSanPham.getMALOAISP(),loaiSanPham.getHINHICON(),loaiSanPham.getHINHANH());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(AdminSuaDanhMucActivity.this, "Xóa thành công danh mục: "+loaiSanPham.getTENLOAISP(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(AdminSuaDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
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

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        edTenDM = findViewById(R.id.edTenDM);
        imgIcon = findViewById(R.id.imgIcon);
        imgHinh = findViewById(R.id.imgHinh);
        btnXoa = findViewById(R.id.btnXoa);

        progressDialog = new ProgressDialog(AdminSuaDanhMucActivity.this);
        progressDialog.setMessage("Loading, please wait!");

        edTenDM.setText(loaiSanPham.getTENLOAISP());

        Picasso.with(AdminSuaDanhMucActivity.this).load(TrangChuActivity.base_url+loaiSanPham.getHINHICON())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgIcon);
        Picasso.with(AdminSuaDanhMucActivity.this).load(TrangChuActivity.base_url+loaiSanPham.getHINHANH())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinh);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        getSupportActionBar().setTitle(loaiSanPham.getTENLOAISP());

    }

    private void updateLoaiSP() {

        Bitmap bm1=((BitmapDrawable)imgIcon.getDrawable()).getBitmap();
        Uri tempUriIcon = getImageUri(AdminSuaDanhMucActivity.this, bm1);
        realpath_icon = getRealPathFromURI(tempUriIcon);

        Bitmap bm2=((BitmapDrawable)imgHinh.getDrawable()).getBitmap();
        Uri tempUriHinh = getImageUri(AdminSuaDanhMucActivity.this, bm2);
        realpath_hinh = getRealPathFromURI(tempUriHinh);

        if(realpath_icon.equals("") && realpath_hinh.equals("") && edTenDM.getText().toString().trim().equals("")){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            String icon = loaiSanPham.getHINHICON();
            String hinh = loaiSanPham.getHINHANH();
            DataService dataService = APIService.getService();
            Call<String>call = dataService.xoaHinhLoaiSP(icon,hinh);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if(kq.equals("OK")){
                        xuLyUpdate();
                    }else {
                        Toast.makeText(AdminSuaDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void xuLyUpdate() {

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
                    Toast.makeText(AdminSuaDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                } else {
                    String []s = kq.split(",");
                    String sicon = s[0].trim();
                    String shinh = s[1].trim();
                    DataService dataService1 = APIService.getService();
                    Call<String>call1 = dataService1.suaLoaiSP(loaiSanPham.getMALOAISP(),edTenDM.getText().toString().trim(),sicon,shinh);
                    call1.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kqthem = response.body();
                            if(kqthem.equals("OK")){
                                Toast.makeText(AdminSuaDanhMucActivity.this, "Sửa thành công danh mục: "+edTenDM.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(AdminSuaDanhMucActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            if(requestCode == icon_code) {
                Uri uri = data.getData();
                //realpath_icon = getRealPathFromURI(uri);
                Picasso.with(getApplicationContext()).load(uri).into(imgIcon);
            }
            if(requestCode == hinh_code){
                Uri uri = data.getData();
                //realpath_hinh = getRealPathFromURI(uri);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itUpdate: updateLoaiSP(); break;
        }
        return super.onOptionsItemSelected(item);
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