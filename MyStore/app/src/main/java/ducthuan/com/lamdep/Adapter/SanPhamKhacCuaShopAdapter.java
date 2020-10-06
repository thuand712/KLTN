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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SanPhamKhacCuaShopAdapter extends RecyclerView.Adapter<SanPhamKhacCuaShopAdapter.ViewHolder>{

    Context context;
    ArrayList<SanPham>sanPhams;

    public SanPhamKhacCuaShopAdapter(Context context, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dong_cacsanphamkhaccuashop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPham sanPham = sanPhams.get(position);

        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSanPham);
        holder.txtLuotMua.setText("Đã bán "+ sanPham.getLUOTMUA());
        holder.txtTenSanPham.setText(sanPham.getTENSP());

        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int giaGoc = Integer.parseInt(sanPham.getGIA());

        int giaKM = (giaGoc/100)*(100-km);

        if(km == 0){
            holder.txtPhanTramGiamGia.setVisibility(View.GONE);
        }else {
            holder.txtPhanTramGiamGia.setText("-"+km+"%");
            holder.txtPhanTramGiamGia.setVisibility(View.VISIBLE);
        }



        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        holder.txtGia.setText(String.valueOf(decimalFormat.format(giaKM))+"đ");



    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSanPham;
        TextView txtTenSanPham, txtGia,txtLuotMua,txtPhanTramGiamGia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHinhSanPham = itemView.findViewById(R.id.imgHinhSP);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSP);
            txtGia = itemView.findViewById(R.id.txtGiaSP);
            txtLuotMua = itemView.findViewById(R.id.txtDaBan);
            txtPhanTramGiamGia = itemView.findViewById(R.id.txtPhanTramGiamGia);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(sanPhams.get(getPosition()).getMASP());
                    callback.enqueue(new Callback<List<SanPham>>() {
                        @Override
                        public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                            ArrayList<SanPham> sanPhams = (ArrayList<SanPham>) response.body();
                            SanPham sanPham = sanPhams.get(0);
                            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                            intent.putExtra("itemsp",sanPham);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<List<SanPham>> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }
}
