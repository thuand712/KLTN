package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ThongTinTaiKhoanActivity extends AppCompatActivity {

    private static final int PERMISSTION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Toolbar toolbar;
    CircleImageView imgHinh;
    EditText edTen, edNgaySinh, edSodt,edDiaChi;
    TextView txtEmail;
    RadioGroup rgGioiTinh;
    Button btnLuuThayDoi;
    ArrayList<NhanVien> nhanViens;
    RadioButton rdNam, rdNu;

    ImageButton imgThemAnh;

    String realpath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);

        Intent intent = getIntent();
        if (intent.hasExtra("nhanvien")) {
            nhanViens = intent.getParcelableArrayListExtra("nhanvien");
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


        edNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int nam = calendar.get(Calendar.YEAR);
                int thang = calendar.get(Calendar.MONTH);
                int ngay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongTinTaiKhoanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        edNgaySinh.setText(dateFormat.format(calendar.getTime()));
                    }
                }, nam, thang, ngay);
                datePickerDialog.show();
            }
        });

        btnLuuThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bm2=((BitmapDrawable)imgHinh.getDrawable()).getBitmap();
                Uri tempUriHinh = getImageUri(ThongTinTaiKhoanActivity.this, bm2);
                realpath = getRealPathFromURI(tempUriHinh);
                if (realpath.equals("")) {
                    Toast.makeText(ThongTinTaiKhoanActivity.this, "Vui lòng chọn hình đại diện !", Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(realpath);
                    //lấy ra đường dẫn file này
                    String filepath = file.getAbsolutePath();
                    String[] mangtenfile = filepath.split("\\.");
                    filepath = mangtenfile[0] + System.currentTimeMillis() + "." + mangtenfile[1];
                    //lấy ra kiểm tra lại xem có phải là file hay hình ảnh
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", filepath, requestBody);
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.upLoadHinhAnh(body);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq = response.body();
                            if (kq.length() > 0) {
                                String manv = nhanViens.get(0).getMANV().trim();
                                String hoten = edTen.getText().toString().trim();
                                String sodt = edSodt.getText().toString().trim();
                                String ngaysinh = edNgaySinh.getText().toString().trim();
                                String diachi = edDiaChi.getText().toString().trim();
                                int vitri = rgGioiTinh.getCheckedRadioButtonId();
                                RadioButton radioButton = findViewById(vitri);
                                String gioitinh = radioButton.getText().toString();
                                String hinh = "hinhanh/hinhnhanvien/"+kq;
                                if(hoten.equals("") || sodt.equals("") || ngaysinh.equals("") || gioitinh.equals("") || diachi.equals("")){
                                    Toast.makeText(ThongTinTaiKhoanActivity.this, "Vui lòng điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                                }else {
                                    DataService dataService1 = APIService.getService();
                                    Call<String>call1 = dataService1.capNhapThongTinTaiKhoan(manv,hoten,sodt,ngaysinh,gioitinh,hinh,diachi);
                                    call1.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq = response.body();
                                            Log.d("kiemtra",kq);
                                            if(kq.equals("OK") || kq.equals("OK1")){
                                                Toast.makeText(ThongTinTaiKhoanActivity.this, "Cập nhập thông tin tài khoản thành công !", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ThongTinTaiKhoanActivity.this, "Cập nhập thông tin tài khoản thất bại !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(ThongTinTaiKhoanActivity.this, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                }
            }
        });

        imgThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSTION_CODE);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinTaiKhoanActivity.this);
                        builder.setTitle("Add product from");
                        builder.setView(R.layout.custom_dialog_themhinhanh);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        TextView txtMayAnh = dialog.findViewById(R.id.txtMayAnh);
                        TextView txtHinhAnh = dialog.findViewById(R.id.txtHinhAnh);
                        txtMayAnh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openCamera();
                                dialog.dismiss();

                            }
                        });

                        txtHinhAnh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openGallery();
                                dialog.dismiss();
                            }
                        });

                    }
                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Picasso.with(getApplicationContext()).load(uri).into(imgHinh);
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(photo);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 001);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSTION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinTaiKhoanActivity.this);
                    builder.setTitle("Add product from");
                    builder.setView(R.layout.custom_dialog_themhinhanh);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    TextView txtMayAnh = dialog.findViewById(R.id.txtMayAnh);
                    TextView txtHinhAnh = dialog.findViewById(R.id.txtHinhAnh);
                    txtMayAnh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openCamera();
                            dialog.dismiss();

                        }
                    });

                    txtHinhAnh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openGallery();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(this, "Từ chối cấp quyền...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgHinh = findViewById(R.id.imgHinh);
        edTen = findViewById(R.id.edTen);
        edNgaySinh = findViewById(R.id.edNgaySinh);
        edDiaChi = findViewById(R.id.edDiaChi);
        txtEmail = findViewById(R.id.txtEmail);
        edSodt = findViewById(R.id.edSodt);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        btnLuuThayDoi = findViewById(R.id.btnLuuThayDoi);
        rdNam = findViewById(R.id.rdNam);
        rdNu = findViewById(R.id.rdNu);
        imgThemAnh = findViewById(R.id.imgThemAnh);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        edTen.setText(nhanViens.get(0).getTENNV());
        txtEmail.setText(nhanViens.get(0).getTENDANGNHAP());

        if (nhanViens.get(0).getNGAYSINH() != null) {
            edNgaySinh.setText(nhanViens.get(0).getNGAYSINH().toString());
        }
        if (nhanViens.get(0).getSODT() != null) {
            edSodt.setText(nhanViens.get(0).getSODT().toString());
        }

        if(nhanViens.get(0).getDIACHI()!=null){
            edDiaChi.setText(nhanViens.get(0).getDIACHI());
        }


        if (nhanViens.get(0).getHINH() != null) {
            String hinh = nhanViens.get(0).getHINH().toString();
            Picasso.with(ThongTinTaiKhoanActivity.this).load(TrangChuActivity.base_url+hinh).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinh);
        }

        if (nhanViens.get(0).getGIOITINH() != null) {
            if (nhanViens.get(0).getGIOITINH().toString().trim().equals("Nam")) {
                rdNam.setChecked(true);
            } else {
                rdNu.setChecked(true);
            }
        }else {
            rdNam.setChecked(true);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /*public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 500, 500,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }*/

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
