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
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;

public class FlashSaleNgayKeTiepAdapter extends RecyclerView.Adapter<FlashSaleNgayKeTiepAdapter.ViewHolder>{

    Context context;
    ArrayList<SanPham>sanPhamFlashSales;

    public FlashSaleNgayKeTiepAdapter(Context context, ArrayList<SanPham> sanPhamFlashSales) {
        this.context = context;
        this.sanPhamFlashSales = sanPhamFlashSales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_flashsale_ngayketiep_activity, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPham sanPhamFlashSale = sanPhamFlashSales.get(position);

        holder.txtPhanTramKM.setText("-"+sanPhamFlashSale.getKHUYENMAI()+"%");
        holder.txtTenSP.setText(sanPhamFlashSale.getTENSP());

        //giá sản phẩm sau khi khuyến mãi
        int km = Integer.parseInt(sanPhamFlashSale.getKHUYENMAI());
        int giachuakm = Integer.parseInt(sanPhamFlashSale.getGIA());
        int gsp = (giachuakm/100)*(100-km);
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSP.setText(String.valueOf(decimalFormat.format(gsp))+"đ");
        holder.txtGiaSPChuaKM.setText(decimalFormat.format(giachuakm)+"đ");
        holder.txtGiaSPChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamFlashSale.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);

    }

    @Override
    public int getItemCount() {
        return sanPhamFlashSales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSP;
        TextView txtPhanTramKM,txtTenSP,txtGiaSP,txtGiaSPChuaKM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanTramKM = itemView.findViewById(R.id.txtPhanTramKM);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtGiaSPChuaKM = itemView.findViewById(R.id.txtGiaSPChuaKM);
            
        }
    }
}
