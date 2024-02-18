package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    TextView sigIn,pwdReset;
    EditText userName,pwd;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sigIn = (TextView)findViewById(R.id.lblSignIn);
        pwdReset = (TextView)findViewById(R.id.lblPWDReset);
        pwd=(EditText)findViewById(R.id.txtPassword) ;
        userName = (EditText)findViewById(R.id.txtUserName);
        login = (Button)findViewById(R.id.btnLogin);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,SignInActivity.class);
            }
        });
        pwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,PwdResetActivity.class);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pwd.getText().toString().equals("admin")&&userName.getText().toString().equals("admin")){

                    SystemOprations.showMessage("Login Successful","Login Successful",LoginActivity.this,1);
                }
                else{
                    SystemOprations.showMessage("Please check your username and password","User Login failed",LoginActivity.this,2);
                }
            }
        });

    }

}
