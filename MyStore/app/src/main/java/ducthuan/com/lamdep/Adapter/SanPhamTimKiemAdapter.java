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

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.TimKiem;
import ducthuan.com.lamdep.R;

public class SanPhamTimKiemAdapter extends RecyclerView.Adapter<SanPhamTimKiemAdapter.ViewHolder> {

    Context context;
    ArrayList<TimKiem> sanPhamTimKiems;

    public SanPhamTimKiemAdapter(Context context, ArrayList<TimKiem> sanPhamTimKiems) {
        this.context = context;
        this.sanPhamTimKiems = sanPhamTimKiems;
    }

    @NonNull
    @Override
    public SanPhamTimKiemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_timkiem_hangdau, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimKiem sanPhamTimKiem = sanPhamTimKiems.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamTimKiem.getHINH()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPTK);
        holder.txtLuotMua.setText("Đã bán "+sanPhamTimKiem.getLUOTMUA()+"+");
        holder.txtTenSanPham.setText(sanPhamTimKiem.getNOIDUNG());
    }


    @Override
    public int getItemCount() {
        return sanPhamTimKiems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSPTK;
        TextView txtTenSanPham,txtLuotMua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPTK = itemView.findViewById(R.id.imgHinhSPTK);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtLuotMua = itemView.findViewById(R.id.txtLuotMua);

        }
    }

}
