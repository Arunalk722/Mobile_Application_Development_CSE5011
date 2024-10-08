package com.example.mad.otherwidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad.R;
import com.example.mad.makeorder.ListOfProductActivity;
import com.example.mad.orderlist.ViewAllOrdersActivity;
import com.example.mad.orderlist.ViewOrderActivity;
import com.example.mad.productmanage.ProductMngActivity;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.example.mad.systeminfos.UserInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    CardView pmCrd,lpCrd,loCard;
    ImageButton productManageBtn,listProductBtn,listOrderBtn,listAllOrderbtn;
    TextView loginUserLbl,userTypeLbl;
    FloatingActionButton  logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uiInit();

    }

    void uiInit(){
        pmCrd = findViewById(R.id.crdProductManage);
        lpCrd =findViewById(R.id.crdListProduct);
        loCard =findViewById(R.id.crdListOrder);
        logoutBtn = findViewById(R.id.btnlogOut);
        productManageBtn=findViewById(R.id.btnProductManage);
        listProductBtn=findViewById(R.id.btnListProduct);
        listOrderBtn=findViewById(R.id.btnListOrder);
        listAllOrderbtn=findViewById(R.id.btnListAllOrder);
        if(UserInfo.isAdmin()==true){
            pmCrd.setVisibility(View.VISIBLE);
        }else{
            pmCrd.setVisibility(View.INVISIBLE);
        }
        loginUserLbl = findViewById(R.id.lblUserName);
        loginUserLbl.setText("Login :"+UserInfo.getUserName());
        userTypeLbl = findViewById(R.id.lblUserType);
        if(UserInfo.isAdmin()==true){
            userTypeLbl.setText("User Type :Super User");
        }else{
            userTypeLbl.setText("User Type :Member");
        }
        productManageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this, ProductMngActivity.class);
            }
        });
        listProductBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this, ListOfProductActivity.class);
            }
        });

        listOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this, ViewOrderActivity.class);
            }
        });
        listAllOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this, ViewAllOrdersActivity.class);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SystemOprations.ynDialog("Would you like to log out from the system? If you do, the auto-login functionality will be disabled until you successfully log in again", "Waring", HomeActivity.this, new SystemOprations.dialogCallback() {
                   @Override
                   public void onPositiveButtonClicked() {
                       FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
                       firebaseAuthClass.deleteFirebaseFirestore("Auto_Login", SystemOprations.getDeviceID(HomeActivity.this).toString(), new FirebaseAuthClass.FirestoreCallback() {
                           @Override
                           public void onSuccess() {
                               Toast.makeText(HomeActivity.this,"Successfully logout", Toast.LENGTH_SHORT).show();
                               SystemOprations.toGoNewPage(HomeActivity.this,Splash_Screen.class);
                           }

                           @Override
                           public void onFailure(Exception error) {
                               Toast.makeText(HomeActivity.this,"logout error "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   }

                   @Override
                   public void onNegativeButtonClicked() {
                       Toast.makeText(HomeActivity.this,"Logout not confirm", Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });
    }
}