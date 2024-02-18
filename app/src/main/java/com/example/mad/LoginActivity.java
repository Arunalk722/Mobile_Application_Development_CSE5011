package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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


public class LoginActivity extends AppCompatActivity {
    TextView sigIn,pwdReset;
    EditText userName,pwd;
    Button login;
    ProgressBar loginProgressBar;
    String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    private FirebaseAuth gAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sigIn = (TextView)findViewById(R.id.lblSignIn);
        pwdReset = (TextView)findViewById(R.id.lblPWDReset);
        pwd=(EditText)findViewById(R.id.txtPassword) ;
        userName = (EditText)findViewById(R.id.txtUserName);
        login = (Button)findViewById(R.id.btnLogin);
        loginProgressBar = (ProgressBar)findViewById(R.id.prgBar);

        loginProgressBar.setVisibility(View.INVISIBLE);


        gAuth = FirebaseAuth.getInstance();

        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(LoginActivity.this,SignInActivity.class);
            }
        });
        pwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
              //  SystemOprations.toGoNewPage(LoginActivity.this,PwdResetActivity.class);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtPwd = pwd.getText().toString();
                String txtUserName = userName.getText().toString().trim();

                if(txtPwd.isEmpty()){
                    SystemOprations.showMessage("Please provide password", "Password required", LoginActivity.this, 2);
                    pwd.setError("please provide password");
                    return;
                }
                if(!txtUserName.matches(emailPattern)){
                    SystemOprations.showMessage("Please provide email address in correct format", "Email format is wrong", LoginActivity.this, 2);
                    userName.setError("please provide valid email");
                    return;
                }
                loginProgressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                userName.setError(null);
                pwd.setError(null);
                loginFB();
            }
        });
    }
    void loginFB(){
        String txtPwd = pwd.getText().toString();
        String txtUserName = userName.getText().toString().trim();
    gAuth.signInWithEmailAndPassword(txtUserName,txtPwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            SystemOprations.showMessage("Login successful", "Login successful", LoginActivity.this, 1);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            SystemOprations.showMessage(e.getMessage(), "Login failed", LoginActivity.this, 2);
            loginProgressBar.setVisibility(View.INVISIBLE);
            login.setVisibility(View.VISIBLE);
        }
    });
    }
    void resetPwd(){
        loginProgressBar.setVisibility(View.VISIBLE);
        String txtUserName = userName.getText().toString().trim();
        gAuth.sendPasswordResetEmail(txtUserName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loginProgressBar.setVisibility(View.INVISIBLE);
                SystemOprations.showMessage("We will send password reset link to your email.", "password reset", LoginActivity.this, 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loginProgressBar.setVisibility(View.INVISIBLE);
                SystemOprations.showMessage(e.getMessage(), "password reset failed.", LoginActivity.this, 1);
            }
        });
    }
}
