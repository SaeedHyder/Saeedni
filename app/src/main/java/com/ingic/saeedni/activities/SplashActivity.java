package com.ingic.saeedni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ingic.saeedni.R;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {

    final int TIME_INTERVAL_TO_CHECK = 500;// in millis
    final int MIN_TIME_INTERVAL_FOR_SPLASH = 5000; // in millis
    boolean workComplete = false;
    Timer checkWorkTimer;
    Runnable backgroundWork = new Runnable() {

        @Override
        public void run() {

            // This area can be used in Splash to do tasks that do not delay
            // Splash as well as do not extend its time if there processing time
            // is less than splash
            // Check Internet Connection and meet necessary conditions
            // to start the app. E.g. Disk Checks.
            // Check preferences and availability of certain data.
            workComplete = true;
        }

    };
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.RelativeLayout1)
    RelativeLayout RelativeLayout1;
    CountDownTimer mCountDownTimer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        launchTimerAndTask();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void launchTimerAndTask() {
        mCountDownTimer = new CountDownTimer(MIN_TIME_INTERVAL_FOR_SPLASH, TIME_INTERVAL_TO_CHECK) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                progressBar.setProgress((int) i * 100 / (5000 / 1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                progressBar.setProgress(100);
                showMainActivity();
            }
        };
        mCountDownTimer.start();
    }

    private void initNextActivity() {
        checkWorkTimer.cancel();
        showMainActivity();

    }

    private void showMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}