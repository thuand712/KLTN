package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ducthuan.com.lamdep.Fragment.Fragment_Tab_DanhMuc;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TaiKhoan;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_BaiViet;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TinNhan;
import ducthuan.com.lamdep.Fragment.Fragment_Tab_TrangChu;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.R;

public class  TrangChuActivity extends AppCompatActivity {

    public static final String base_url = "https://webt2.000webhostapp.com/webt2/";
    //public static final String base_url = "http://172.17.26.228/webt2/";
    //public static final String base_url = "http://192.168.43.56/webt2/";

    FrameLayout frameLayout_Content;
    BottomNavigationView bottomNav;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    ArrayList<Chat>listChat;

    TextView txtSL;

    MenuItem item1,item2,item3,item4,item5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
        setContentView(R.layout.activity_trang_chu);

        addControls();
        getCountMessage();
        addEvents();
        //getDataSanPhamGoiY();
        /*} else {
            CheckConnect.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra lại kết nối!!!");
        }*/
    }


    private void getCountMessage() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        View view = menuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
        View slmsg = LayoutInflater.from(this).inflate(R.layout.custom_circle_tinnhan,itemView,false);
        itemView.addView(slmsg);
        txtSL = view.findViewById(R.id.txtSL);
        listChat = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Query query = FirebaseDatabase.getInstance().getReference("Chats")
                    .orderByChild("receiver").equalTo(firebaseUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int sl = 0;
                    listChat.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        listChat.add(chat);
                    }

                    for (int i = 0; i < listChat.size(); i++) {
                        if(!listChat.get(i).isSeen()){
                            int dem = 0;
                            for (int j = 0; j < i; j++) {
                                if(!listChat.get(j).isSeen()){
                                    if(listChat.get(i).getSender().equals(listChat.get(j).getSender())){
                                        dem = 1;
                                        break;
                                    }
                                }
                            }
                            if(dem==0){
                                sl++;
                            }
                        }
                    }

                    if(sl > 0){
                        txtSL.setVisibility(View.VISIBLE);
                        txtSL.setText(sl+"");
                    }else {
                        txtSL.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    //add cac controls
    private void addControls() {
        frameLayout_Content = findViewById(R.id.frameLayout_Content);
        bottomNav = findViewById(R.id.bottomNav);

        item1 = (MenuItem) bottomNav.getMenu().getItem(0);
        item2 = (MenuItem) bottomNav.getMenu().getItem(1);
        item3 = (MenuItem) bottomNav.getMenu().getItem(2);
        item5 = (MenuItem) bottomNav.getMenu().getItem(4);
        item1.setIcon(R.drawable.ic_home_active);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Content,new Fragment_Tab_TrangChu()).commit();

    }

    private void addEvents() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment seletedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.itTrangChu:
                        item1.setIcon(R.drawable.ic_home_active);
                        item2.setIcon(R.drawable.ic_categories_inactive);
                        item5.setIcon(R.drawable.ic_profile_inactive);
                        // change color for icon 0
                        seletedFragment = new Fragment_Tab_TrangChu();
                        break;
                    case R.id.itDanhMuc:
                        item1.setIcon(R.drawable.ic_home_inactive);
                        item2.setIcon(R.drawable.ic_categories_active);
                        item5.setIcon(R.drawable.ic_profile_inactive);
                        seletedFragment = new Fragment_Tab_DanhMuc();
                        break;
                    case R.id.itTimKiem:
                        item1.setIcon(R.drawable.ic_home_inactive);
                        item2.setIcon(R.drawable.ic_categories_inactive);
                        item5.setIcon(R.drawable.ic_profile_inactive);
                        seletedFragment = new Fragment_Tab_TinNhan();
                        break;
                    case R.id.itLamDep:
                        item1.setIcon(R.drawable.ic_home_inactive);
                        item2.setIcon(R.drawable.ic_categories_inactive);
                        item5.setIcon(R.drawable.ic_profile_inactive);
                        seletedFragment = new Fragment_Tab_BaiViet();
                        break;
                    case R.id.itTaiKhoan:
                        item1.setIcon(R.drawable.ic_home_inactive);
                        item2.setIcon(R.drawable.ic_categories_inactive);
                        item5.setIcon(R.drawable.ic_profile_active);
                        seletedFragment = new Fragment_Tab_TaiKhoan();
                        break;
                }

                //thay the fragment content bang fragment chon
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Content,seletedFragment).commit();
                return true;
            }
        });
    }

    public void status(String status){
        if(firebaseUser!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            Map<String,Object> map = new HashMap<>();
            map.put("status",status);
            databaseReference.updateChildren(map);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}
