package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    TextView sigIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sigIn = (TextView)findViewById(R.id.lblSignIn);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,SignInActivity.class);
            }
        });
    }

}