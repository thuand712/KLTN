package ducthuan.com.lamdep.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_DangKy extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    String email = "", matkhau = "", name = "";

    TextInputLayout txtTenDangKy, txtEmailDangKy, txtMatKhauDangky;
    Button btnDangKy;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dang_ky, container, false);

        AnhXa();
        addEvents();

        return view;
    }

    private void addEvents() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtTenDangKy.getEditText().getText().toString().trim();
                email = txtEmailDangKy.getEditText().getText().toString().trim();
                matkhau = txtMatKhauDangky.getEditText().getText().toString().trim();
                if (!kiemTraEmail() | !kiemTraPassword() | !kiemTraUsername()) {
                    return;
                }

                progressDialog.show();

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        themDuLieu();
                                    }
                                }
                            });
                        }else{
                            txtEmailDangKy.setEnabled(true);
                            txtEmailDangKy.setError("Email đã tồn tại hoặc không đúng định dạng!");
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void themDuLieu() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("id", firebaseAuth.getUid());
        map.put("name", name);
        map.put("email", email);
        map.put("status", "offline");
        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.kiemTraDangKy(name, email);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String check = response.body();
                            if (check.equals("OK")) {
                                Toast.makeText(getContext(), "Đăng ký thành công, vui lòng kiểm tra email của bạn để xác nhận", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), DangNhapActivity.class));
                            }else if(check.equals("FAIL")){
                                Toast.makeText(getContext(), "Đăng ký thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("KTDK",t.toString());
                        }
                    });

                }

            }
        });
    }


    private void AnhXa() {
        txtTenDangKy = view.findViewById(R.id.txtTenDangKy);
        txtEmailDangKy = view.findViewById(R.id.txtEmailDangKy);
        txtMatKhauDangky = view.findViewById(R.id.txtMatKhauDangky);
        btnDangKy = view.findViewById(R.id.btnDangKy);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please");

    }

    private boolean kiemTraEmail(){
        if(email.isEmpty()){
            txtEmailDangKy.setError("Email không được bỏ trống");
            return false;
            //Kiểm tra định dạng email
        }else{
            txtEmailDangKy.setError(null);
            txtEmailDangKy.setErrorEnabled(false);
            return true;
        }

        /*else if(email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){//\
            txtEmailDangKy.setError(null);
            txtEmailDangKy.setErrorEnabled(false);
            return true;
        } else {
            txtEmailDangKy.setError("Email sai định dạng");
            return false;
        }*/
    }


    private boolean kiemTraUsername(){
        if(name.isEmpty()){
            txtTenDangKy.setError("Username không được bỏ trống");
            return false;
        }else if(name.length()>50){
            txtTenDangKy.setError("Tối đa 50 ký tự");
            return false;
        }else {
            txtTenDangKy.setError(null);
            txtTenDangKy.setErrorEnabled(false);
            return true;
        }
    }

    private boolean kiemTraPassword(){
        if(matkhau.isEmpty()){
            txtMatKhauDangky.setError("Password không được bỏ trống");
            return false;
        }else if(matkhau.matches("((?=.*\\d)(?=.*[A-Z ])(?=.*[a-z]).{6,20})")) {
            txtMatKhauDangky.setError(null);
            txtMatKhauDangky.setErrorEnabled(false);
            return true;
        } else {
            txtMatKhauDangky.setError("Mật khẩu chứa ít nhất 6 ký tự và 1 chữ hoa");
            return false;
        }
    }


}
