package com.cconmausa.cconmausa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by ymg on 2016-07-14.
 */
public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static boolean possible =true;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        int result=0;
        try{
if(Build.VERSION.SDK_INT>16){
    result = Settings.Global.getInt(context.getContentResolver(),Settings.Global.AIRPLANE_MODE_ON);
}else{
    result =Settings.System.getInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON);
}
        }catch (Exception e){

        }
        if(result!=0){
            status = "비행기모드";
            possible = false;
        }
        else if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
            possible = true;
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
            possible = true;
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
            possible = false;
        }
        return status;
    }
}
