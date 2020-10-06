package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import ducthuan.com.lamdep.Activity.ChatActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context context;
    ArrayList<User>users;
    boolean isChat;
    boolean seen;
    boolean checkLastUser;
    String lastMsg;
    FirebaseUser firebaseUser;

    public UserAdapter(Context context, ArrayList<User> users, boolean isChat, FirebaseUser firebaseUser) {
        this.context = context;
        this.users = users;
        this.isChat = isChat;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raw_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);

        getHinhNV(user.getEmail(),holder.txtUser,holder.imgUser);


        if(user.getStatus().equals("online")){
            holder.imgOnline.setVisibility(View.VISIBLE);
            holder.imgOffline.setVisibility(View.GONE);
        }else {
            holder.imgOnline.setVisibility(View.GONE);
            holder.imgOffline.setVisibility(View.VISIBLE);
        }


        if(isChat){
            lastMessage(user.getId(), holder.txtLastMSG);
        }else {
            holder.txtLastMSG.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });
    }

    private void getHinhNV(String email, final TextView txtUser, final CircleImageView imgUser) {
        DataService dataService = APIService.getService();
        Call<List<NhanVien>> call = dataService.getNhanVien(email);
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                List<NhanVien>nhanViens = response.body();
                if(nhanViens.size()>0){
                    if(nhanViens.get(0).getHINH()!=null){
                        Picasso.with(context).load(TrangChuActivity.base_url+nhanViens.get(0).getHINH())
                                .placeholder(R.drawable.noimage).error(R.drawable.error).into(imgUser);
                    }
                    txtUser.setText(nhanViens.get(0).getTENNV());
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUser,imgOnline,imgOffline;
        TextView txtUser,txtLastMSG;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUser = itemView.findViewById(R.id.txtUser);
            imgOnline = itemView.findViewById(R.id.imgOnline);
            imgOffline = itemView.findViewById(R.id.imgOffline);
            txtLastMSG = itemView.findViewById(R.id.txtLastMSG);
        }
    }

    private void lastMessage(final String id, final TextView txtLastMSG) {
        lastMsg = "default";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(id)
                            || chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(id)){
                        if(chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(id)){
                            checkLastUser = true;
                        }else {
                            checkLastUser = false;
                        }

                        lastMsg = chat.getMessage();
                        seen = chat.isSeen();
                    }
                }

                if(!lastMsg.equals("default")){
                    txtLastMSG.setText(lastMsg);
                    if(seen == false && checkLastUser == false){
                        txtLastMSG.setTextColor(context.getResources().getColor(R.color.colorAccent));

                    }else {
                        txtLastMSG.setTextColor(context.getResources().getColor(R.color.mauxam));
                    }
                }else {
                    txtLastMSG.setText("No message");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
