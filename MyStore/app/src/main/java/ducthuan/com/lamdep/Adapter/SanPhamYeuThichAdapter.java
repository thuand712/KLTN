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

import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPhamYeuThichAdapter extends RecyclerView.Adapter<SanPhamYeuThichAdapter.ViewHolder>{

    Context context;
    ArrayList<SanPham>sanPhamYeuThiches;

    public SanPhamYeuThichAdapter(Context context, ArrayList<SanPham> sanPhamYeuThiches) {
        this.context = context;
        this.sanPhamYeuThiches = sanPhamYeuThiches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_sanpham_yeuthich, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sanPhamYeuThich = sanPhamYeuThiches.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url+sanPhamYeuThich.getANHLON()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinhSPYT);
        holder.txtLuotMua.setText("Đã bán "+sanPhamYeuThich.getLUOTMUA());
        holder.txtTenSanPham.setText(sanPhamYeuThich.getTENSP());
    }

    @Override
    public int getItemCount() {
        return sanPhamYeuThiches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhSPYT;
        TextView txtTenSanPham, txtLuotMua;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSPYT = itemView.findViewById(R.id.imgHinhSPYT);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtLuotMua = itemView.findViewById(R.id.txtLuotMua);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService dataService = APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(sanPhamYeuThiches.get(getPosition()).getMASP());
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
