package ducthuan.com.lamdep.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.TimKiemTrangChuActivity;
import ducthuan.com.lamdep.Adapter.QuangCaoAdapter;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tab_TrangChu extends Fragment {

    View view;
    Toolbar toolbarTrangChu;
    SharedPreferences sharedPreferences;

    TextView txtTimKiem;

    ViewPager viewPagerQuangCao;
    CircleIndicator indicatorQuangCao;
    QuangCaoAdapter quangCaoAdapter;
    ArrayList<QuangCao> quangCaos;

    int[]dsMauQuangCao = {R.color.mauhong,R.color.mauxam,R.color.maudo};

    Runnable runnable;
    Handler handler;

    int currentItem;

    TextView txtGioHang;
    boolean onpause = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_trang_chu,container,false);

        //if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.mau60000000));
        }*/
        addControls();
        getDataQuangCao();
        addEvents();
        //getDataSanPhamGoiY();
        /*} else {
            CheckConnect.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra lại kết nối!!!");
        }*/

        return view;
    }


    private void getDataQuangCao() {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>>callback = dataService.layQuangCaoTheoNgay();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                quangCaos = (ArrayList<QuangCao>) response.body();
                if(quangCaos.size()>0){
                    quangCaoAdapter = new QuangCaoAdapter(getActivity(), quangCaos);
                    viewPagerQuangCao.setAdapter(quangCaoAdapter);
                    indicatorQuangCao.setViewPager(viewPagerQuangCao);

                    //Tu dong chay quang cao
                    handler = new Handler();
                    //thuc hien hanh dong khi handler goi
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            currentItem = viewPagerQuangCao.getCurrentItem();
                            currentItem++;
                            if(currentItem>=viewPagerQuangCao.getAdapter().getCount()){
                                currentItem=0;
                            }
                            viewPagerQuangCao.setCurrentItem(currentItem, true);
                            handler.postDelayed(runnable, 4500);
                        }
                    };
                    handler.postDelayed(runnable, 4500);
                }

            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {

            }
        });
    }


    //add cac controls
    private void addControls() {
        setHasOptionsMenu(true);
        toolbarTrangChu = view.findViewById(R.id.toolbarTrangChu);
        txtTimKiem = view.findViewById(R.id.txtTimKiem);
        viewPagerQuangCao = view.findViewById(R.id.viewPagerQuangCao);
        indicatorQuangCao = view.findViewById(R.id.indicatorQuangCao);


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarTrangChu);

        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);

    }

    private void addEvents() {
        txtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimKiemTrangChuActivity.class);
                intent.putExtra("timkiem","trangchu");
                startActivity(intent);
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

    //khi back ve load lai du lieu o day




   @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       getActivity().getMenuInflater().inflate(R.menu.menu_trang_chu, menu);
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



    /*private void layTenNguoiDungDangNhap() {
        sharedPreferences = getActivity().getSharedPreferences("dangnhap", Context.MODE_PRIVATE);
        tennguoidung = sharedPreferences.getString("tennv", "");

        Log.d("tennv", tennguoidung);
        if (!tennguoidung.equals("")) {
            itdangnhap.setTitle(tennguoidung);
        }
    }

    private void layTenNguoiDungGoogle() {
        //Yêu cầu người dùng cung cấp thông tin cơ bản, tên, email, hình ảnh
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //két nối google API client
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());

        if (googleSignInAccount != null) {
            itdangnhap.setTitle(googleSignInAccount.getDisplayName());
        }
    }

    private void layTenNguoiDungFacebook() {
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null && !accessToken.isExpired()) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        itdangnhap.setTitle(object.getString("name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            /*case R.id.itDangNhap:
                if (accessToken == null && googleSignInAccount == null && tennguoidung.equals("")) {
                    startActivity(new Intent(getActivity(), DangNhapActivity.class));
                }
                break;

            case R.id.itDangXuat:

                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();
                    menu.clear();
                    onCreateOptionsMenu(menu,);
                    *//*AlertDialog.Builder builder = new AlertDialog.Builder(TrangChuActivity.this);
                    builder.setMessage("Bạn có muốn đăng xuất không?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoginManager.getInstance().logOut();
                            menu.clear();
                            onCreateOptionsMenu(menu);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();*//*
                }
                if (googleSignInAccount != null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("tennv", "");
                                    editor.putString("manv", "");
                                    editor.commit();
                                    menu.clear();
                                    onCreateOptionsMenu(menu);
                                }
                            });
                }

                if (!tennguoidung.equals("")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", "");
                    editor.putString("manv", "");
                    editor.commit();
                    menu.clear();
                    onCreateOptionsMenu(menu);
                }
                startActivity(new Intent(getActivity(), TrangChuActivity.class));
                break;
            */
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(onpause==true){
            getDataGioHang(txtGioHang);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onpause = true;
    }
}
