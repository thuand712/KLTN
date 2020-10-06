package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.HienThiSanPhamTheoDanhMucActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.LoaiSanPham;
import ducthuan.com.lamdep.R;

public class LoaiSanPhamAdapter extends RecyclerView.Adapter<LoaiSanPhamAdapter.ViewHolder>{

    Context context;
    ArrayList<LoaiSanPham>loaiSanPhams;

    public LoaiSanPhamAdapter(Context context, ArrayList<LoaiSanPham> loaiSanPhams) {
        this.context = context;
        this.loaiSanPhams = loaiSanPhams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_loaisanpham, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSanPham loaiSanPham = loaiSanPhams.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+loaiSanPham.getHINHICON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhLoaiSanPham);
        holder.txtTenLoaiSanPham.setText(loaiSanPham.getTENLOAISP());
    }

    @Override
    public int getItemCount() {
        return loaiSanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgHinhLoaiSanPham;
        TextView txtTenLoaiSanPham;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenLoaiSanPham = itemView.findViewById(R.id.txtTenLoaiSanPham);
            imgHinhLoaiSanPham = itemView.findViewById(R.id.imgHinhLoaiSanPham);
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
