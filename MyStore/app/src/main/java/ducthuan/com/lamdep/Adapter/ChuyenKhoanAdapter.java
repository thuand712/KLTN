package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietChuyenKhoanActivity;
import ducthuan.com.lamdep.Model.ChuyenKhoan;
import ducthuan.com.lamdep.R;

public class ChuyenKhoanAdapter extends RecyclerView.Adapter<ChuyenKhoanAdapter.ViewHolder>{

    Context context;
    ArrayList<ChuyenKhoan>chuyenKhoans;

    public ChuyenKhoanAdapter(Context context, ArrayList<ChuyenKhoan> chuyenKhoans) {
        this.context = context;
        this.chuyenKhoans = chuyenKhoans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_chuyenkhoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChuyenKhoan chuyenKhoan = chuyenKhoans.get(position);
        holder.txtMaHD.setText(chuyenKhoan.getMAHDTONG());
        holder.txtTenKH.setText(chuyenKhoan.getTENNGUOINHAN());
        holder.txtNgayCK.setText(chuyenKhoan.getNGAYCK());
        holder.txtTongTienHD.setText(chuyenKhoan.getTONGTIENHD());
        holder.txtTrangThai.setText(chuyenKhoan.getTRANGTHAI());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        int sotienchuyen = Integer.parseInt(chuyenKhoan.getSOTIENCHUYEN());
        holder.txtSoTienChuyen.setText(decimalFormat.format(sotienchuyen)+"đ");

        holder.txtXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietChuyenKhoanActivity.class);
                intent.putExtra("chuyenkhoan",chuyenKhoan);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chuyenKhoans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaHD,txtTenKH,txtNgayCK,txtTongTienHD,txtSoTienChuyen,txtTrangThai,txtXemChiTiet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaHD = itemView.findViewById(R.id.txtMaHD);
            txtTenKH = itemView.findViewById(R.id.txtTenKH);
            txtNgayCK = itemView.findViewById(R.id.txtNgayCK);
            txtTongTienHD = itemView.findViewById(R.id.txtTongTienHD);
            txtSoTienChuyen = itemView.findViewById(R.id.txtSoTienChuyen);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtXemChiTiet = itemView.findViewById(R.id.txtXemChiTiet);
        }
    }
}
