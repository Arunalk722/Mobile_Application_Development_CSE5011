package com.example.mad.systeminfos;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mad.systeminfos.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirebaseAuthClass {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface FirestoreCallback {
        void onSuccess();

        void onFailure(Exception error);
    }

    public void saveToFireStore(Map<String, Object> mapData, String collectionName, String docName, FirestoreCallback callback) {
        try {

            db.collection(collectionName).document(docName).set(mapData)
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateFirebaseFirestore(Map<String, Object> updateData, String collectionName, String docName, FirestoreCallback callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(collectionName).document(docName).update(updateData)
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void deleteFirebaseFirestore(Map<String, Object> updateData, String collectionName, String docName, FirestoreCallback callback) {
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(collectionName).document(docName).delete()
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void initFirebaseAuth(String uN, String pwd, FirestoreCallback callback) {
        try {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void initLogin(String userName, String pwd, Context context, FirestoreCallback callback) {
        try {
            FirebaseAuth gAuth = FirebaseAuth.getInstance();
            gAuth.signInWithEmailAndPassword(userName, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    callback.onSuccess();
                    UserInfo userInfo = new UserInfo();
                    userInfo.getUserInfo(userName, context);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void resetPwd(String txtUserName, FirestoreCallback callback) {
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getNextId(String collectionName, NextIdCallback callback) {
        try {
            db.collection(collectionName).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int nextId = queryDocumentSnapshots.size() + 1; // Increment the count by one for the next ID
                            callback.onNextId(nextId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onError(e);
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public interface NextIdCallback {
        void onNextId(int nextId);

        void onError(Exception e);
    }

    public void scanFromFirestore(String documentID, String collectionName, ScanProductCallback callback) {
        try {
            db.collection(collectionName).document(documentID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        callback.onScanProductSuccess(documentSnapshot);
                    } else {
                        callback.onScanProductFailure(new Exception("Document does not exist"));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onScanProductFailure(e);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public interface ScanProductCallback {
        void onScanProductSuccess(DocumentSnapshot documentSnapshot);

        void onScanProductFailure(Exception e);
    }
}
