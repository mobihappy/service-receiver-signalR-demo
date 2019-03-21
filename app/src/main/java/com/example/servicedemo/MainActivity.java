package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyReceiver.ConnectionListener {
    private MyService mService;
    private MyReceiver myReceiver;
    private Intent intent;
    private TextView tvNoInternet;
    private TextView tvUsbConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNoInternet = findViewById(R.id.tvNoInternet);
        tvUsbConnect = findViewById(R.id.tvUsbConnect);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startService(View view){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });
    }

    public void bindService(View view){
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void phone(View view){
        Intent intent = new Intent(this,PhoneActivity.class);
        startActivity(intent);
    }

    public void startClick(View view){
        Intent intent = new Intent(this,StartServiceActivity.class);
        startActivity(intent);
    }

    public void boundClick(View view){
        Intent intent = new Intent(this,BoundServiceActivity.class);
        startActivity(intent);
    }

    public void intentClick(View view){
        Intent intent = new Intent(this,IntentServiceActivity.class);
        startActivity(intent);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
        unregisterReceiver(myReceiver);
    }

    @Override
    public void showNoInternet(boolean isConnect) {
        if (isConnect){
            tvNoInternet.setVisibility(View.GONE);
        }else {
            tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showUsbConnect(String status) {
        tvUsbConnect.setVisibility(View.VISIBLE);
        tvUsbConnect.setText(status);
    }
}
