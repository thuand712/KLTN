package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.DiaChiActivity;
import ducthuan.com.lamdep.Model.DiaChiKhachHang;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaChiAdapter extends RecyclerView.Adapter<DiaChiAdapter.ViewHolder>{

    int mSelectedItem;
    Context context;
    ArrayList<DiaChiKhachHang>diaChiKhachHangs;

    public DiaChiAdapter(Context context, ArrayList<DiaChiKhachHang> diaChiKhachHangs) {
        this.context = context;
        this.diaChiKhachHangs = diaChiKhachHangs;
        mSelectedItem = -1;
    }


    public int getmSelectedItem() {
        return mSelectedItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_diachi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DiaChiKhachHang diaChiKhachHang = diaChiKhachHangs.get(position);
        if(diaChiKhachHang.getMACDINH().equals("1")){
            holder.txtDiaChiMacDinh.setVisibility(View.VISIBLE);
        }else {
            holder.txtDiaChiMacDinh.setVisibility(View.GONE);
        }

        holder.rdSelected.setChecked(position == mSelectedItem);



        holder.txtTenNV.setText(diaChiKhachHang.getTENKH());
        holder.txtSdt.setText(diaChiKhachHang.getSODTKH());
        holder.txtDiaChi.setText(diaChiKhachHang.getDIACHIKH());




        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.imgMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_more_diachi,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.itMacDinh:
                                DataService dataService = APIService.getService();
                                Call<String>callback = dataService.setDiaChiMacDinh(diaChiKhachHang.getMAKH(),diaChiKhachHang.getMADC());
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq = response.body();
                                        if(kq.equals("OK")){

                                            for(int i = 0; i < DiaChiActivity.diaChiKhachHangs.size();i++){
                                                DiaChiActivity.diaChiKhachHangs.get(i).setMACDINH("0");
                                            }
                                            DiaChiActivity.diaChiKhachHangs.get(position).setMACDINH("1");
                                            DiaChiActivity.diaChiAdapter.notifyDataSetChanged();
                                        }else {
                                            Toast.makeText(context, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                                break;
                            case R.id.itXoa:
                                if(diaChiKhachHang.getMACDINH().equals("1")){
                                    Toast.makeText(context, "Bạn không thể xóa địa chỉ mặc định !", Toast.LENGTH_SHORT).show();
                                }else {
                                    DataService dataService1 = APIService.getService();
                                    Call<String>callback1 = dataService1.xoaDiaChiKhachHang(diaChiKhachHang.getMADC());
                                    callback1.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq = response.body();
                                            if(kq.equals("OK")){
                                                DiaChiActivity.diaChiKhachHangs.remove(position);
                                                DiaChiActivity.diaChiAdapter.notifyDataSetChanged();
                                            }else {
                                                Toast.makeText(context, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                }
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return diaChiKhachHangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton rdSelected;
        TextView txtTenNV,txtSdt,txtDiaChi,txtDiaChiMacDinh;
        ImageView imgMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rdSelected = itemView.findViewById(R.id.rdSelected);
            txtTenNV = itemView.findViewById(R.id.txtTenNV);
            txtSdt = itemView.findViewById(R.id.txtSdt);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtDiaChiMacDinh = itemView.findViewById(R.id.txtDiaChiMacDinh);
            imgMore = itemView.findViewById(R.id.imgMore);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            rdSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, diaChiKhachHangs.size());
                }
            });
        }
    }
}
