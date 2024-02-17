package com.example.mad;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

public class SystemOprations {
    public  static  void toGoNewPage(Context context, Class<?>newPage){
        Intent intent = new Intent(context, newPage);
        context.startActivity(intent);
    }
}
