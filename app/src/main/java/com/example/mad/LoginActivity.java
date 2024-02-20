package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    TextView sigIn,pwdReset;
    EditText userName,pwd;
    Button login;
    ProgressBar loginProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layoutInit();
        setPwdLinkDisable();
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,SignInActivity.class);
            }
        });
        pwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd(userName.getText().toString().trim());
              //  SystemOprations.toGoNewPage(LoginActivity.this,PwdResetActivity.class);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtPwd = pwd.getText().toString();
                String txtUserName = userName.getText().toString().trim();

                if(!txtUserName.matches(SystemOprations.emailPattern)){
                   // SystemOprations.showMessage("Please provide password", "Password required", LoginActivity.this, 2);

                    userName.setError("please provide valid email");
                    return;
                }
                if(txtPwd.isEmpty()){
                  //  SystemOprations.showMessage("Please provide email address in correct format", "Email format is wrong", LoginActivity.this, 2);
                    pwd.setError("please provide password");
                    return;
                }
                showPrograssBar();
                loginFB(txtPwd,txtUserName);
            }
        });
    }
    void layoutInit(){
        sigIn = (TextView)findViewById(R.id.lblSignIn);
        pwdReset = (TextView)findViewById(R.id.lblPWDReset);
        pwd=(EditText)findViewById(R.id.txtPassword) ;
        userName = (EditText)findViewById(R.id.txtUserName);
        login = (Button)findViewById(R.id.btnLogin);
        loginProgressBar = (ProgressBar)findViewById(R.id.prgBar);
        hidePrograssBar();

    }
    void setPwdLinkDisable(){
        pwdReset.setEnabled(false);
        pwdReset.setTextColor(Color.parseColor("#545352"));
    }
    void setPwdLinkEnable(){
        pwdReset.setEnabled(true);
        pwdReset.setTextColor(getResources().getColor(R.color.darkPink));
    }

    void loginFB(String txtPwd,String txtUserName){
        FirebaseAuthClass.initLogin(txtUserName, txtPwd, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                SystemOprations.toGoNewPage(LoginActivity.this,HomeActivity.class);
                SystemOprations.showMessage("Login successful", "Login successful", LoginActivity.this, 1);
            }

            @Override
            public void onFailure(Exception error) {
                SystemOprations.showMessage(error.getMessage(), "Login failed", LoginActivity.this, 2);

                hidePrograssBar();
                setPwdLinkEnable();
            }
        });

    }
    void resetPwd(String txtUserName){
        if(txtUserName.matches(SystemOprations.emailPattern)){
            showPrograssBar();

            FirebaseAuthClass.resetPwd(txtUserName, new FirebaseAuthClass.FirestoreCallback() {
                @Override
                public void onSuccess() {
                    SystemOprations.showMessage("We will send password reset link to your email.", "password reset", LoginActivity.this, 1);
                }

                @Override
                public void onFailure(Exception error) {
                    hidePrograssBar();
                    SystemOprations.showMessage(error.getMessage(), "password reset failed.", LoginActivity.this, 2);
                }
            });

        }else{
            SystemOprations.showMessage("please check your email address.", "invalid email address", LoginActivity.this, 2);
        }
    }
    void showPrograssBar(){
        loginProgressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        resetError();
    }
    void hidePrograssBar(){
        loginProgressBar.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        resetError();
    }
    void resetError(){
        userName.setError(null);
        pwd.setError(null);
    }

}
