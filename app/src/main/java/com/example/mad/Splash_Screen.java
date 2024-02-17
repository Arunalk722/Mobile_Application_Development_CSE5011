package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              /*  Intent intent = new Intent(Splash_Screen.this,WelcomeActivity.class);
                startActivity(intent);*/
                SystemOprations.toGoNewPage(Splash_Screen.this,WelcomeActivity.class);
                finish();
            }
        },1000);
    }
}