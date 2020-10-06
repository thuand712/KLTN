package ducthuan.com.lamdep.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ducthuan.com.lamdep.Activity.FlashSaleActivity;
import ducthuan.com.lamdep.Adapter.SanPhamFlashSaleAdapter;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_FlashSale extends Fragment {
    View view;
    TextView txtXemThemFlashSale;
    RecyclerView rvFlashSale;
    SanPhamFlashSaleAdapter sanPhamFlashSaleAdapter;
    ArrayList<SanPham>sanPhamFlashSales;
    TextView txtTimeGio,txtTimePhut, txtTimeGiay;
    ImageView imgFLS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flashsale, container, false);

        addControls();
        dongHoFlashSale();
        getDataFlashSale();
        addEvents();

        return view;
    }

    private void addControls() {
        txtXemThemFlashSale = view.findViewById(R.id.txtXemThemFlashSale);
        rvFlashSale = view.findViewById(R.id.rvFlashSale);
        txtTimeGio = view.findViewById(R.id.txtTimeGio);
        txtTimePhut = view.findViewById(R.id.txtTimePhut);
        txtTimeGiay = view.findViewById(R.id.txtTimeGiay);
        imgFLS = view.findViewById(R.id.imgFLS);

        Animation animAlpha = AnimationUtils.loadAnimation(getContext(),R.anim.anim_alpha_flashsale);
        imgFLS.startAnimation(animAlpha);

    }

    private void addEvents() {
        txtXemThemFlashSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FlashSaleActivity.class));
            }
        });
    }

    private void dongHoFlashSale() {

        Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        int giay = calendar.get(Calendar.SECOND);
        int tongs = 0;
        if(gio >= 0 && gio <= 7){
            tongs = 8*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 8 && gio <= 15){
            tongs = 16*60*60 - (gio*60*60 + phut * 60 + giay);
        }else if(gio >= 16 && gio <= 23){
            tongs = 24*60*60 - (gio*60*60 + phut * 60 + giay);
        }

        new CountDownTimer(tongs * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                txtTimeGio.setText(String.format("%02d", hours));
                txtTimePhut.setText(String.format("%02d", minutes));
                txtTimeGiay.setText(String.format("%02d", seconds));
            }

            public void onFinish() {
                this.start();
            }
        }.start();
    }

    private void getDataFlashSale() {
        DataService dataService = APIService.getService();
        Call<List<SanPham>>callback = dataService.layDanhSachSanPhamFlashSale(0);
        callback.enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                sanPhamFlashSales = (ArrayList<SanPham>) response.body();
                if(sanPhamFlashSales.size()>0){
                    sanPhamFlashSaleAdapter = new SanPhamFlashSaleAdapter(getActivity(), sanPhamFlashSales);
                    rvFlashSale.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
                    rvFlashSale.setHasFixedSize(true);
                    rvFlashSale.setAdapter(sanPhamFlashSaleAdapter);
                    sanPhamFlashSaleAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
            }
        });
    }
}
