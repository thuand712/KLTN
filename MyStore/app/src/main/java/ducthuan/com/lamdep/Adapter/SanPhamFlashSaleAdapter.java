package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.FlashSaleActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;

public class SanPhamFlashSaleAdapter extends RecyclerView.Adapter<SanPhamFlashSaleAdapter.ViewHolder>{

    Context context;
    ArrayList<SanPham>sanPhamFlashSales;

    public SanPhamFlashSaleAdapter(Context context, ArrayList<SanPham> sanPhamFlashSales) {
        this.context = context;
        this.sanPhamFlashSales = sanPhamFlashSales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_flashsale, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPham sanPhamFlashSale = sanPhamFlashSales.get(position);

        holder.txtPhanTramGiamGia.setText("-"+sanPhamFlashSale.getKHUYENMAI()+"%");

        //giá sản phẩm sau khi khuyến mãi
        int km = Integer.parseInt(sanPhamFlashSale.getKHUYENMAI());
        int giachuakm = Integer.parseInt(sanPhamFlashSale.getGIA());
        int gsp = (giachuakm/100)*(100-km);
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSPFS.setText(String.valueOf(decimalFormat.format(gsp))+"đ");

        int sll = Integer.parseInt(sanPhamFlashSale.getSOLUONG());
        int lmm = Integer.parseInt(sanPhamFlashSale.getLUOTMUA());
        if(lmm == sll){
            holder.txtSoLuongSP.setText("ĐÃ BÁN HẾT");
        }else{
            holder.txtSoLuongSP.setText("ĐÃ BÁN "+lmm);
        }

        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamFlashSale.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPFS);

        Double luotmua = Double.parseDouble(sanPhamFlashSale.getLUOTMUA());
        Double soluong = Double.parseDouble(sanPhamFlashSale.getSOLUONG());

        Double pars = (luotmua/soluong)*100;
        int progress = pars.intValue();

        holder.progressBarFlashSale.setProgress(progress);

        if(luotmua >=(soluong/2)){
            holder.imgFileFlashsale.setVisibility(View.VISIBLE);
        }else{
            holder.imgFileFlashsale.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,FlashSaleActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamFlashSales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSPFS, imgFileFlashsale;
        TextView txtPhanTramGiamGia,txtGiaSPFS,txtSoLuongSP;
        ProgressBar progressBarFlashSale;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPFS = itemView.findViewById(R.id.imgHinhSPFS);
            txtPhanTramGiamGia = itemView.findViewById(R.id.txtPhanTramGiamGia);
            txtGiaSPFS = itemView.findViewById(R.id.txtGiaSPFS);
            txtSoLuongSP = itemView.findViewById(R.id.txtSoLuongSP);
            progressBarFlashSale = itemView.findViewById(R.id.progressBarFlashSale);
            imgFileFlashsale = itemView.findViewById(R.id.imgFireFlashSale);
        }
    }
}
