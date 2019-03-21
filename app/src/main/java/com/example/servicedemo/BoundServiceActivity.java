package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BoundServiceActivity extends AppCompatActivity {
    private boolean binded=false;
    private BoundService boundService;
    private TextView weatherText;
    private EditText locationText;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.LocalWeatherBinder binder = (BoundService.LocalWeatherBinder)iBinder;
            boundService = binder.getService();
            binded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);

        weatherText = (TextView) this.findViewById(R.id.tv_weather);
        locationText = (EditText)this.findViewById(R.id.tv_input_location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,BoundService.class);
        this.bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (binded){
            this.unbindService(connection);
            binded = false;
        }
    }

    public void showWeather(View view){
        String location = locationText.getText().toString();
        String weather= this.boundService.getWeatherToday(location);

        weatherText.setText(weather);
    }
}
