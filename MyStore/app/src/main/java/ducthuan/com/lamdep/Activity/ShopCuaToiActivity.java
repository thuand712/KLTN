package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.ShopCuaToiAdapter;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCuaToiActivity extends AppCompatActivity {

    ArrayList<NhanVien> nhanViens;
    public static ArrayList<SanPham>sanPhams;
    public static ShopCuaToiAdapter shopCuaToiAdapter;
    Toolbar toolbar;
    TextView txtTenNV, txtEmailNV, txtNgayDangKy,txtDoanhThu;
    ImageView imgHinhNV;

    TextView txtQuanLyDonHang,txtDangSanPhamMoi,txtXemShopCuaToi,txtSoLuongSP,txtXemTatCaSPNext;

    RecyclerView rvSanPham;
    TextView txtXemTatCaSP;

    private static final int PERMISSTION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    boolean onpause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cua_toi);
        Intent intent = getIntent();
        if (intent.hasExtra("nhanvien")) {
            nhanViens = intent.getParcelableArrayListExtra("nhanvien");
            addControls();
            getSanPhamTheoShop();
            addEvents();

        }
    }


    private void getSanPhamTheoShop() {

        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.getSanPhamTheoShop(nhanViens.get(0).getMANV(),0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhams = (ArrayList<SanPham>) response.body();
                if(sanPhams.size()>0){
                    txtSoLuongSP.setText(sanPhams.size()+" sản phẩm");
                    shopCuaToiAdapter = new ShopCuaToiAdapter(ShopCuaToiActivity.this,sanPhams);
                    rvSanPham.setLayoutManager(new GridLayoutManager(ShopCuaToiActivity.this, 2));
                    rvSanPham.setHasFixedSize(true);
                    rvSanPham.setNestedScrollingEnabled(true);
                    rvSanPham.setAdapter(shopCuaToiAdapter);
                    shopCuaToiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {

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
        txtDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopCuaToiActivity.this, DoanhThuActivity.class));
            }
        });

        txtXemTatCaSPNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopCuaToiActivity.this,DanhSachSanPhamShopCuaToiActivity.class));
            }
        });

        txtXemTatCaSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopCuaToiActivity.this,DanhSachSanPhamShopCuaToiActivity.class));
            }
        });

        txtQuanLyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopCuaToiActivity.this, QuanLyDonHangShopActivity.class));
            }
        });

        txtXemShopCuaToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopCuaToiActivity.this,ShopActivity.class);
                intent.putExtra("tennv",nhanViens.get(0).getTENDANGNHAP());
                startActivity(intent);
            }
        });



        txtDangSanPhamMoi.setOnClickListener(new View.OnClickListener() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShopCuaToiActivity.this);
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
            Intent intent = new Intent(ShopCuaToiActivity.this,ThemSanPhamShopCuaToiActivity.class);
            intent.putExtra("uri", uri.toString());
            startActivity(intent);
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri uri = getImageUri(getApplicationContext(), photo);
            Intent intent = new Intent(ShopCuaToiActivity.this,ThemSanPhamShopCuaToiActivity.class);
            intent.putExtra("uri", uri.toString());
            startActivity(intent);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShopCuaToiActivity.this);
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
        txtTenNV = findViewById(R.id.txtTenNV);
        txtEmailNV = findViewById(R.id.txtEmailNV);
        txtNgayDangKy = findViewById(R.id.txtNgayDangKy);
        imgHinhNV = findViewById(R.id.imgHinhNV);
        txtXemTatCaSP = findViewById(R.id.txtXemTatCaSP);
        txtSoLuongSP = findViewById(R.id.txtSoLuongSP);
        rvSanPham = findViewById(R.id.rvSanPham);
        txtXemTatCaSPNext = findViewById(R.id.txtXemTatCaSPNext);
        txtQuanLyDonHang = findViewById(R.id.txtQuanLyDonHang);
        txtXemShopCuaToi = findViewById(R.id.txtXemShopCuaToi);
        txtDangSanPhamMoi = findViewById(R.id.txtDangSanPhamMoi);
        txtDoanhThu = findViewById(R.id.txtDoanhThu);

        txtTenNV.setText(nhanViens.get(0).getTENNV());
        txtEmailNV.setText(nhanViens.get(0).getTENDANGNHAP());
        txtNgayDangKy.setText("Thành viên từ " + nhanViens.get(0).getNGAYDANGKY());
        if(nhanViens.get(0).getHINH()!=null){
            String hinh = nhanViens.get(0).getHINH().toString();
            Picasso.with(ShopCuaToiActivity.this).load(TrangChuActivity.base_url+hinh).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhNV);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onpause==true){
            getSanPhamTheoShop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpause = true;
    }
}
