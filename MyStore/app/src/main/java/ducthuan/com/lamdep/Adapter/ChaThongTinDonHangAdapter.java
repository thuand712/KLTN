package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Activity.XacNhanThongTinMuaHangActivity;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.R;

public class ChaThongTinDonHangAdapter extends RecyclerView.Adapter<ChaThongTinDonHangAdapter.ViewHolder>{

    Context context;
    ArrayList<String>maShops;
    ArrayList<GioHang>gioHangs;
    ArrayList<String>tenShops;
    String[]loinhan;
    String[]tongtienss;

    public ChaThongTinDonHangAdapter(Context context, ArrayList<GioHang> gioHangs, ArrayList<String>maShops,ArrayList<String>tenShops) {
        this.context = context;
        this.gioHangs = gioHangs;
        this.maShops=maShops;
        this.tenShops=tenShops;
        loinhan = new String[maShops.size()];
        tongtienss = new String[maShops.size()];
    }

    public String[] getTongtienss() {
        return tongtienss;
    }

    public String[] getLoinhan() {
        return loinhan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_cha_sanpham, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.txtTenShop.setText("Được giao bởi shop "+tenShops.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvCha.setLayoutManager(layoutManager);
        holder.rvCha.setHasFixedSize(true);
        holder.rvCha.setNestedScrollingEnabled(true);

        ArrayList<GioHang>itemcon = new ArrayList<>();

        int dem = 0;
        int tongtien = 0;

        for(int i = 0; i < gioHangs.size();i++){
            if(gioHangs.get(i).getMASHOP().equals(maShops.get(position))){
                itemcon.add(gioHangs.get(i));
                dem++;

                int giachuakm = Integer.parseInt(gioHangs.get(i).getGIASP());
                int km = Integer.parseInt(gioHangs.get(i).getKHUYENMAI());
                int sl = Integer.parseInt(gioHangs.get(i).getSOLUONG());
                tongtien += (giachuakm/100)*(100-km)*sl;
            }
        }

        holder.txtTextTongTienShop.setText("Tổng số tiền ("+dem+") sản phẩm:");
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        holder.txtTongTienShop.setText(decimalFormat.format(tongtien)+"đ");

        tongtienss[position] = holder.txtTongTienShop.getText().toString();
        holder.edTinNhanChoShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loinhan[position] = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


        ConThongTinDonHangAdapter conThongTinDonHangAdapter = new ConThongTinDonHangAdapter(context,itemcon);
        holder.rvCha.setAdapter(conThongTinDonHangAdapter);
        conThongTinDonHangAdapter.notifyDataSetChanged();




    }

    @Override
    public int getItemCount() {
        return maShops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvCha;
        TextView txtTenShop,txtTextTongTienShop,txtTongTienShop;
        EditText edTinNhanChoShop;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvCha = itemView.findViewById(R.id.rvCha);
            txtTenShop = itemView.findViewById(R.id.txtTenShop);
            txtTextTongTienShop = itemView.findViewById(R.id.txtTextTongTienShop);
            txtTongTienShop = itemView.findViewById(R.id.txtTongTienShop);
            edTinNhanChoShop = itemView.findViewById(R.id.edTinNhanChoShop);

        }
    }


}
