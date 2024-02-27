package com.example.mad.otherwidget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.example.mad.R;
import com.example.mad.WelcomeActivity;
import com.example.mad.systeminfos.EncryptingPwd;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLoginEnable();

            }
        }, 1);

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
                                firebaseAuthClass.initLogin(userName, encryptingPwd.decrypt(passwordHash,Splash_Screen.this), Splash_Screen.this, new FirebaseAuthClass.FirestoreCallback() {

                                    @Override
                                    public void onSuccess() {


                                    }

                                    @Override
                                    public void onFailure(Exception error) {
                                        SystemOprations.toGoNewPage(Splash_Screen.this, WelcomeActivity.class);
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

    void toGoWelcomePage() {
        SystemOprations.toGoNewPage(Splash_Screen.this, WelcomeActivity.class);
    }

}