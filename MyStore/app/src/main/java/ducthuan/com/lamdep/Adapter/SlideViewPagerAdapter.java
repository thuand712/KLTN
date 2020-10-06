package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.ShowHinhSanPhamActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.R;

public class SlideViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<String>dsHinh;

    public SlideViewPagerAdapter(Context context, ArrayList<String> dsHinh) {
        this.context = context;
        this.dsHinh = dsHinh;
    }

    @Override
    public int getCount() {
        return dsHinh.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_slide_viewpager_chitietsanpham, null);

        ImageView imgHinhCTSP = view.findViewById(R.id.imgHinhCTSP);
        Picasso.with(context).load(TrangChuActivity.base_url+dsHinh.get(position)).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhCTSP);

        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowHinhSanPhamActivity.class);
                intent.putExtra("hinhs",dsHinh);
                context.startActivity(intent);
            }
        });
        return view;
    }

    //khi den cuoi cung thi remove no di, khong de bi loi
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object );
    }
}
