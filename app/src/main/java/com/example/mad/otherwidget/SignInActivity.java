package com.example.mad.otherwidget;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad.R;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    TextView login;
    EditText userName, pwd1, pwd2, phoneNo, address;
    Button reg;
    ProgressBar signInProgressBar;
    String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUIWidget();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(SignInActivity.this, LoginActivity.class);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("Would you like to register Sprinkles Bakery application", "Sign in", SignInActivity.this, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        visibleProBar();
                        String txtUserName = userName.getText().toString().trim().toLowerCase();
                        String txtPassword = pwd1.getText().toString();
                        String txtPasswordConf = pwd2.getText().toString();
                        String txtPhoneNo = phoneNo.getText().toString().trim();
                        String txtAddress = address.getText().toString().trim();
                        if (txtUserName.isEmpty()) {
                            SystemOprations.showMessage("Please provide email address", "User name required", SignInActivity.this, 2);
                            userName.setError("User name required");
                            hideProBar();
                            return;
                        }

                        if (!txtUserName.matches(emailPattern)) {
                            SystemOprations.showMessage("Please provide email address in correct format", "Email format is wrong", SignInActivity.this, 2);
                            userName.setError("user name is not valid email format");
                            hideProBar();
                            return;
                        }

                        if (txtPassword.isEmpty() || txtPasswordConf.isEmpty()) {
                            SystemOprations.showMessage("Please provide password and confirm password", "Password required", SignInActivity.this, 2);
                            pwd1.setError("Password required");
                            pwd2.setError("Password required");
                            hideProBar();
                            return;
                        }

                        if (!txtPassword.equals(txtPasswordConf)) {
                            SystemOprations.showMessage("Passwords do not match", "Password mismatch", SignInActivity.this, 2);
                            pwd1.setError("Password mismatch");
                            pwd2.setError("Password mismatch");
                            hideProBar();
                            return;
                        }

                        if (txtPhoneNo.isEmpty() || txtPhoneNo.length() != 10) {
                            phoneNo.setError("Invalid phone number");
                            SystemOprations.showMessage("Please provide a 10-digit phone number", "Invalid phone number", SignInActivity.this, 2);
                            hideProBar();
                            return;
                        }
                        if (txtAddress.isEmpty()) {
                            address.setError("Address required");
                            SystemOprations.showMessage("Please provide a Address", "Address required", SignInActivity.this, 2);
                            hideProBar();
                            return;
                        }
                        signUpGoogle(txtUserName, txtPassword, txtPhoneNo, txtAddress);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(SignInActivity.this, "not confirm", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    void initUIWidget() {
        login = (TextView) findViewById(R.id.lblLogin);

        userName = (EditText) findViewById(R.id.txtUserName);
        pwd1 = (EditText) findViewById(R.id.txtPassword);
        pwd2 = (EditText) findViewById(R.id.txtPasswordConf);
        phoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        address = (EditText) findViewById(R.id.txtAddress);

        reg = (Button) findViewById(R.id.btnRegister);

        signInProgressBar = (ProgressBar) findViewById(R.id.prgBar);

        hideProBar();
    }

    void signUpGoogle(String uN, String pwd, String phone, String address) {
        try {
            FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
            firebaseAuthClass.initFirebaseAuth(uN, pwd, new FirebaseAuthClass.FirestoreCallback() {
                @Override
                public void onSuccess() {
                    firebaseDB(uN, phone, address);
                    resetError();
                }

                @Override
                public void onFailure(Exception error) {
                    SystemOprations.showMessage(uN + error.getMessage(), "Sign-In failed", SignInActivity.this, 2);
                    hideProBar();
                }
            });

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void resetError() {
        pwd1.setError(null);
        pwd2.setError(null);
        userName.setError(null);
        phoneNo.setError(null);
    }

    void firebaseDB(String uN, String phoneNo, String address) {
        try {

            Map<String, Object> userList = new HashMap<>();
            userList.put("Email", uN.toString());
            userList.put("PhoneNo", phoneNo.toString());
            userList.put("IsLogin", true);
            userList.put("Address", address.toString());
            userList.put("UserTypeIs", "M");
            userList.put("RegDate", SystemOprations.curretDate());

            FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
            firebaseAuthClass.saveToFireStore(userList, "User_List", uN.toString().toString(), new FirebaseAuthClass.FirestoreCallback() {
                @Override
                public void onSuccess() {
                  //  SystemOprations.showMessage("Sign in Successful using " + uN, "Sign in Successful", SignInActivity.this, 1);
                   SystemOprations.ynDialogSuccess("Sign in Successful using " + uN + "\nWould you like to go to login page", "Sign in Successful", SignInActivity.this, new SystemOprations.dialogCallback() {
                       @Override
                       public void onPositiveButtonClicked() {
                           hideProBar();
                           SystemOprations.toGoNewPage(SignInActivity.this, LoginActivity.class);
                       }

                       @Override
                       public void onNegativeButtonClicked() {

                       }
                   });

                }

                @Override
                public void onFailure(Exception e) {
                    SystemOprations.showMessage(uN + e.getMessage(), "Sign in failed", SignInActivity.this, 2);
                    hideProBar();

                }
            });

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void visibleProBar() {
        signInProgressBar.setVisibility(View.VISIBLE);
        reg.setVisibility(View.INVISIBLE);
    }

    void hideProBar() {
        signInProgressBar.setVisibility(View.INVISIBLE);
        reg.setVisibility(View.VISIBLE);
    }
}