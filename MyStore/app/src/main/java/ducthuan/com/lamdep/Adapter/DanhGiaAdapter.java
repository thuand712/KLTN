package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.DanhGia;
import ducthuan.com.lamdep.R;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {

    Context context;
    ArrayList<DanhGia>danhGias;

    public DanhGiaAdapter(Context context, ArrayList<DanhGia> danhGias) {
        this.context = context;
        this.danhGias = danhGias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_danhgia, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhGia danhGia = danhGias.get(position);
        if(danhGia.getHINH()!=null){
            String s = danhGias.get(position).getHINH();
            Picasso.with(context).load(TrangChuActivity.base_url+s).placeholder(R.drawable.noimage)
                    .error(R.drawable.error).into(holder.imgHinhKhachHangDG);
        }else {
            holder.imgHinhKhachHangDG.setImageResource(R.drawable.logo);
        }
        float sosao = Float.parseFloat(danhGia.getSOSAO());
        holder.ratingBarDG.setRating(sosao);
        holder.txtNgayDG.setText(danhGia.getNGAYDG());
        holder.txtNoiDungDG.setText(danhGia.getNOIDUNG());
        holder.txtTieuDeDG.setText(danhGia.getTIEUDE());
        holder.txtTenNhanVien.setText(danhGia.getTENNV());
    }

    @Override
    public int getItemCount() {
        return danhGias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgHinhKhachHangDG;
        TextView txtTenNhanVien,txtTieuDeDG,txtNoiDungDG,txtNgayDG;
        RatingBar ratingBarDG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhKhachHangDG = itemView.findViewById(R.id.imgHinhKhachHangDG);
            txtTenNhanVien = itemView.findViewById(R.id.txtTenNhanVien);
            txtTieuDeDG = itemView.findViewById(R.id.txtTieuDeDG);
            txtNoiDungDG = itemView.findViewById(R.id.txtNoiDungDG);
            txtNgayDG = itemView.findViewById(R.id.txtNgayDG);
            ratingBarDG = itemView.findViewById(R.id.ratingBarDG);
        }
    }

}
