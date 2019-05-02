package com.example.servicedemo.kotlin

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import microsoft.aspnet.signalr.client.Platform
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent
import microsoft.aspnet.signalr.client.hubs.HubConnection
import microsoft.aspnet.signalr.client.hubs.HubProxy
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport
import java.util.concurrent.ExecutionException


class MyService : Service() {
    private lateinit var mHubConnection: HubConnection
    private lateinit var mHubProxy: HubProxy
    private val mBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startSignalR()
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        startSignalR()
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        mHubConnection.stop()

        val intent = Intent()
        intent.setAction("restartService")
        intent.setClass(this, MyReceiver::class.java)
        sendBroadcast(intent)
    }

    fun sendMessage_To(receiverName: String, message: String) {
        val SERVER_METHOD_SEND_TO = "send"
        mHubProxy.invoke(SERVER_METHOD_SEND_TO, receiverName, message)
    }

    private fun startSignalR() {
        Platform.loadPlatformComponent(AndroidPlatformComponent())
        val serverUrl = "http://icichtc.ewave.vn:4902"
        mHubConnection = HubConnection(serverUrl)
        val SERVER_HUB_CHAT = "ChatHub"
        mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT)
        val clientTransport = ServerSentEventsTransport(mHubConnection.logger)
        val signalRFuture = mHubConnection.start(clientTransport)
        try {
            signalRFuture.get()
        } catch (e: ExecutionException) {
            Log.d("SimpleSignalR", e.toString())
            return
        }

        val CLIENT_METHOD_BROADAST_MESSAGE = "broadcastMessage"

//        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE,
//                { name, msg ->  }, String::class.java, String::class.java)

        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE,
                { name, msg -> NotificationHelper(this@MyService).CreateNotification(name, msg) }, String::class.java, String::class.java)

    }

    inner class LocalBinder : Binder() {
        fun getService():MyService {
            return this@MyService
        }
    }
}
