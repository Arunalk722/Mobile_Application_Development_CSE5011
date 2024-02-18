package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PwdResetActivity extends AppCompatActivity {

    TextView signIn;
    EditText email,otp,newPwd,newPwdConf;
    Button resetPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_reset);
        signIn = (TextView) findViewById(R.id.lblSignIn);

        resetPwd = (Button) findViewById(R.id.btnResetPWD);

        email = (EditText) findViewById(R.id.txtEmail);
        otp = (EditText) findViewById(R.id.txtOTP);
        newPwd = (EditText) findViewById(R.id.txtNewPWD);
        newPwdConf = (EditText) findViewById(R.id.txtNewPWD2);
        //hi
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(PwdResetActivity.this,LoginActivity.class);
            }
        });
    }
}