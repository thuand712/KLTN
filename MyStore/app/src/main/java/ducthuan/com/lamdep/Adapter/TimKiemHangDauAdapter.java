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

public class TimKiemHangDauAdapter extends RecyclerView.Adapter<TimKiemHangDauAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham> sanPhamTimKiems;

    public TimKiemHangDauAdapter(Context context, ArrayList<SanPham> sanPhamTimKiems) {
        this.context = context;
        this.sanPhamTimKiems = sanPhamTimKiems;
    }

    @NonNull
    @Override
    public TimKiemHangDauAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_timkiem_hangdau_activity, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPhamTimKiem = sanPhamTimKiems.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamTimKiem.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPTK);
        holder.txtLuotMua.setText("Đã bán: "+sanPhamTimKiem.getLUOTMUA());
        holder.txtTenSanPham.setText(sanPhamTimKiem.getTENSP());
        //giá sản phẩm sau khi khuyến mãi
        int km = Integer.parseInt(sanPhamTimKiem.getKHUYENMAI());
        int giachuakm = Integer.parseInt(sanPhamTimKiem.getGIA());
        int gsp = (giachuakm/100)*(100-km);
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtGiaSP.setText(decimalFormat.format(gsp)+"đ");
        holder.txtTop.setText("TOP\n"+(position+1));
    }


    @Override
    public int getItemCount() {
        return sanPhamTimKiems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinhSPTK;
        TextView txtTenSanPham,txtLuotMua,txtGiaSP,txtTop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPTK = itemView.findViewById(R.id.imgHinhSPTK);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtLuotMua = itemView.findViewById(R.id.txtLuotMua);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtTop = itemView.findViewById(R.id.txtTop);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(sanPhamTimKiems.get(getPosition()).getMASP());
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
