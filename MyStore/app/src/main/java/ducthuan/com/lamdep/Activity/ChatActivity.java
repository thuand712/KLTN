package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Adapter.ChatAdapter;
import ducthuan.com.lamdep.Adapter.ChatNhanhAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.ChatNhanh;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.Notifications.Data;
import ducthuan.com.lamdep.Notifications.MyResponse;
import ducthuan.com.lamdep.Notifications.Sender;
import ducthuan.com.lamdep.Notifications.Token;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ChatActivity extends AppCompatActivity {

    ImageView imgBack;
    CircleImageView imgShop,imgOnline,imgOffline;
    TextView txtShop;
    ImageButton btnSend;
    EditText edMessage;
    RecyclerView rvMessage,rvChatNhanh;

    ChatAdapter chatAdapter;
    ArrayList<Chat>chats;

    //Chat box
    ArrayList<ChatNhanh>chatNhanhs;
    ChatNhanhAdapter chatNhanhAdapter;

    Intent intent;
    String userid = "";
    boolean notify = false;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    //khai bao de khi thoat man hinh chat se huy trang thai
    ValueEventListener seenEventMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        if(intent.hasExtra("userid")){
            userid = intent.getStringExtra("userid");

            addControls();
            seenMessage();
            addEvents();
        }
    }


    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = edMessage.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }
                edMessage.setText("");
            }
        });

    }

    private void sendMessage(String uid, final String userid, String msg) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Map<String,Object> map = new HashMap<>();
        map.put("sender",uid);
        map.put("receiver",userid);
        map.put("message",msg);
        map.put("seen",false);
        databaseReference.push().setValue(map);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(firebaseUser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef1.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final String msg1 = msg;
//
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(userid, user.getName(), msg1);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // goi class token
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username+": "+message, "Có tin nhắn mới!!",
                            userid);
                    // goi class sender
                    Sender sender = new Sender(data, token.getToken());
                    // goi sendNotification trong token dung CallBack retrofit xem dong bo ko
                    DataService dataService = APIService.getFCMService();
                    dataService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(ChatActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        imgShop = findViewById(R.id.imgShop);
        txtShop = findViewById(R.id.txtShop);
        btnSend = findViewById(R.id.btnSend);
        edMessage = findViewById(R.id.edMessage);
        rvMessage = findViewById(R.id.rvMessage);
        rvChatNhanh = findViewById(R.id.rvChatNhanh);
        imgOnline = findViewById(R.id.imgOnline);
        imgOffline = findViewById(R.id.imgOffline);

        rvMessage.setHasFixedSize(true);



        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);

        rvMessage.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getStatus().equals("online")){
                    imgOnline.setVisibility(View.VISIBLE);
                    imgOffline.setVisibility(View.GONE);
                }else {
                    imgOnline.setVisibility(View.GONE);
                    imgOffline.setVisibility(View.VISIBLE);
                }

                readMessage(firebaseUser.getUid(),userid,"default");
                getHinhNV(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatNhanhs = new ArrayList<>();
        chatNhanhs.add(new ChatNhanh("Xin chào!"));
        chatNhanhs.add(new ChatNhanh("Món này còn không shop?"));
        chatNhanhs.add(new ChatNhanh("Cảm ơn bạn nhiều nhé!"));
        chatNhanhs.add(new ChatNhanh("Không có gì nhé bạn!"));
        chatNhanhAdapter = new ChatNhanhAdapter(chatNhanhs,ChatActivity.this,userid);
        rvChatNhanh.setLayoutManager(new LinearLayoutManager(ChatActivity.this,RecyclerView.HORIZONTAL,false));
        rvChatNhanh.setNestedScrollingEnabled(true);
        rvChatNhanh.setHasFixedSize(true);
        rvChatNhanh.setAdapter(chatNhanhAdapter);

    }

    private void getHinhNV(String email) {
        DataService dataService = APIService.getService();
        Call<List<NhanVien>>call = dataService.getNhanVien(email);
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                List<NhanVien>nhanViens = response.body();
                if(nhanViens.get(0).getHINH()!=null){
                    Picasso.with(ChatActivity.this).load(TrangChuActivity.base_url+nhanViens.get(0).getHINH())
                            .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgShop);
                }
                txtShop.setText(nhanViens.get(0).getTENNV());
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }

    private void readMessage(final String uid, final String userid, final String image) {

        chats = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getSender().equals(uid) && chat.getReceiver().equals(userid) ||
                            chat.getReceiver().equals(uid) && chat.getSender().equals(userid)){
                        chats.add(chat);
                    }
                }

                chatAdapter = new ChatAdapter(ChatActivity.this,chats,image);
                rvMessage.setAdapter(chatAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void seenMessage() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenEventMessage = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        Map<String, Object>map = new HashMap<>();
                        map.put("seen",true);
                        dataSnapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("notshow",MODE_PRIVATE).edit();
        editor.putString("cu",userid);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenEventMessage);
        currentUser("none");
    }
}
