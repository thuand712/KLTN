package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ducthuan.com.lamdep.Activity.ChatActivity;
import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.DanhGiaCuaToiActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.DonMuaActivity;
import ducthuan.com.lamdep.Activity.AdminActivity;
import ducthuan.com.lamdep.Activity.SanPhamDaXemActivity;
import ducthuan.com.lamdep.Activity.SanPhamYeuThichActivity;
import ducthuan.com.lamdep.Activity.ShopCuaToiActivity;
import ducthuan.com.lamdep.Activity.ThongTinTaiKhoanActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_TaiKhoan extends Fragment {
    View view;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    TextView txtGioHang;
    ArrayList<NhanVien>nhanViens;

    LinearLayout linearLayoutAdmin;
    RelativeLayout relativeLayout;

    boolean onpause = false,clickchat = false;

    LinearLayout linearLayoutDanhChoNguoiBan;

    TextView txtDangNhapDangKy,txtEmailNV,txtNgayDangKy,txtTenNV,txtBatDauBan,txtQuanLyDonHang,txtQuanLyAdmin;
    ImageView imgHinhNV,imgThongTinTaiKhoan,imgChatAdmin;
    int dx = 0, dy = 0;

    TextView txtSPDaXem,txtSPYeuThich,txtDanhGiaCuaToi,txtLienHe,txtHoTro;

    Button btnDangXuat;
    String tennv = "";
    String manv = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_tai_khoan,container,false);
        addControls();
        setHasOptionsMenu(true);
        addEvents();
        return view;
    }

    private void addEvents() {
        txtDangNhapDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DangNhapActivity.class));
            }
        });

        txtQuanLyAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AdminActivity.class));
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tennv.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();

                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    GoogleSignIn.getClient(
                            getContext(),
                            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    ).signOut();

                    startActivity(new Intent(getActivity(), TrangChuActivity.class));
                }

                //sử kiện logout tại đây


            }
        });

        imgThongTinTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThongTinTaiKhoanActivity.class);
                intent.putParcelableArrayListExtra("nhanvien",nhanViens);
                getActivity().startActivity(intent);
            }
        });

        txtQuanLyDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!manv.equals("")){
                    startActivity(new Intent(getActivity(), DonMuaActivity.class));

                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }


            }
        });

        txtBatDauBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShopCuaToiActivity.class);
                intent.putParcelableArrayListExtra("nhanvien",nhanViens);
                getActivity().startActivity(intent);
            }
        });

        txtSPDaXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), SanPhamDaXemActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

        txtSPYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), SanPhamYeuThichActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

        txtDanhGiaCuaToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manv.equals("")){
                    Intent intent = new Intent(getActivity(), DanhGiaCuaToiActivity.class);
                    intent.putExtra("manv",manv);
                    startActivity(intent);


                }else {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
            }
        });

        imgChatAdmin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                long times = motionEvent.getEventTime() - motionEvent.getDownTime();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && times < 100) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("userid","YGU8bO7x85hz9QBzADhUvn8DYQ33");
                    startActivity(intent);
                    return true;
                }
                int X = (int) motionEvent.getRawX();
                int Y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        //clickchat = true;
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        dx = X - params.leftMargin;
                        dy = Y - params.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        /*if (clickchat == true) {
                            view.performClick();
                        }*/
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //clickchat = false;
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        params1.leftMargin = (X - dx);
                        params1.topMargin = (Y - dy);
                        params1.rightMargin = -250;
                        params1.bottomMargin = -250;
                        view.setLayoutParams(params1);
                        break;
                }
                relativeLayout.invalidate();
                return true;
            }
        });
    }

    private void addControls() {
        toolbar = view.findViewById(R.id.toolbar);
        txtDangNhapDangKy = view.findViewById(R.id.txtDangNhapDangKy);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        txtEmailNV = view.findViewById(R.id.txtEmailNV);
        txtNgayDangKy = view.findViewById(R.id.txtNgayDangKy);
        txtTenNV = view.findViewById(R.id.txtTenNV);
        txtBatDauBan = view.findViewById(R.id.txtBatDauBan);
        imgThongTinTaiKhoan = view.findViewById(R.id.imgThongTinTaiKhoan);
        txtQuanLyDonHang = view.findViewById(R.id.txtQuanLyDonHang);
        linearLayoutDanhChoNguoiBan = view.findViewById(R.id.linearLayoutDanhChoNguoiBan);
        txtSPDaXem = view.findViewById(R.id.txtSPDaXem);
        txtSPYeuThich = view.findViewById(R.id.txtSPYeuThich);
        txtDanhGiaCuaToi = view.findViewById(R.id.txtDanhGiaCuaToi);
        imgHinhNV = view.findViewById(R.id.imgHinhNV);
        imgChatAdmin = view.findViewById(R.id.imgChatAdmin);
        linearLayoutAdmin = view.findViewById(R.id.linearLayoutAdmin);
        txtQuanLyAdmin = view.findViewById(R.id.txtQuanLyAdmin);
        relativeLayout = view.findViewById(R.id.relativeLayout);

        //khoi tao img chat admin
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250,250);
        /*layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);*/
        layoutParams.topMargin = 1200;
        layoutParams.leftMargin = 600;
        imgChatAdmin.setLayoutParams(layoutParams);



        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = getActivity().getSharedPreferences("dangnhap",Context.MODE_PRIVATE);
        tennv = sharedPreferences.getString("tennv","");
        manv = sharedPreferences.getString("manv","");
        if(manv.equals("")){
            imgChatAdmin.setVisibility(View.GONE);
        }


        if(!tennv.equals("")){
            imgThongTinTaiKhoan.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
            txtDangNhapDangKy.setVisibility(View.GONE);
            txtEmailNV.setVisibility(View.VISIBLE);
            txtNgayDangKy.setVisibility(View.VISIBLE);
            txtBatDauBan.setVisibility(View.VISIBLE);
            linearLayoutDanhChoNguoiBan.setVisibility(View.VISIBLE);
            //Picasso.with(getContext()).load(firebaseUser.getPhotoUrl()).into(imgHinhNV);
            getNhanVien();
        }

    }

    public void getNhanVien(){
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>callback = dataService.getNhanVien(tennv);
        callback.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                nhanViens = (ArrayList<NhanVien>) response.body();
                if(nhanViens.size()>0){
                    txtTenNV.setText(nhanViens.get(0).getTENNV());
                    txtEmailNV.setText(nhanViens.get(0).getTENDANGNHAP());
                    txtNgayDangKy.setText("Thành viên từ "+nhanViens.get(0).getNGAYDANGKY());
                    if(nhanViens.get(0).getHINH()!=null){
                        String hinh = nhanViens.get(0).getHINH().toString();
                        Picasso.with(getActivity()).load(TrangChuActivity.base_url+hinh).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhNV);
                    }
                    if(nhanViens.get(0).getMALOAINV().equals("1")){
                        linearLayoutAdmin.setVisibility(View.VISIBLE);//hiện lên
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }


    //lay san pham gio hang
    public void getDataGioHang(final TextView txtGH) {

        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        String manv = sharedPreferences.getString("manv", "");
        if (manv.equals("")) {
            txtGH.setVisibility(View.GONE);
        } else {
            DataService dataService = APIService.getService();
            Call<String> callSLSP = dataService.getSoLuongSPGioHang(manv);
            callSLSP.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sl = response.body();
                    if(sl.equals("")){
                        txtGH.setVisibility(View.GONE);
                    }else {
                        txtGH.setVisibility(View.VISIBLE);
                        txtGH.setText(sl);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_trang_chu,menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.itGioHang);
        View giaoDienCustomGioHang = MenuItemCompat.getActionView(item);
        txtGioHang = giaoDienCustomGioHang.findViewById(R.id.txtSoLuongSanPhamGioHang);
        getDataGioHang(txtGioHang);

        giaoDienCustomGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
                String manv= sharedPreferences.getString("manv","");
                if(manv.equals("")){
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }else {
                    Intent intent = new Intent(getActivity(), GioHangActivity.class);
                    intent.putExtra("trangchu",1);
                    startActivity(intent);
                }
            }
        });

    }
    

    @Override
    public void onResume() {
        super.onResume();
        if(onpause == true){
            getDataGioHang(txtGioHang);
            getNhanVien();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
