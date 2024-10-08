package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mad.otherwidget.LoginActivity;
import com.example.mad.otherwidget.SignInActivity;
import com.example.mad.systeminfos.SystemOprations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WelcomeActivity extends AppCompatActivity {

    TextView signIn, logIn;
    FloatingActionButton feedbackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        signIn = (TextView) findViewById(R.id.lblSignIn);
        feedbackBtn = (FloatingActionButton)findViewById(R.id.btnFeedBack);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(WelcomeActivity.this, SignInActivity.class);
            }
        });
        logIn = (TextView) findViewById(R.id.lblLogin);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(WelcomeActivity.this, LoginActivity.class);
            }
        });
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(WelcomeActivity.this,FeedBackFormActivity.class);
            }
        });
    }
}//k