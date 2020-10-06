package ducthuan.com.lamdep.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ducthuan.com.lamdep.R;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNameMap;

public class TestActivity extends AppCompatActivity {

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Shopify";
    private String merchantCode = "MOMOIGMP20200824";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán đơn hàng 123";

    EditText edAmount;
    Button btnPayMoMo;
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        addControls();
        addEvents();
    }

    private void addControls() {
        edAmount = findViewById(R.id.edAmount);
        btnPayMoMo = findViewById(R.id.btnPayMoMo);
        tvMessage = findViewById(R.id.tvMessage);

    }


    private void addEvents() {

        btnPayMoMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment();
            }
        });

    }

    //Get token through MoMo app
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", "123"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", Integer.parseInt(edAmount.getText().toString())); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();

                    } else {
                        tvMessage.setText("message: this.getString(R.string.not_receive_info)");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    tvMessage.setText("message: this.getString(R.string.not_receive_info)");
                } else {
                    //TOKEN FAIL
                    tvMessage.setText("message: this.getString(R.string.not_receive_info)");
                }
            } else {
                tvMessage.setText("message: this.getString(R.string.not_receive_info)");
            }
        } else {
            tvMessage.setText("message: this.getString(R.string.not_receive_info)");
        }
    }

}
