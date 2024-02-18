package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    TextView login;
    EditText userName, pwd1, pwd2, phoneNo;
    Button reg;
    ProgressBar signInProgressBar;
    String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    private FirebaseAuth gAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = (TextView) findViewById(R.id.lblLogin);

        userName = (EditText) findViewById(R.id.txtUserName);
        pwd1 = (EditText) findViewById(R.id.txtPassword);
        pwd2 = (EditText) findViewById(R.id.txtPasswordConf);
        phoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        reg = (Button) findViewById(R.id.btnRegister);

        signInProgressBar = (ProgressBar) findViewById(R.id.prgBar);
        signInProgressBar.setVisibility(View.INVISIBLE);

        gAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(SignInActivity.this, LoginActivity.class);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUserName = userName.getText().toString().trim();
                String txtPassword = pwd1.getText().toString();
                String txtPasswordConf = pwd2.getText().toString();
                String txtPhoneNo = phoneNo.getText().toString().trim();

                if (txtUserName.isEmpty()) {
                    SystemOprations.showMessage("Please provide email address", "User name required", SignInActivity.this, 2);
                    userName.setError("User name required");
                    return;
                }

                if (!txtUserName.matches(emailPattern)) {
                    SystemOprations.showMessage("Please provide email address in correct format", "Email format is wrong", SignInActivity.this, 2);
                    userName.setError("Email format is wrong");
                    return;
                }

                if (txtPassword.isEmpty() || txtPasswordConf.isEmpty()) {
                    SystemOprations.showMessage("Please provide password and confirm password", "Password required", SignInActivity.this, 2);
                    pwd1.setError("Password required");
                    pwd2.setError("Password required");
                    return;
                }

                if (!txtPassword.equals(txtPasswordConf)) {
                    SystemOprations.showMessage("Passwords do not match", "Password mismatch", SignInActivity.this, 2);
                    pwd1.setError("Password mismatch");
                    pwd2.setError("Password mismatch");
                    return;
                }

                if (txtPhoneNo.isEmpty() || txtPhoneNo.length() != 10) {
                    phoneNo.setError("Invalid phone number");
                    SystemOprations.showMessage("Please provide a 10-digit phone number", "Invalid phone number", SignInActivity.this, 2);
                    return;
                }
                signInProgressBar.setVisibility(View.VISIBLE);
                reg.setVisibility(View.INVISIBLE);
                signUpGoogle();
            }
        });


    }

    void signUpGoogle() {

        String txtUserName = userName.getText().toString().trim();
        String txtPassword = pwd1.getText().toString();
        String txtPasswordConf = pwd2.getText().toString();
        String txtPhoneNo = phoneNo.getText().toString().trim();


        gAuth.createUserWithEmailAndPassword(txtUserName, txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                userAdd();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SystemOprations.showMessage(txtUserName + e.getMessage(), "Sign in failed", SignInActivity.this, 2);
                signInProgressBar.setVisibility(View.INVISIBLE);
                reg.setVisibility(View.VISIBLE);
            }
        });
        pwd1.setError(null);
        pwd2.setError(null);
    }

    void userAdd() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String txtUserName = userName.getText().toString().trim();
        String txtPhone = phoneNo.getText().toString();
        Map<String, String> userList = new HashMap<>();
        userList.put("RegDate", SystemOprations.curretDate());
        userList.put("androidID", txtUserName.toString());
        userList.put("email", txtUserName.toString());
        userList.put("phoneNo", txtPhone.toString());
        db.collection("userList").document().set(userList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                SystemOprations.showMessage("Sign in Successful using " + txtUserName, "Sign in Successful", SignInActivity.this, 1);
                SystemOprations.toGoNewPage(SignInActivity.this,LoginActivity.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SystemOprations.showMessage(txtUserName + e.getMessage(), "Sign in failed", SignInActivity.this, 2);
            }
        });

    }
}