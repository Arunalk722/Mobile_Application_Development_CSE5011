package com.example.mad.otherwidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mad.R;
import com.example.mad.makeorder.ListOfProductActivity;
import com.example.mad.orderlist.ViewAllOrdersActivity;
import com.example.mad.orderlist.ViewOrderActivity;
import com.example.mad.productmanage.ProductMngActivity;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.example.mad.systeminfos.UserInfo;

public class HomeActivity extends AppCompatActivity {

    CardView pmCrd,lpCrd,loCard;
    Button productManageBtn,listProductBtn,listOrderBtn,listAllOrderbtn,logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uiInit();

    }

    void uiInit(){
        pmCrd = (CardView)findViewById(R.id.crdProductManage);
        lpCrd = (CardView) findViewById(R.id.crdListProduct);
        loCard =(CardView) findViewById(R.id.crdListOrder);
        logoutBtn = (Button)findViewById(R.id.btnlogOut);
        productManageBtn=(Button) findViewById(R.id.btnProductManage);
        listProductBtn=(Button) findViewById(R.id.btnListProduct);
        listOrderBtn=(Button) findViewById(R.id.btnListOrder);
        listAllOrderbtn=findViewById(R.id.btnListAllOrder);
        if(UserInfo.isAdmin()==true){
            pmCrd.setVisibility(View.VISIBLE);
        }else{
            pmCrd.setVisibility(View.INVISIBLE);
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