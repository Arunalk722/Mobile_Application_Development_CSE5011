package com.example.mad.systeminfos;
import android.content.Context;
import android.widget.Toast;

import com.example.mad.WelcomeActivity;
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
        try {

            firebaseAuthClass.scanFromFirestore(Name, "User_List", new FirebaseAuthClass.ScanProductCallback() {
                @Override
                public void onScanProductSuccess(DocumentSnapshot documentSnapshot) {
                    setUserName(documentSnapshot.getString("Email").toString());
                    setUserType(documentSnapshot.getString("UserTypeIs").toString());
                    setAddress(documentSnapshot.getString("Address").toString());
                    setPhoneNo(documentSnapshot.getString("PhoneNo").toString());
                    setOrderGUID(SystemOprations.makeGUID());
                    SystemOprations.toGoNewPage(context, HomeActivity.class);
                }

                @Override
                public void onScanProductFailure(Exception e) {
                    //SystemOprations.showMessage(e.toString(), "Login error", context, 1);
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    SystemOprations.toGoNewPage(context, WelcomeActivity.class);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }


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

    public static boolean isAdmin() {
        if (userType.equals("A") || userType.equals("a")) {
            return true;
        } else {
            return false;
        }
    }
}
