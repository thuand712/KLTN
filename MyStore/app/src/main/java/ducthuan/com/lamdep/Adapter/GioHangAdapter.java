package ducthuan.com.lamdep.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ducthuan.com.lamdep.Activity.ChiTietSanPhamActivity;
import ducthuan.com.lamdep.Activity.GioHangActivity;
import ducthuan.com.lamdep.Activity.TrangChuActivity;
import ducthuan.com.lamdep.Model.GioHang;
import ducthuan.com.lamdep.Model.SanPham;
import ducthuan.com.lamdep.R;
import ducthuan.com.lamdep.Service.APIService;
import ducthuan.com.lamdep.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    Context context;
    ArrayList<GioHang> gioHangs;
    String duocchon = "0";

    RadioGroup rgMau,rgKichThuoc;

    public GioHangAdapter(Context context, ArrayList<GioHang> gioHangs) {
        this.context = context;
        this.gioHangs = gioHangs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_giohang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final GioHang gioHang = gioHangs.get(position);
        Picasso.with(context).load(TrangChuActivity.base_url + gioHang.getHINH()).placeholder(R.drawable.noimage)
                .error(R.drawable.error).into(holder.imgHinhSP);
        holder.txtTenSP.setText(gioHang.getTENSP());
        holder.txtPhanLoaiSP.setText("Phân loại: " + gioHang.getMAUSAC() + ", " + gioHang.getKICHTHUOC());

        int km = Integer.parseInt(gioHang.getKHUYENMAI());
        final int giachuakm = Integer.parseInt(gioHang.getGIASP());
        //Format gia tien
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalFormatSymbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        if (km == 0) {
            holder.txtGiaSPChuaKM.setVisibility(View.INVISIBLE);
            holder.txtGiaSP.setText(decimalFormat.format(Integer.parseInt(gioHang.getGIASP())) + "đ");
        } else {
            int giakm = (giachuakm / 100) * (100 - km);
            holder.txtGiaSP.setText(decimalFormat.format(giakm) + "đ");
            holder.txtGiaSPChuaKM.setText(gioHang.getGIASP() + "đ");
            holder.txtGiaSPChuaKM.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.txtSoLuong.setText(gioHang.getSOLUONG());

        //xu ly phan loai sp
        holder.txtPhanLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(R.layout.custom_dialog_sua_phan_loai_sp);
                final AlertDialog dialog = builder.create();
                dialog.show();
                //anh xa
                ImageView imgHinhSP = dialog.findViewById(R.id.imgHinhSP);
                ImageView imgThoat = dialog.findViewById(R.id.imgThoat);
                TextView txtGiaSP = dialog.findViewById(R.id.txtGiaSP);
                TextView txtPhanLoaiSP = dialog.findViewById(R.id.txtPhanLoaiSP);
                rgMau = dialog.findViewById(R.id.rgMau);
                rgKichThuoc = dialog.findViewById(R.id.rgKichThuoc);
                Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

                Picasso.with(context).load(TrangChuActivity.base_url+gioHang.getHINH()).placeholder(R.drawable.noimage).error(R.drawable.error).into(imgHinhSP);
                txtGiaSP.setText(holder.txtGiaSP.getText());
                txtPhanLoaiSP.setText(holder.txtPhanLoaiSP.getText());
                ArrayList<String>dsMauSac = new ArrayList<>();
                ArrayList<String>dsKichThuoc = new ArrayList<>();
                String[] sMau = gioHang.getMAUSACSP().split(",");
                String[]sKT = gioHang.getKICHTHUOCSP().split(",");
                for (int i = 0; i < sMau.length; i++) {
                    dsMauSac.add(sMau[i]);
                }
                for (int i = 0; i < sKT.length; i++) {
                    dsKichThuoc.add(sKT[i]);
                }

                //xử lý thêm màu sắc kích thước
                themMauSac(dsMauSac,gioHang.getMAUSAC());
                themKichThuoc(dsKichThuoc,gioHang.getKICHTHUOC());

                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int vtmau = rgMau.getCheckedRadioButtonId();
                        int vtkichthuoc = rgKichThuoc.getCheckedRadioButtonId();
                        if(vtmau == -1 || vtkichthuoc == -1){
                            Toast.makeText(context, "Vui lòng chọn đầy đủ thuộc tính", Toast.LENGTH_SHORT).show();
                        }else {
                            RadioButton rdMau = rgMau.findViewById(vtmau);
                            RadioButton rdkichthuoc = rgKichThuoc.findViewById(vtkichthuoc);
                            final String mausac = rdMau.getText().toString();
                            final String kichthuoc = rdkichthuoc.getText().toString();

                            DataService dataService = APIService.getService();
                            Call<String>callback = dataService.suaPhanLoaiSP(gioHang.getMASP(),gioHang.getMANV(),mausac,kichthuoc);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq = response.body();
                                    if(kq.equals("OK")){
                                        Toast.makeText(context, "Cập nhập thuộc tính thành công", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "Cập nhập thuộc tính thất bại", Toast.LENGTH_SHORT).show();
                                    }

                                    GioHangActivity.gioHangs.get(position).setMAUSAC(mausac);
                                    GioHangActivity.gioHangs.get(position).setKICHTHUOC(kichthuoc);
                                    GioHangActivity.gioHangAdapter.notifyDataSetChanged();

                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    }
                });



                imgThoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        //xu ly checkbox
        if(gioHang.getDUOCCHON().equals("1")){
            holder.chkSP.setChecked(true);
        }else {
            holder.chkSP.setChecked(false);
        }

        holder.chkSP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b==true){
                    duocchon="1";
                }else {
                    duocchon="0";
                }

                DataService dataService = APIService.getService();
                Call<String>callback = dataService.capNhapChonSPGioHang(gioHang.getMASP(),gioHang.getMANV(),duocchon);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("OK")){
                            GioHangActivity.gioHangs.get(position).setDUOCCHON(duocchon);
                            GioHangActivity.gioHangAdapter.notifyDataSetChanged();
                            GioHangActivity.getTrangThaiChonTatCa();
                            GioHangActivity.getTongTien();

                        }else {
                            Toast.makeText(context, "Đã xảy ra lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




        int slmua = Integer.parseInt(holder.txtSoLuong.getText().toString());
        int sltonkho = Integer.parseInt(gioHang.getSOLUONGTONKHO());
        if (slmua >= sltonkho) {
            holder.btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
            holder.btnTang.setImageResource(R.drawable.ic_cong_xam_24dp);
        } else if (slmua <= 1) {
            holder.btnGiam.setImageResource(R.drawable.ic_tru_xam_24dp);
            holder.btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
        } else if (slmua > 1) {
            holder.btnGiam.setImageResource(R.drawable.ic_tru_den_24dp);
            holder.btnTang.setImageResource(R.drawable.ic_cong_den_24dp);
        }


        //tang so luong sp
        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(gioHang.getSOLUONG().toString());
                int sltonkho = Integer.parseInt(gioHang.getSOLUONGTONKHO());
                if (sl < sltonkho) {
                    sl++;
                    GioHangActivity.gioHangs.get(position).setSOLUONG(sl + "");
                    GioHangActivity.gioHangAdapter.notifyDataSetChanged();
                    GioHangActivity.getTongTien();
                    //cap nhap so luong sp gio hang
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.capNhapSLSPGioHang(gioHang.getMASP(), gioHang.getMANV(), sl);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(context, "Rất tiếc shop chỉ còn " + gioHang.getSOLUONGTONKHO() + " sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(holder.txtSoLuong.getText().toString());
                if (sl >= 2) {
                    sl--;
                    GioHangActivity.gioHangs.get(position).setSOLUONG(sl + "");
                    GioHangActivity.gioHangAdapter.notifyDataSetChanged();
                    GioHangActivity.getTongTien();
                    //cap nhap so luong sp gio hang
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.capNhapSLSPGioHang(gioHang.getMASP(), gioHang.getMANV(), sl);
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });


        //xoa san pham trong gio hang
        holder.imgDeleteSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn chắc chắn muốn xóa sản phẩm này trong giỏ hàng?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataService dataService = APIService.getService();
                        Call<String> callback = dataService.xoaSanPhamGioHang(gioHang.getMASP(), gioHang.getMANV());
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq = response.body();
                                if (kq.equals("OK")) {

                                    GioHangActivity.gioHangs.remove(position);
                                    GioHangActivity.gioHangAdapter.notifyDataSetChanged();
                                    GioHangActivity.getTongTien();
                                    if (gioHangs.size() == 0) {
                                        //không có sp thì hiện view giỏ hàng trống lên
                                        GioHangActivity.rvGioHang.setVisibility(View.GONE);
                                        GioHangActivity.relativeLayout.setVisibility(View.GONE);
                                        GioHangActivity.lnGioHangTrong.setVisibility(View.VISIBLE);
                                        GioHangActivity.txtMuaSamNgay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                context.startActivity(new Intent(context, TrangChuActivity.class));
                                            }
                                        });

                                    }
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                } else if(kq.equals("FAIL")) {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }else if(kq.equals("NO")){
                                    Toast.makeText(context, "Sản phẩm này đã được xóa trước đó", Toast.LENGTH_SHORT).show();
                                    GioHangActivity.gioHangs.remove(position);
                                    GioHangActivity.gioHangAdapter.notifyDataSetChanged();
                                    GioHangActivity.getTongTien();
                                    if (gioHangs.size() == 0) {
                                        //không có sp thì hiện view giỏ hàng trống lên
                                        GioHangActivity.rvGioHang.setVisibility(View.GONE);
                                        GioHangActivity.relativeLayout.setVisibility(View.GONE);
                                        GioHangActivity.lnGioHangTrong.setVisibility(View.VISIBLE);
                                        GioHangActivity.txtMuaSamNgay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                context.startActivity(new Intent(context, TrangChuActivity.class));
                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });

        //ket thuc
    }

    private void themKichThuoc(final ArrayList<String>dsKichThuoc,String kt) {
        for (int i = 0; i < dsKichThuoc.size(); i++) {
            RadioButton rd1 = new RadioButton(context);
            rd1.setId(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,5,0);
            rd1.setLayoutParams(layoutParams);
            rd1.setText(dsKichThuoc.get(i));
            rd1.setButtonDrawable(null);
            rd1.setPadding(10,0,10,0);
            rd1.setTextColor(context.getResources().getColor(R.color.mauden));
            rd1.setBackground(context.getResources().getDrawable(R.drawable.custom_radio_button_select));
            if(dsKichThuoc.get(i).equals(kt)){
                rd1.setChecked(true);
                rd1.setTextColor(context.getResources().getColor(R.color.mauhong));
            }
            rgKichThuoc.addView(rd1);
        }

        rgKichThuoc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = null;

                for (int i1 = 0; i1 < dsKichThuoc.size(); i1++) {
                    if(i1 == i){
                        button = radioGroup.findViewById(i1);
                        button.setChecked(true);
                        button.setTextColor(context.getResources().getColor(R.color.mauhong));
                    }else {
                        button = radioGroup.findViewById(i1);
                        button.setChecked(false);
                        button.setTextColor(context.getResources().getColor(R.color.mauden));
                    }
                }
            }
        });

    }

    private void themMauSac(final ArrayList<String>dsMauSac,String ms) {
        for (int i = 0; i < dsMauSac.size(); i++) {
            RadioButton rd1 = new RadioButton(context);
            rd1.setId(i);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,5,0);
            rd1.setLayoutParams(layoutParams);
            rd1.setText(dsMauSac.get(i));
            rd1.setButtonDrawable(null);
            rd1.setPadding(10,0,10,0);
            rd1.setTextColor(context.getResources().getColor(R.color.mauden));
            rd1.setBackground(context.getResources().getDrawable(R.drawable.custom_radio_button_select));
            if(dsMauSac.get(i).equals(ms)){
                rd1.setChecked(true);
                rd1.setTextColor(context.getResources().getColor(R.color.mauhong));
            }
            rgMau.addView(rd1);
        }

        rgMau.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = null;

                for (int i1 = 0; i1 < dsMauSac.size(); i1++) {
                    if(i1 == i){
                        button = radioGroup.findViewById(i1);
                        button.setChecked(true);
                        button.setTextColor(context.getResources().getColor(R.color.mauhong));
                    }else {
                        button = radioGroup.findViewById(i1);
                        button.setChecked(false);
                        button.setTextColor(context.getResources().getColor(R.color.mauden));
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgHinhSP, imgDeleteSP;
        TextView txtTenSP, txtPhanLoaiSP, txtGiaSP, txtGiaSPChuaKM, txtSoLuong;
        ImageButton btnGiam, btnTang;
        CheckBox chkSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            imgHinhSP = itemView.findViewById(R.id.imgHinhSP);
            imgDeleteSP = itemView.findViewById(R.id.imgDeleteSP);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtPhanLoaiSP = itemView.findViewById(R.id.txtPhanLoaiSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtGiaSPChuaKM = itemView.findViewById(R.id.txtGiaSPChuaKM);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            btnGiam = itemView.findViewById(R.id.btnGiam);
            btnTang = itemView.findViewById(R.id.btnTang);
            chkSP = itemView.findViewById(R.id.chkSP);


            imgHinhSP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService dataService =APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(gioHangs.get(getPosition()).getMASP());
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

            txtTenSP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataService dataService =APIService.getService();
                    Call<List<SanPham>> callback = dataService.getSanPhamDuocChon(gioHangs.get(getPosition()).getMASP());
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
