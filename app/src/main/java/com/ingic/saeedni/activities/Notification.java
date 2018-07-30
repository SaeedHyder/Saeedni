package com.ingic.saeedni.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ingic.saeedni.R;

public class Notification extends AppCompatActivity {

    public boolean isNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                isNotification = getIntent().getExtras().getBoolean("tapped");

                Intent myIntent = new Intent(Notification.this, MainActivity.class);
                myIntent.putExtra("mystring","tapped");
                startActivity(myIntent);
            }
        }
    }
}
