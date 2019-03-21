package com.example.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wiFiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isConnected = wiFiInfo != null &&
                wiFiInfo.isConnectedOrConnecting() || mobileInfo != null && mobileInfo.isConnectedOrConnecting();

        ConnectionListener listener = (MainActivity) context;
        if (isConnected) {
            listener.showNoInternet(true);
        } else {
            listener.showNoInternet(false);
        }

        if (isCharging && usbCharge){
            listener.showUsbConnect("usb connect charging");
        }else if (isCharging && acCharge){
            listener.showUsbConnect("ac connect charging");
        }else {
            listener.showUsbConnect("not charging");
        }


    }

    public interface ConnectionListener{
        void showNoInternet(boolean isConnect);
        void showUsbConnect(String status);
    }
}
