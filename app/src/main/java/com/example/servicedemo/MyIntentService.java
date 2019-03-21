package com.example.servicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;


public class MyIntentService extends IntentService {
    public static final String ACTION_1 ="MY_ACTION_1";

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);

        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyIntentService.ACTION_1);
        for (int i = 0; i <= 100; i++) {

            // Sét đặt giá trị cho dữ liệu gửi đi.
            // (Phần trăm của công việc).
            broadcastIntent.putExtra("percel", i);

            // Send broadcast
            // Phát sóng gửi đi.
            sendBroadcast(broadcastIntent);

            // Sleep 100 Milliseconds.
            // Tạm dừng 100 Mili giây.
            SystemClock.sleep(100);
        }
    }
}
