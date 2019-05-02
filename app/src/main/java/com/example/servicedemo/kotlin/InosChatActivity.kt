package com.example.servicedemo.kotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.servicedemo.R
import com.example.servicedemo.kotlin.`class`.CustomMessage
import com.example.servicedemo.kotlin.adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_inos_chat.*
import microsoft.aspnet.signalr.client.Platform
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent
import microsoft.aspnet.signalr.client.hubs.HubConnection
import microsoft.aspnet.signalr.client.hubs.HubProxy
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport
import java.util.concurrent.ExecutionException

class InosChatActivity : AppCompatActivity(), ChatAdapter.ChatListener {
    private var listMessage: ArrayList<CustomMessage> = ArrayList()
    private lateinit var adapter: ChatAdapter

    private lateinit var mHubConnection: HubConnection
    private lateinit var mHubProxy: HubProxy
    private lateinit var mService: MyService
    private lateinit var myIntent: Intent


    override fun onItemClick(position: Int) {
        toast(listMessage[position].Message)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inos_chat)
        val intent = Intent(this, MyService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        adapter = ChatAdapter(listMessage, this, "Thanh")
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter
        adapter.notifyDataSetChanged()
        receiverMessage()
        imgSent.setOnClickListener {
            sendMessage_To("Thanh", edInputMessage.text.toString().trim())
        }

    }

    override fun onResume() {
        super.onResume()
//        receiverMessage()
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private fun receiverMessage() {
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

        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE,
                { name, msg ->
                    runOnUiThread {
                        listMessage.add(CustomMessage(name, msg))
                        adapter.notifyDataSetChanged()
                    }

                }, String::class.java, String::class.java)

    }

    fun sendMessage_To(receiverName: String, message: String) {
        val SERVER_METHOD_SEND_TO = "send"
        mHubProxy.invoke(SERVER_METHOD_SEND_TO, receiverName, message)
    }
}
