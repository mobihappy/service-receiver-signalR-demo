package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BoundService extends Service {
    private static String LOG_TAG = "BoundService";
    private static final Map<String,String> weatherData = new HashMap<>();
    private final IBinder binder = new LocalWeatherBinder();

    public class LocalWeatherBinder extends Binder {

        public BoundService getService()  {
            return BoundService.this;
        }
    }
    public BoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return this.binder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getWeatherToday(String location) {
        Date now= new Date();
        DateFormat df= new SimpleDateFormat("dd-MM-yyyy");

        String dayString = df.format(now);
        String keyLocAndDay = location + "$"+ dayString;

        String weather=  weatherData.get(keyLocAndDay);
        //
        if(weather != null)  {
            return weather;
        }

        //
        String[] weathers = new String[]{"Rainy", "Hot", "Cool", "Warm" ,"Snowy"};

        // Giá trị ngẫu nhiên từ 0 tới 4
        int i= new Random().nextInt(5);

        weather =weathers[i];
        weatherData.put(keyLocAndDay, weather);
        //
        return weather;
    }
}
