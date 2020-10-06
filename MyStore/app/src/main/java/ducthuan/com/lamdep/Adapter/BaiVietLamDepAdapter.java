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

import ducthuan.com.lamdep.Activity.BaiVietLamDepActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.BaiVietLamDep;
import ducthuan.com.lamdep.R;

public class BaiVietLamDepAdapter extends RecyclerView.Adapter<BaiVietLamDepAdapter.ViewHolder>{

    Context context;
    ArrayList<BaiVietLamDep>baiVietLamDeps;

    public BaiVietLamDepAdapter(Context context, ArrayList<BaiVietLamDep> baiVietLamDeps) {
        this.context = context;
        this.baiVietLamDeps = baiVietLamDeps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_bai_viet_lam_dep, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiVietLamDep baiVietLamDep = baiVietLamDeps.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+baiVietLamDep.getHINHBAIVIETLAMDEP())
                .placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhBaiViet);
        holder.txtTieuDeBaiViet.setText(baiVietLamDep.getTIEUDEBAIVIETLAMDEP());
    }

    @Override
    public int getItemCount() {
        return baiVietLamDeps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhBaiViet;
        TextView txtTieuDeBaiViet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHinhBaiViet = itemView.findViewById(R.id.imgHinhBaiViet);
            txtTieuDeBaiViet = itemView.findViewById(R.id.txtTieuDeBaiViet);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BaiVietLamDepActivity.class);
                    intent.putExtra("baivietlamdep", baiVietLamDeps.get(getPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}
