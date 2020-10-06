package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Activity.AdminChiTietNhanVienActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.NhanVien;
import ducthuan.com.lamdep.R;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder>{

    Context context;
    ArrayList<NhanVien>nhanViens;

    public NhanVienAdapter(Context context, ArrayList<NhanVien> nhanViens) {
        this.context = context;
        this.nhanViens = nhanViens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_nhanvien,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NhanVien nhanVien = nhanViens.get(position);
        if(nhanVien.getHINH() != null){
            Picasso.with(context).load(TrangChuActivity.base_url+nhanVien.getHINH())
                    .placeholder(R.drawable.noimage).error(R.drawable.error).into(holder.imgNV);
        }else {
            holder.imgNV.setImageResource(R.drawable.ic_account_circle_print_24dp);
        }

        holder.txtTenNV.setText(nhanVien.getTENNV());
        holder.txtEmail.setText("Email: "+nhanVien.getTENDANGNHAP());
        holder.txtNgayDangKy.setText("Ngày đăng ký: "+nhanVien.getNGAYDANGKY());
        holder.btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminChiTietNhanVienActivity.class);
                intent.putExtra("nhanvien",nhanVien);
                context.startActivity(intent);
            }
        });
        holder.txtTrangThai.setText("Trạng thái: "+nhanVien.getTRANGTHAINV());

    }

    @Override
    public int getItemCount() {
        return nhanViens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgNV;
        ImageButton btnChiTiet;
        TextView txtTenNV,txtEmail,txtNgayDangKy,txtTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNV = itemView.findViewById(R.id.imgNV);
            btnChiTiet = itemView.findViewById(R.id.btnChiTiet);
            txtTenNV = itemView.findViewById(R.id.txtTenNV);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtNgayDangKy = itemView.findViewById(R.id.txtNgayDangKy);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
        }
    }
}
