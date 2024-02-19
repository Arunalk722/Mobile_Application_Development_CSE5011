package com.example.mad;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirebaseAuthClass {


    public interface FirestoreCallback {
        void onSuccess();
        void onFailure(Exception error);
    }
    static void intFirebaseFireStore(Map<String, String> mapData, String collectionName, String docName, FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName).document().set(mapData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Call the onSuccess method of the callback
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Call the onFailure method of the callback
                        callback.onFailure(e);
                    }
                });
    }

    static  void initFirebaseAuth(String uN,String pwd,FirestoreCallback callback){
        FirebaseAuth gAuth;
        gAuth = FirebaseAuth.getInstance();

        gAuth.createUserWithEmailAndPassword(uN, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

    static  void initLogin(String userName,String pwd,FirestoreCallback callback){
        FirebaseAuth gAuth = FirebaseAuth.getInstance();
        gAuth.signInWithEmailAndPassword(userName,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
              callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            callback.onFailure(e);
            }
        });
    }

    static  void resetPwd(String txtUserName,FirestoreCallback callback){
        FirebaseAuth gAuth = FirebaseAuth.getInstance();
        gAuth.sendPasswordResetEmail(txtUserName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             callback.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e);
            }
        });
    }

}
