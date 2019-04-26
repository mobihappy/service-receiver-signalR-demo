package com.example.servicedemo.kotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.servicedemo.R
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    private lateinit var mService:MyService
    private lateinit var myIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btStartService.setOnClickListener {
            myIntent = Intent(this, MyService::class.java)
            startService(myIntent)
        }

        btBindService.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(myIntent)
    }
}
