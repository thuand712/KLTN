package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ducthuan.com.lamdep.Model.ChatNhanh;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.R;

public class ChatNhanhAdapter extends RecyclerView.Adapter<ChatNhanhAdapter.ViewHolder>{

    ArrayList<ChatNhanh>chatNhanhs;
    Context context;
    String userid;

    public ChatNhanhAdapter(ArrayList<ChatNhanh> chatNhanhs, Context context,String userid) {
        this.chatNhanhs = chatNhanhs;
        this.context = context;
        this.userid = userid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_chatnhanh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ChatNhanh chatNhanh = chatNhanhs.get(position);
        holder.txtChatNhanh.setText(chatNhanh.getNoidung());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatNhanh.getNoidung();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String idcurrent = firebaseUser.getUid();
                sendMessage(idcurrent,userid,message,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatNhanhs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtChatNhanh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChatNhanh = itemView.findViewById(R.id.txtChatNhanh);
        }
    }

    private void sendMessage(final String uid, final String userid, String msg, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Map<String,Object> map = new HashMap<>();
        map.put("sender",uid);
        map.put("receiver",userid);
        map.put("message",msg);
        map.put("seen",true);

        databaseReference.push().setValue(map);

        Map<String,Object> maprep = new HashMap<>();
        maprep.put("sender",userid);
        maprep.put("receiver",uid);
        switch (position){
            case 0: maprep.put("message","Chào bạn!"); break;
            case 1: maprep.put("message","Món này còn nhiều nhé bạn!"); break;
            case 2: maprep.put("message","Không có gì nhé bạn!"); break;
            case 3: maprep.put("message","Ok bạn"); break;
        }
        maprep.put("seen",false);

        databaseReference.push().setValue(maprep);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(uid).child(userid);
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

        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(uid);
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef1.child("id").setValue(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
