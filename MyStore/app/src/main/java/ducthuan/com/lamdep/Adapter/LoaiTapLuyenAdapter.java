package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ducthuan.com.lamdep.Model.TapLuyen;
import ducthuan.com.lamdep.R;

public class LoaiTapLuyenAdapter extends RecyclerView.Adapter<LoaiTapLuyenAdapter.ViewHolder> {

    Context context;
    ArrayList<TapLuyen> tapLuyens;
    AssetManager assetManager;


    public LoaiTapLuyenAdapter(Context context, ArrayList<TapLuyen> tapLuyens) {
        this.context = context;
        this.tapLuyens = tapLuyens;
        assetManager = context.getAssets();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_loai_tap_luyen, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TapLuyen tapLuyen = tapLuyens.get(position);
        holder.txtTen.setText(tapLuyen.getTentapluyen());

        if (tapLuyen.getLaplai().equals("0")) {
            int laplai = Integer.parseInt(tapLuyen.getThoigianlaplai()) / 1000;
            holder.txtLapLai.setText(laplai + "s");
        } else {
            holder.txtLapLai.setText("x" + tapLuyen.getLaplai());
        }


        try {
            String[] dshinh = tapLuyen.getHinh().split("\\,");
            Bitmap bitmap = null;
            InputStream inputStream = assetManager.open(dshinh[0]);
            bitmap = BitmapFactory.decodeStream(inputStream);
            holder.imgHinh.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TapLuyenActivity.class);
                intent.putExtra("tapluyen", tapLuyens.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tapLuyens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtLapLai;
        ImageView imgHinh;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTen = itemView.findViewById(R.id.txtTen);
            txtLapLai = itemView.findViewById(R.id.txtLapLai);
            imgHinh = itemView.findViewById(R.id.imgHinh);

        }
    }
}
