package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.ThemSanPhamShopCuaToiActivity;
import ducthuan.com.lamdep.R;

public class HinhSanPhamSuaAdapter extends RecyclerView.Adapter<HinhSanPhamSuaAdapter.ViewHolder>{

    Context context;
    ArrayList<Uri>dsHinhSP;

    public HinhSanPhamSuaAdapter(Context context, ArrayList<Uri> dsHinhSP) {
        this.context = context;
        this.dsHinhSP = dsHinhSP;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_anhnho_suasanpham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Uri uri = dsHinhSP.get(position);
        Picasso.with(context).load(uri).into(holder.imgAnhNho);
        /*holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSanPhamShopCuaToiActivity.uriHinhSP.remove(position);
                ThemSanPhamShopCuaToiActivity.hinhSanPhamAdapter.notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dsHinhSP.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnhNho,imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhNho = itemView.findViewById(R.id.imgAnhNho);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
