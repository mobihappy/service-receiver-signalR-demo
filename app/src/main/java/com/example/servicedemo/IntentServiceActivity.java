package com.example.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IntentServiceActivity extends AppCompatActivity {
    private Button startButton;
    private Button stopButton;
    private TextView percelText;

    private ProgressBar progressBar;

    private Intent serviceIntent;

    private ResponseReceiver receiver = new ResponseReceiver();

    public class ResponseReceiver extends BroadcastReceiver {

        // on broadcast received
        @Override
        public void onReceive(Context context, Intent intent) {

            // Kiểm tra nhiệm vụ của Intent gửi đến.
            if(intent.getAction().equals(MyIntentService.ACTION_1)) {
                int value = intent.getIntExtra("percel", -1);

                new ShowProgressBarTask().execute(value);
            }
        }
    }

    class ShowProgressBarTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... args) {

            return args[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            progressBar.setProgress(result);

            percelText.setText(result + " % Loaded");

            if (result == 100) {
                percelText.setText("Completed");
                startButton.setEnabled(true);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        this.startButton= (Button) this.findViewById(R.id.button_start);
        this.stopButton = (Button)this.findViewById(R.id.button_stop);
        this.percelText = (TextView) this.findViewById(R.id.text_percel);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                MyIntentService.ACTION_1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public void startButtonClicked(View view)  {
        startButton.setEnabled(false);

        // Intent yêu cầu gửi đến Service.
        serviceIntent = new Intent(this, MyIntentService.class);

        // Chạy dịch vụ.
        startService(serviceIntent);
    }


    public void stopButtonClicked(View view)  {
        if(serviceIntent!= null)  {
            // serviceIntent.get
        }
    }
}
