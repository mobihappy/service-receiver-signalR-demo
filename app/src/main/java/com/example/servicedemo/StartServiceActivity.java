package com.example.servicedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
    }

    public void playSong(View view){
        Intent intent = new Intent(StartServiceActivity.this, StartSongService.class);
        this.startService(intent);
    }

    public void stopSong(View view){
        Intent intent = new Intent(StartServiceActivity.this, StartSongService.class);
        this.stopService(intent);
    }
}
