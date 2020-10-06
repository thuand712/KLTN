package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class HienThiSanPhamTheoDanhMucAdapter extends RecyclerView.Adapter<HienThiSanPhamTheoDanhMucAdapter.ViewHolder> {

    Context context;
    ArrayList<SanPham>sanPhams;
    int layoutManager;

    public HienThiSanPhamTheoDanhMucAdapter(Context context, int layoutManager, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
        this.layoutManager = layoutManager;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgHinhSPTheoDM;
        TextView txtPhanTramKMTheoDM,txtTenSPTheoDM,txtGiaSPTheoDM,txtGiaSPTheoDMChuaKM,txtLuotMuaSPTheoDM;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPTheoDM = itemView.findViewById(R.id.imgHinhSPTheoDM);
            txtPhanTramKMTheoDM = itemView.findViewById(R.id.txtPhanTramKMTheoDM);
            txtTenSPTheoDM = itemView.findViewById(R.id.txtTenSPTheoDM);
            txtGiaSPTheoDM = itemView.findViewById(R.id.txtGiaSPTheoDM);
            txtGiaSPTheoDMChuaKM = itemView.findViewById(R.id.txtGiaSPTheoDMChuaKM);
            txtLuotMuaSPTheoDM = itemView.findViewById(R.id.txtLuotMuaSPTheoDM);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DataService dataService =APIService.getService();
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
    @NonNull
    @Override
    public HienThiSanPhamTheoDanhMucAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(layoutManager == 1){
            view = LayoutInflater.from(context).inflate(R.layout.dong_grid_sanpham_theodanhmuc, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.dong_list_sanpham_theodanhmuc, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HienThiSanPhamTheoDanhMucAdapter.ViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPham.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPTheoDM);
        holder.txtTenSPTheoDM.setText(sanPham.getTENSP());
        holder.txtLuotMuaSPTheoDM.setText("Đã bán "+sanPham.getLUOTMUA());

        int km = Integer.parseInt(sanPham.getKHUYENMAI());
        int gkm = Integer.parseInt(sanPham.getGIA());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        if(km == 0){
            holder.txtPhanTramKMTheoDM.setVisibility(View.GONE);
            holder.txtGiaSPTheoDMChuaKM.setVisibility(View.INVISIBLE);
            holder.txtGiaSPTheoDM.setText(String.valueOf(decimalFormat.format(gkm))+"đ");

        }else if(km > 0){
            int giagiam = (gkm/100)* (100-km);
            holder.txtGiaSPTheoDM.setText(String.valueOf(decimalFormat.format(giagiam)+"đ"));
            holder.txtPhanTramKMTheoDM.setText("-"+sanPham.getKHUYENMAI()+"%");
            holder.txtGiaSPTheoDMChuaKM.setText(String.valueOf(decimalFormat.format((gkm))+"đ"));
            holder.txtGiaSPTheoDMChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        DataService dataService = APIService.getService();
        Call<String> call = dataService.getDanhGiaTheoSP(sanPham.getMASP());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("0")){
                    holder.ratingBar.setRating(0);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                }else{
                    float rt = Float.parseFloat(kq);
                    holder.ratingBar.setRating(rt);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }


}
