package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.R;

public class ConThongTinDonHangAdapter extends RecyclerView.Adapter<ConThongTinDonHangAdapter.ViewHolder>{

    Context context;
    ArrayList<GioHang>gioHangs;

    public ConThongTinDonHangAdapter(Context context, ArrayList<GioHang> gioHangs) {
        this.context = context;
        this.gioHangs = gioHangs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_thongtinmuahang, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GioHang gioHang = gioHangs.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+gioHang.getHINH()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtPhanLoaiSP.setText("Phân loại: "+gioHang.getMAUSAC()+", "+gioHang.getKICHTHUOC()+", "+gioHang.getSOLUONG());
        holder.txtTenSP.setText(gioHang.getTENSP());
        holder.txtSL.setText("x"+gioHang.getSOLUONG());

        int km = Integer.parseInt(gioHang.getKHUYENMAI());
        int giachuakm = Integer.parseInt(gioHang.getGIASP());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        if(km==0){
            holder.txtKM.setVisibility(View.GONE);
            holder.txtGiaSPChuaKM.setVisibility(View.GONE);
            holder.txtGiaSP.setText(decimalFormat.format(giachuakm)+"đ");
        }else {
            holder.txtKM.setText("-"+gioHang.getKHUYENMAI()+"%");
            holder.txtGiaSPChuaKM.setText(decimalFormat.format(giachuakm)+"đ");
            holder.txtGiaSPChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int giasp = (giachuakm/100)*(100-km);
            holder.txtGiaSP.setText(decimalFormat.format(giasp)+"đ");
        }
    }

    @Override
    public int getItemCount() {
        return gioHangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSP;
        TextView txtTenSP,txtPhanLoaiSP,txtGiaSP,txtGiaSPChuaKM,txtKM,txtSL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanLoaiSP = itemView.findViewById(R.id.txtPhanLoaiSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtGiaSPChuaKM = itemView.findViewById(R.id.txtGiaSPChuaKM);
            txtKM = itemView.findViewById(R.id.txtKM);
            txtSL = itemView.findViewById(R.id.txtSL);
        }
    }


}
