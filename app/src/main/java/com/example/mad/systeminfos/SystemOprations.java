package com.example.mad.systeminfos;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.example.mad.R;
import com.example.mad.otherwidget.Splash_Screen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SystemOprations {

    public  static  void toGoNewPage(Context context, Class<?>newPage){
        Intent intent = new Intent(context, newPage);
        context.startActivity(intent);
    }
    public static void showMessage(String message,String title,Context context,int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        //int 1 == success
        //int 2 == failed
        // int 3 == error
        if(type==1){
            builder.setIcon(R.drawable.c);
        }
        if(type==2){
            builder.setIcon(R.drawable.f);
        }
        if(type==3){
            builder.setIcon(R.drawable.q);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public  static  String curretDate(){
        LocalDateTime now = null;
        String dateTime="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = now.format(formatter);
        }
        return  dateTime;
    }
    public static String makeGUID(){
        return  String.valueOf(UUID.randomUUID());
    }

    public static void ynDialog(String message, String title, Context context, dialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(R.drawable.q);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click
                if (callback != null) {
                    callback.onPositiveButtonClicked();
                }
                dialog.dismiss();
            }
        });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                if (callback != null) {
                    callback.onNegativeButtonClicked();
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void ynDialogSuccess(String message, String title, Context context, dialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setIcon(R.drawable.c);
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click
                if (callback != null) {
                    callback.onPositiveButtonClicked();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle Cancel button click
                if (callback != null) {
                    callback.onNegativeButtonClicked();
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public interface dialogCallback {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }
    public static String getDeviceID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
