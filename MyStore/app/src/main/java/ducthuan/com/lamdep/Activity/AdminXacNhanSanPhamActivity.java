package ducthuan.com.lamdep.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Adapter.HinhSanPhamSuaAdapter;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminXacNhanSanPhamActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgAnhBia;

    SanPham sanPham;
    RecyclerView rvHinhNho;
    Button btnDeleteProduct,btnXacNhan;
    TextView txtTenSP,txtMoTaSP,txtGiaSP,txtSoLuongSP,txtKhuyenMai,edMauSac,edKichThuoc;
    TextView txtDanhMuc;

    public static ArrayList<Uri> uriHinhSP;
    public static HinhSanPhamSuaAdapter hinhSanPhamAdapter;
    ArrayList<LoaiSanPham>loaiSanPhams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_xac_nhan_san_pham);
        Intent intent = getIntent();
        if (intent.hasExtra("itemsp")) {
            sanPham = intent.getParcelableExtra("itemsp");
            getLoaiSanPham();
            addControls();
            addEvents();
        }

    }

    private void getLoaiSanPham() {
        DataService dataService = APIService.getService();
        Call<List<LoaiSanPham>>call = dataService.layDanhSachLoaiSanPham();
        call.enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                loaiSanPhams = (ArrayList<LoaiSanPham>) response.body();
                for (int i = 0; i < loaiSanPhams.size(); i++) {
                    if(loaiSanPhams.get(i).getMALOAISP().equals(sanPham.getMALOAISP()))
                    {
                        txtDanhMuc.setText(loaiSanPhams.get(i).getTENLOAISP());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable t) {

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


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminXacNhanSanPhamActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xác nhận sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataService dataService = APIService.getService();
                        Call<String>call = dataService.capNhapTrangThaiSP(sanPham.getMASP());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(AdminXacNhanSanPhamActivity.this, "Cập nhập sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AdminXacNhanSanPhamActivity.this, "Đã xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                }
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


        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminXacNhanSanPhamActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataService dataService = APIService.getService();
                        Call<String>call = dataService.deleteProduct(sanPham.getMASP());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if(kq.equals("OK")){
                                    Toast.makeText(AdminXacNhanSanPhamActivity.this, "Xóa sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(AdminXacNhanSanPhamActivity.this, "Xóa sản phẩm thất bại !", Toast.LENGTH_SHORT).show();
                                }
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

    }


    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        imgAnhBia = findViewById(R.id.imgAnhBia);
        rvHinhNho = findViewById(R.id.rvHinhNho);
        edMauSac = findViewById(R.id.edMauSac);
        edKichThuoc = findViewById(R.id.edKichThuoc);

        txtTenSP = findViewById(R.id.txtTenSP);
        txtMoTaSP = findViewById(R.id.txtMoTaSP);
        txtGiaSP = findViewById(R.id.txtGiaSP);
        txtSoLuongSP = findViewById(R.id.txtSoLuongSP);
        txtKhuyenMai = findViewById(R.id.txtKhuyenMai);
        txtDanhMuc = findViewById(R.id.txtDanhMuc);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        uriHinhSP = new ArrayList<>();

        hinhSanPhamAdapter = new HinhSanPhamSuaAdapter(AdminXacNhanSanPhamActivity.this,uriHinhSP);
        rvHinhNho.setLayoutManager(new LinearLayoutManager(AdminXacNhanSanPhamActivity.this, RecyclerView.HORIZONTAL,false));
        rvHinhNho.setHasFixedSize(true);
        rvHinhNho.setAdapter(hinhSanPhamAdapter);
        hinhSanPhamAdapter.notifyDataSetChanged();

        Picasso.with(AdminXacNhanSanPhamActivity.this).load(TrangChuActivity.base_url+sanPham.getANHLON()).into(imgAnhBia);
        if(!sanPham.getANHNHO().equals("") && sanPham.getANHNHO()!=null){
            String[]anhnhos = sanPham.getANHNHO().split(",");
            for (int i = 0; i < anhnhos.length; i++) {
                uriHinhSP.add(Uri.parse(TrangChuActivity.base_url+anhnhos[i]));
            }
            hinhSanPhamAdapter.notifyDataSetChanged();
        }

        txtTenSP.setText(sanPham.getTENSP());
        txtMoTaSP.setText(sanPham.getTHONGTIN());
        txtGiaSP.setText(sanPham.getGIA());
        txtSoLuongSP.setText(sanPham.getSOLUONG());
        txtKhuyenMai.setText(sanPham.getKHUYENMAI());
        edMauSac.setText(sanPham.getMAUSAC());
        edKichThuoc.setText(sanPham.getKICHTHUOC());

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

    }
}
