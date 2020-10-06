package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.AdminSuaDanhMucActivity;
import ducthuan.com.lamdep.Activity.HienThiSanPhamTheoDanhMucActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoaiSanPhamAdapter extends RecyclerView.Adapter<AdminLoaiSanPhamAdapter.ViewHolder>{

    Context context;
    ArrayList<LoaiSanPham>loaiSanPhams;

    public AdminLoaiSanPhamAdapter(Context context, ArrayList<LoaiSanPham> loaiSanPhams) {
        this.context = context;
        this.loaiSanPhams = loaiSanPhams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_danhmuc_admin, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LoaiSanPham loaiSanPham = loaiSanPhams.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+loaiSanPham.getHINHICON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgLoaiDM);
        holder.txtLoaiDM.setText(loaiSanPham.getTENLOAISP());

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminSuaDanhMucActivity.class);
                intent.putExtra("loaisp",loaiSanPham);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiSanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgLoaiDM,imgSua;
        TextView txtLoaiDM;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLoaiDM = itemView.findViewById(R.id.txtLoaiDM);
            imgLoaiDM = itemView.findViewById(R.id.imgLoaiDM);
            imgSua = itemView.findViewById(R.id.imgSua);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, HienThiSanPhamTheoDanhMucActivity.class);
                    intent.putExtra("itemloaisp", loaiSanPhams.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

}
