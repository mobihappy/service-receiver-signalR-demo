package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class MyService extends Service {
    private final IBinder iBinder = new LocalBinder();
    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//            }
//        }, 3000, 10000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startSignalR();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        startSignalR();
        return iBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHubConnection.stop();
        Log.d("onDestroy", "destroy");
        Intent intent = new Intent();
        intent.setAction("restartService");
        intent.setClass(this, MyReceiver.class);
        sendBroadcast(intent);
    }

    class LocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }

    private void startSignalR(){
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        String serverUrl = "http://icichtc.ewave.vn:4902";
        mHubConnection = new HubConnection(serverUrl);
        String SERVER_HUB_CHAT = "ChatHub";
        mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);

        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("SimpleSignalR", e.toString());
            return;
        }

        String CLIENT_METHOD_BROADAST_MESSAGE = "broadcastMessage";
        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE,
                new SubscriptionHandler2<String, String>() {
                    @Override
                    public void run(final String name, final String msg) {
                        new NotificationHelper(MyService.this).createNotification(name, msg);
                    }
                }, String.class,String.class);
    }
}
