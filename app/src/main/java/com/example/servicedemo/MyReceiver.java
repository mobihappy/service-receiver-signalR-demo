package com.example.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wiFiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isConnected = wiFiInfo != null &&
                wiFiInfo.isConnectedOrConnecting() || mobileInfo != null && mobileInfo.isConnectedOrConnecting();

        if (context instanceof MainActivity) {
            ConnectionListener listener = (MainActivity) context;
            if (isConnected) {
                listener.showNoInternet(true);
            } else {
                listener.showNoInternet(false);
            }
        }else {
            if (!isConnected){
//                Toast.makeText(context, "Mất kết nối mạng", Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface ConnectionListener{
        void showNoInternet(boolean isConnect);
    }
}
