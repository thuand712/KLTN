package ducthuan.com.lamdep.Service;

import ducthuan.com.lamdep.Activity.TrangChuActivity;

public class APIService {

    public static final String rs_url = "http://192.168.1.5:8080/";
    public static final String fcm_url = "https://fcm.googleapis.com/";


    public static DataService getService(){
        //create: khoi tao nhung phuong thuc ben dataservice de gui len server
        return APIRetrofitClient.getClient(TrangChuActivity.base_url).create(DataService.class);
    }

    public static DataService getRSService(){
        //create: khoi tao nhung phuong thuc ben dataservice de gui len server
        return APIRetrofitClient.getClient(rs_url).create(DataService.class);
    }

    public static DataService getFCMService(){
        //create: khoi tao nhung phuong thuc ben dataservice de gui len server
        return APIRetrofitClient.getClient(fcm_url).create(DataService.class);
    }


}