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
import ducthuan.com.lamdep.Activity.TimKiemHangDauActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.Model.TimKiem;
import ducthuan.com.lamdep.Model.TimKiemHangDau;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.ViewHolder> {

    Context context;
    ArrayList<TimKiemHangDau> timKiemHangDaus;

    public TimKiemAdapter(Context context, ArrayList<TimKiemHangDau> timKiemHangDaus) {
        this.context = context;
        this.timKiemHangDaus = timKiemHangDaus;
    }

    @NonNull
    @Override
    public TimKiemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_timkiem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TimKiemHangDau timKiemHangDau = timKiemHangDaus.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url + timKiemHangDau.getHinh()).placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgHinh);
        holder.txtTen.setText(timKiemHangDau.getNoidung());
        if (timKiemHangDau.isCheck() == true) {
            holder.txtTen.setTextColor(context.getResources().getColor(R.color.mauhong));
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.txtTen.setTextColor(context.getResources().getColor(R.color.mauden));
            holder.view.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < timKiemHangDaus.size(); i++) {
                    if (i == position) {
                        timKiemHangDaus.get(i).setCheck(true);
                    } else {
                        timKiemHangDaus.get(i).setCheck(false);
                    }
                }
                TimKiemHangDauActivity.timKiemAdapter.notifyDataSetChanged();
                TimKiemHangDauActivity.sanPhams = new ArrayList<>();
                DataService dataService = APIService.getService();
                Call<List<SanPham>>call = dataService.getTopSPTimKiem(timKiemHangDau.getNoidung(),0);
                call.enqueue(new Callback<List<SanPham>>() {
                    @Override
                    public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                        TimKiemHangDauActivity.sanPhams = (ArrayList<SanPham>) response.body();
                        TimKiemHangDauActivity.timKiemHangDauAdapter = new TimKiemHangDauAdapter(context,TimKiemHangDauActivity.sanPhams);
                        TimKiemHangDauActivity.rvSanPham.setAdapter(TimKiemHangDauActivity.timKiemHangDauAdapter);
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
        return timKiemHangDaus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinh;
        TextView txtTen;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            txtTen = itemView.findViewById(R.id.txtTen);
            view = itemView.findViewById(R.id.view);


        }
    }

}

