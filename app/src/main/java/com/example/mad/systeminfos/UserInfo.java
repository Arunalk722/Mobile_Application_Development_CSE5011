package com.example.mad.systeminfos;
import android.content.Context;

import com.example.mad.otherwidget.HomeActivity;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserInfo {
    private static String userName = null;
    private static String userType = null;
    private static String address = null;
    private static String phoneNo = null;
    private static String orderID = null;

    public void getUserInfo(String Name, Context context) {
        FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
        firebaseAuthClass.scanFromFirestore(Name, "User_List", new FirebaseAuthClass.ScanProductCallback() {
            @Override
            public void onScanProductSuccess(DocumentSnapshot documentSnapshot) {
                setUserName(documentSnapshot.getString("email").toString());
                setUserType(documentSnapshot.getString("UserTypeIs").toString());
                setAddress(documentSnapshot.getString("address").toString());
                setPhoneNo(documentSnapshot.getString("phoneNo").toString());
                setOrderGUID(SystemOprations.makeGUID());
                SystemOprations.toGoNewPage(context, HomeActivity.class);
            }
            @Override
            public void onScanProductFailure(Exception e) {
                SystemOprations.showMessage(e.toString(), "Login error", context, 1);
            }
        });
    }

    public static void setOrderGUID(String orderGUID) {
        UserInfo.orderID = orderGUID;
    }

    public static String getOrderGUID() {
        return orderID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        UserInfo.userType = userType;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        UserInfo.address = address;
    }

    public static String getPhoneNo() {
        return phoneNo;
    }

    public static void setPhoneNo(String phoneNo) {
        UserInfo.phoneNo = phoneNo;
    }
}
