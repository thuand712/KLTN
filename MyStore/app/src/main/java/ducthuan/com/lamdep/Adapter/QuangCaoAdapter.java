package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.QuangCao;
import ducthuan.com.lamdep.R;

public class QuangCaoAdapter extends PagerAdapter {

    Context context;
    ArrayList<QuangCao> quangCaos;

    public QuangCaoAdapter(Context context, ArrayList<QuangCao> dsquangcao) {
        this.context = context;
        this.quangCaos = dsquangcao;
    }

    @Override
    public int getCount() {
        return quangCaos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_quangcao, null);

        ImageView imgBgQuangCao = view.findViewById(R.id.imgBgQuangCao);
        Picasso.with(context).load(TrangChuActivity.base_url+quangCaos.get(position).getHINHQUANGCAO()).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgBgQuangCao);

        container.addView(view);
        return view;
    }

    //khi den cuoi cung thi remove no di, khong de bi loi
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object );
    }


}
