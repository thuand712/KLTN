package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ducthuan.com.lamdep.Model.DoanhThu;
import ducthuan.com.lamdep.R;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.ViewHolder>{

    Context context;
    ArrayList<DoanhThu>doanhThus;

    public DoanhThuAdapter(Context context, ArrayList<DoanhThu> doanhThus) {
        this.context = context;
        this.doanhThus = doanhThus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_doanhthu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoanhThu doanhThu = doanhThus.get(position);
        holder.txtMaHD.setText(doanhThu.getMAHDTONG());
        holder.txtTenKH.setText(doanhThu.getTENNGUOINHAN());
        holder.txtNgayGiao.setText(doanhThu.getNGAYGIAO());
        holder.txtTongTien.setText(doanhThu.getTONGTIEN());
    }

    @Override
    public int getItemCount() {
        return doanhThus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMaHD,txtTenKH,txtNgayGiao,txtTongTien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaHD = itemView.findViewById(R.id.txtMaHD);
            txtTenKH = itemView.findViewById(R.id.txtTenKH);
            txtNgayGiao = itemView.findViewById(R.id.txtNgayGiao);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);

        }
    }
}
