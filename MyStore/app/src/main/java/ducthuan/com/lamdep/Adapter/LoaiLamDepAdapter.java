package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.DanhSachBaiVietLamDepActivity;
import ducthuan.com.lamdep.Activity.LoaiTapLuyenActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.LoaiLamDep;
import ducthuan.com.lamdep.R;

public class LoaiLamDepAdapter extends RecyclerView.Adapter<LoaiLamDepAdapter.ViewHolder> {

    Context context;
    ArrayList<LoaiLamDep>loaiLamDeps;

    public LoaiLamDepAdapter(Context context, ArrayList<LoaiLamDep> loaiLamDeps) {
        this.context = context;
        this.loaiLamDeps = loaiLamDeps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_loai_lam_dep, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiLamDep loaiLamDep = loaiLamDeps.get(position);
        holder.txtTenLoaiLamDep.setText(loaiLamDep.getTIEUDELOAILAMDEP());
        Picasso.with(context).load(TrangChuActivity.base_url+loaiLamDep.getHINHLOAILAMDEP()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgLoaiLamDep);
    }

    @Override
    public int getItemCount() {
        return loaiLamDeps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLoaiLamDep;
        TextView txtTenLoaiLamDep;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLoaiLamDep = itemView.findViewById(R.id.imgLoaiLamDep);
            txtTenLoaiLamDep = itemView.findViewById(R.id.txtTenLoaiLamDep);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if(!loaiLamDeps.get(getPosition()).getMALAMDEP().equals("6")){

                        intent = new Intent(context, DanhSachBaiVietLamDepActivity.class);
                        intent.putExtra("loailamdep",loaiLamDeps.get(getPosition()));
                        context.startActivity(intent);
                    }else {
                        intent = new Intent(context, LoaiTapLuyenActivity.class);
                        intent.putExtra("loailamdep",loaiLamDeps.get(getPosition()));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
