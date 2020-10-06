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
import java.util.StringTokenizer;

import ducthuan.com.lamdep.Activity.ChiTietDonHangShopActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.QuanLyDonHangShop;
import ducthuan.com.lamdep.R;

public class QuanLyDonHangShopAdapter extends RecyclerView.Adapter<QuanLyDonHangShopAdapter.ViewHolder>{

    Context context;
    ArrayList<QuanLyDonHangShop>quanLyDonHangShops;
    ArrayList<String>madhs;

    public QuanLyDonHangShopAdapter(Context context, ArrayList<QuanLyDonHangShop> quanLyDonHangShops, ArrayList<String>madhs) {
        this.context = context;
        this.quanLyDonHangShops = quanLyDonHangShops;
        this.madhs = madhs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_shop_quanlydonhang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtMaDonHang.setText(madhs.get(position));
        int slsanphamdonhang = 0;
        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getMAHD().equals(madhs.get(position))){

                StringTokenizer stringTokenizer = new StringTokenizer(quanLyDonHangShops.get(i).getTENSHOP(),"@");
                while (stringTokenizer.hasMoreTokens()){
                    holder.txtTenShop.setText(stringTokenizer.nextToken());
                    break;
                }

                holder.txtTenSP.setText(quanLyDonHangShops.get(i).getTENSP());
                holder.txtPhanLoaiSP.setText(quanLyDonHangShops.get(i).getMAUSAC()+","+quanLyDonHangShops.get(i).getKICHTHUOC());
                Picasso.with(context).load(TrangChuActivity.base_url+quanLyDonHangShops.get(i).getANHLON()).placeholder(R.drawable.noimage)
                        .error(R.drawable.error).into(holder.imgHinhSP);
                holder.txtTongTien.setText(quanLyDonHangShops.get(i).getTONGTIEN());
                holder.txtTrangThai.setText(quanLyDonHangShops.get(i).getTRANGTHAI());
                break;
            }
        }

        for(int i = 0; i < quanLyDonHangShops.size();i++){
            if(quanLyDonHangShops.get(i).getMAHD().equals(madhs.get(position))){
                int sl = Integer.parseInt(quanLyDonHangShops.get(i).getSOLUONG());
                slsanphamdonhang+=sl;
            }
        }

        holder.txtSoLuong.setText(slsanphamdonhang+" sản phẩm");

    }

    @Override
    public int getItemCount() {
        return madhs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenShop,txtTrangThai,txtTenSP,txtPhanLoaiSP,txtSoLuong,txtTongTien,txtMaDonHang;
        ImageView imgHinhSP;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenShop = itemView.findViewById(R.id.txtTenShop);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanLoaiSP = itemView.findViewById(R.id.txtPhanLoaiSP);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            txtMaDonHang = itemView.findViewById(R.id.txtMaDonHang);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<QuanLyDonHangShop>donhangs = new ArrayList<>();
                    for(int i = 0; i < quanLyDonHangShops.size();i++){
                        if(quanLyDonHangShops.get(i).getMAHD().equals(madhs.get(getPosition()))){
                            donhangs.add(quanLyDonHangShops.get(i));
                        }
                    }
                    Intent intent = new Intent(context, ChiTietDonHangShopActivity.class);
                    intent.putParcelableArrayListExtra("donhang",donhangs);
                    context.startActivity(intent);
                }
            });
        }
    }

}
