package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        login = (TextView)findViewById(R.id.lblLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(SignInActivity.this,LoginActivity.class);
            }
        });
    }
}