package ducthuan.com.lamdep.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import ducthuan.com.lamdep.Activity.DangNhapActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_DangNhap extends Fragment {

    View view;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    TextInputLayout txtEmailDangNhap,txtMatKhauDangNhap;
    TextView txtQuenMatKhau;
    Button btnDangNhap;

    CallbackManager callbackManager;
    LoginButton login_button;

    SignInButton sign_in_button;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    public static int RC_SIGN_IN = 111;

    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getContext());
        view = inflater.inflate(R.layout.fragment_dang_nhap, container, false);

        addControls();
        loginFacebook();
        loginGoogle();
        addEvents();

        return view;
    }

    private void addControls() {
        txtEmailDangNhap = view.findViewById(R.id.txtEmailDangNhap);
        txtMatKhauDangNhap = view.findViewById(R.id.txtMatKhauDangNhap);
        txtQuenMatKhau = view.findViewById(R.id.txtQuenMatKhau);
        btnDangNhap = view.findViewById(R.id.btnDangNhap);
        login_button = view.findViewById(R.id.login_button);
        sign_in_button = view.findViewById(R.id.sign_in_button);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please wait!");
    }


    private void loginFacebook() {
        callbackManager = CallbackManager.Factory.create();
        login_button.setFragment(this);
        login_button.setReadPermissions("email", "public_profile");
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("sss","Login Success "+ loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("sss","Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("sss","Login Error "+ error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("sss",token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String uid = firebaseUser.getUid().trim();
                            String namegg = firebaseUser.getDisplayName().trim();
                            String emailgg = firebaseUser.getEmail().trim();
                            Log.d("namegg",emailgg);

                            xuLyLuuDuLieuLoginFB_GG(uid,namegg,emailgg);
                        }else{
                            Log.d("errorFB",task.toString());
                        }
                        progressDialog.dismiss();
                    }
                });
    }




    private void loginGoogle() {
        sign_in_button.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(sign_in_button, "Kết nối với Google");

        //Yêu cầu người dùng cung cấp thông tin cơ bản, tên, email, hình ảnh
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //két nối google API client
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

        //kiểm tra kết nối
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:

                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                        break;
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String uid = firebaseUser.getUid().trim();
                            String namegg = firebaseUser.getDisplayName().trim();
                            String emailgg = firebaseUser.getEmail().trim();
                            Log.d("namegg",emailgg);

                            xuLyLuuDuLieuLoginFB_GG(uid,namegg,emailgg);

                        }else{
                            Toast.makeText(getContext(), "Đăng nhập thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void xuLyLuuDuLieuLoginFB_GG(String uid, final String namegg, final String emailgg) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("id", uid);
        map.put("name", namegg);
        map.put("email", emailgg);
        map.put("status", "offline");
        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.dangNhapFBGG(namegg, emailgg);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq = response.body();
                            if (kq.equals("FAIL")) {
                                Toast.makeText(getContext(), "Đăng nhập thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            }else if(kq.equals("KHOA")){
                                Toast.makeText(getContext(), "Tài khoản của bạn đã bị khóa", Toast.LENGTH_SHORT).show();
                            }else{
                                String[] nv = kq.split(",");
                                String manv = nv[0].trim();
                                String tennv = nv[1].trim();
                                sharedPreferences = getActivity().getSharedPreferences("dangnhap", getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("tennv", tennv);
                                editor.putString("manv",manv);
                                editor.commit();
                                Toast.makeText(getActivity(), "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(),TrangChuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (Exception e){

            }
        }
    }


    //custom button google
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setPadding(0,0,30,0);
                return;
            }
        }
    }


    private void addEvents() {

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!kiemTraEmail() | !kiemTraPassword()){
                    Toast.makeText(getActivity(), "Vui lòng kiểm tra lại email hoặc mật khẩu !", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = txtEmailDangNhap.getEditText().getText().toString().trim();
                final String matkhau = txtMatKhauDangNhap.getEditText().getText().toString().trim();

                progressDialog.show();

                auth.signInWithEmailAndPassword(email,matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if(firebaseUser.isEmailVerified()){
                                kiemTraDangNhap(email);
                            }else{
                                Toast.makeText(getContext(), "Tài khoản của bạn chưa được xác minh", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getContext(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });


            }
        });

        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(R.layout.custom_dialog_datlaimatkhau);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText edEmail = alertDialog.findViewById(R.id.edEmail);
                Button btnTiepTheo = alertDialog.findViewById(R.id.btnTiepTheo);

                btnTiepTheo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String email = edEmail.getText().toString().trim();
                        int kt = kiemTraEmailTT(email);
                        if(kt == 0){
                            Toast.makeText(getContext(), "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                        }else if(kt == 2){
                            Toast.makeText(getContext(), "Email sai định dạng", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Mã xác minh đã được gửi đến Email của bạn.\nVui lòng xác minh.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getContext(), "Email không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    private void kiemTraDangNhap(String email) {

        DataService dataService = APIService.getService();
        Call<String>callback = dataService.kiemTraDangNhap(email);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("KHOA")){
                    Toast.makeText(getContext(), "Tài khoản của bạn đã bị khóa!", Toast.LENGTH_SHORT).show();
                }else {
                    String[] nv = kq.split(",");
                    String manv = nv[0];
                    String tennv = nv[1];
                    sharedPreferences = getActivity().getSharedPreferences("dangnhap", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tennv", tennv);
                    editor.putString("manv",manv);
                    editor.commit();
                    Toast.makeText(getActivity(), "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(),TrangChuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private boolean kiemTraEmail(){
        String email = txtEmailDangNhap.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            txtEmailDangNhap.setError("Email không được bỏ trống");
            return false;
            //Kiểm tra định dạng email
        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){//\
            txtEmailDangNhap.setError(null);
            txtEmailDangNhap.setErrorEnabled(false);
            return true;
        } else {
            //txtEmailDangNhap.setError("Email sai định dạng");
            return false;
        }
    }

    private int kiemTraEmailTT(String email){
        if(email.isEmpty()){
            return 0;
        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return 1;
        } else {
            return 2;
        }
    }

    private boolean kiemTraPassword(){
        String password = txtMatKhauDangNhap.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            txtMatKhauDangNhap.setError("Password không được bỏ trống");
            return false;
        }else if(password.matches("((?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,20})")) {
            txtMatKhauDangNhap.setError(null);
            txtMatKhauDangNhap.setErrorEnabled(false);
            return true;

        } else {
            //txtMatKhauDangNhap.setError("Mật khẩu chứa ít nhất 6 ký tự và 1 chữ hoa");
            return false;
        }
    }

}
