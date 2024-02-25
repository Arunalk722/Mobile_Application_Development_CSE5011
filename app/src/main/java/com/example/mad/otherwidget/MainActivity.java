package com.example.mad.otherwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mad.R;
import com.example.mad.systeminfos.SystemOprations;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemOprations.toGoNewPage(this,Splash_Screen.class);


    }
}