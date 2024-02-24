package com.example.mad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  autoLoginEnable();
            ///    SystemOprations.toGoNewPage(Splash_Screen.this,HomeActivity.class);
             //   finish();
            //    getUserInfo("arunalk722@hotmail.com");

            }
        }, 5);
    }

    public void autoLoginEnable() {
        String deviceId = Settings.Secure.getString(Splash_Screen.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Auto_Login")
                .document(deviceId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("email");
                            String passwordHash = documentSnapshot.getString("password");
                            boolean isAutoLogin = documentSnapshot.getBoolean("isAutoLogin");
                            if (isAutoLogin == true) {
                                EncryptingPwd encryptingPwd = new EncryptingPwd();
                                FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
                               firebaseAuthClass.initLogin(userName, encryptingPwd.decrypt(passwordHash),Splash_Screen.this, new FirebaseAuthClass.FirestoreCallback() {

                                            @Override
                                            public void onSuccess() {


                                            }

                                            @Override
                                            public void onFailure(Exception error) {
                                                SystemOprations.toGoNewPage(Splash_Screen.this,WelcomeActivity.class);
                                            }
                                        });


                            } else {
                                toGoWelcomePage();

                            }

                        } else {
                            toGoWelcomePage();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
    void toGoWelcomePage(){
        SystemOprations.toGoNewPage(Splash_Screen.this,WelcomeActivity.class);
    }

   /* void getUserInfo(String Name){
        UserInfo userInfo = new UserInfo();
        FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
        firebaseAuthClass.scanFromFirestore(Name,"User_List", new FirebaseAuthClass.ScanProductCallback() {
            @Override
            public void onScanProductSuccess(DocumentSnapshot documentSnapshot) {
                userInfo.setUserName(documentSnapshot.getString("email").toString());
                userInfo.setUserType(documentSnapshot.getString("UserTypeIs").toString());
                userInfo.setAddress(documentSnapshot.getString("address").toString());
                userInfo. setPhoneNo(documentSnapshot.getString("phoneNo").toString());
                userInfo. setOrderGUID(SystemOprations.makeGUID());
                     SystemOprations.showMessage(userInfo.getOrderGUID(), "error on product saving", Splash_Screen.this, 3);

            }

            @Override
            public void onScanProductFailure(Exception e) {
                SystemOprations.showMessage(e.toString(), "error on product saving", Splash_Screen.this, 3);
            }
        });
    }*/
}