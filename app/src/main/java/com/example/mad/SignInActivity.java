package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    TextView login;
    EditText userName,pwd1,pwd2,phoneNo;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = (TextView)findViewById(R.id.lblLogin);

        userName = (EditText)findViewById(R.id.txtUserName);
        pwd1 = (EditText)findViewById(R.id.txtPassword);
        pwd2 = (EditText)findViewById(R.id.txtPasswordConf);
        phoneNo = (EditText)findViewById(R.id.txtPhoneNo);


        login = (Button)findViewById(R.id.btnRegister);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(SignInActivity.this,LoginActivity.class);
            }
        });
    }
}