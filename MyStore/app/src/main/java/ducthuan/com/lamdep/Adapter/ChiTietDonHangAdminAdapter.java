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
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangAdminAdapter extends RecyclerView.Adapter<ChiTietDonHangAdminAdapter.ViewHolder>{

    Context context;
    ArrayList<QuanLyDonHangShop>quanLyDonHangShops;

    public ChiTietDonHangAdminAdapter(Context context, ArrayList<QuanLyDonHangShop> quanLyDonHangShops) {
        this.context = context;
        this.quanLyDonHangShops = quanLyDonHangShops;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_chitietdonhang_shop,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QuanLyDonHangShop quanLyDonHangShop = quanLyDonHangShops.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+quanLyDonHangShop.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtTenSP.setText(quanLyDonHangShop.getTENSP());
        holder.txtPhanLoaiSP.setText(quanLyDonHangShop.getMAUSAC()+", "+quanLyDonHangShop.getKICHTHUOC());

        //giá sản phẩm sau khi khuyến mãi
        int gsp = Integer.parseInt(quanLyDonHangShop.getGIASP());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSP.setText(decimalFormat.format(gsp)+"đ");
        holder.txtSoLuongSP.setText("x "+quanLyDonHangShop.getSOLUONG());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataService dataService = APIService.getService();
                Call<List<SanPham>> call = dataService.getSanPhamDuocChon(quanLyDonHangShop.getMASP());
                call.enqueue(new Callback<List<SanPham>>() {
                    @Override
                    public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                        ArrayList<SanPham>sanPhams = (ArrayList<SanPham>) response.body();
                        if(sanPhams.size()>0){
                            Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                            intent.putExtra("itemsp",sanPhams.get(0));
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SanPham>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return quanLyDonHangShops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSP;
        TextView txtTenSP,txtPhanLoaiSP,txtGiaSP,txtSoLuongSP;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanLoaiSP = itemView.findViewById(R.id.txtPhanLoaiSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtSoLuongSP = itemView.findViewById(R.id.txtSoLuongSP);
        }
    }
}
