package com.example.mad.otherwidget;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mad.R;
import com.example.mad.systeminfos.EncryptingPwd;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.example.mad.systeminfos.UserInfo;

import java.util.HashMap;
import java.util.Map;



public class LoginActivity extends AppCompatActivity {
    TextView lblSigIn,lblPwdReset;
    EditText userNametxt,pwdTxt;
    Button loginBtn;
    ProgressBar loginProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layoutInit();
        setPwdLinkDisable();
        lblSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,SignInActivity.class);
            }
        });
        lblPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd(userNametxt.getText().toString().trim());

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtPwd = pwdTxt.getText().toString();
                String txtUserName = userNametxt.getText().toString().trim();
                if(!isValidEmail(txtUserName)){
                    userNametxt.setError("please provide valid email");
                    return;
                }
                if(txtPwd.isEmpty()){
                    pwdTxt.setError("please provide password");
                    return;
                }

                loginUsingFB(txtPwd,txtUserName);
            }
        });
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    void layoutInit(){
        lblSigIn = (TextView)findViewById(R.id.lblSignIn);
        lblPwdReset = (TextView)findViewById(R.id.lblPWDReset);
        pwdTxt=(EditText)findViewById(R.id.txtPassword) ;
        userNametxt = (EditText)findViewById(R.id.txtUserName);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        loginProgressBar = (ProgressBar)findViewById(R.id.prgBar);
        hidePrograssBar();

    }
    void setPwdLinkDisable(){
        lblPwdReset.setEnabled(false);
        lblPwdReset.setTextColor(Color.parseColor("#545352"));
    }
    void setPwdLinkEnable(){
        lblPwdReset.setEnabled(true);
        lblPwdReset.setTextColor(getResources().getColor(R.color.darkPink));
    }
    void loginUsingFB(String txtPwd,String txtUserName){
        showPrograssBar();
        FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
        firebaseAuthClass.initLogin(txtUserName, txtPwd,LoginActivity.this, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                autoLoginEnable(txtUserName,txtPwd);
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
        if(!isValidEmail(txtUserName)){
            showPrograssBar();

            FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
           firebaseAuthClass.resetPwd(txtUserName, new FirebaseAuthClass.FirestoreCallback() {
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
        loginBtn.setVisibility(View.INVISIBLE);
        resetError();
    }
    void hidePrograssBar(){
        loginProgressBar.setVisibility(View.INVISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
        resetError();
    }
    void resetError(){
        userNametxt.setError(null);
        pwdTxt.setError(null);
    }
    void autoLoginEnable(String userName,String pwd){
        EncryptingPwd encryptingPwd = new EncryptingPwd();
         Map<String,Object> listAutoLogin = new HashMap<>();
        listAutoLogin.put("email", userName.toString());
        listAutoLogin.put("password", encryptingPwd.encrypt(pwd.toString()));
        listAutoLogin.put("isAutoLogin", true);
        listAutoLogin.put("RegDate", SystemOprations.curretDate());

        FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
        firebaseAuthClass.saveToFireStore(listAutoLogin, "Auto_Login", Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID), new FirebaseAuthClass.FirestoreCallback() {
           @Override
           public void onSuccess() {
               UserInfo userInfo=new UserInfo();
               userInfo.getUserInfo(userName,LoginActivity.this);
           }

           @Override
           public void onFailure(Exception error) {

           }
       });
    }
}
