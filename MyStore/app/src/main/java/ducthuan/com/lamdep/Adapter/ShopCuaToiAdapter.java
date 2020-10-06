package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ShopCuaToiAdapter extends RecyclerView.Adapter<ShopCuaToiAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham>sanPhams;

    public ShopCuaToiAdapter(Context context, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham_shopcuatoi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SanPham sanPham = sanPhams.get(position);
        if (sanPham.getTRANGTHAI().equals("Chờ xác nhận")){
            holder.txtTrangThai.setVisibility(View.VISIBLE);
        }else {
            holder.txtTrangThai.setVisibility(View.GONE);
        }
        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtTenSP.setText(sanPham.getTENSP());
        holder.txtLuotThich.setText(sanPham.getLUOTTHICH());
        holder.txtLuotXem.setText(sanPham.getLUOTXEM());
        holder.txtDaBan.setText("Đã bán "+sanPham.getLUOTMUA());

        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int gsp = Integer.parseInt(sanPham.getGIA());
        int giakm = (gsp/100)* (100-km);
        holder.txtGiaSP.setText(String.valueOf(decimalFormat.format(gsp))+"đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sanPham.getTRANGTHAI().equals("Chờ xác nhận")){
                    Toast.makeText(context, "Sản phẩm của bạn đang chờ xác nhận!", Toast.LENGTH_SHORT).show();
                }else {
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(sanPham.getMASP());
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSP;
        TextView txtTenSP,txtGiaSP,txtLuotThich,txtLuotXem,txtDaBan,txtTrangThai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtLuotThich = itemView.findViewById(R.id.txtLuotThich);
            txtLuotXem = itemView.findViewById(R.id.txtLuotXem);
            txtDaBan = itemView.findViewById(R.id.txtDaBan);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);

        }
    }
}
