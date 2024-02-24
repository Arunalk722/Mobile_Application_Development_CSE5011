package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    CardView pmCrd,lpCrd,orderCard,loCard;
    Button productManageBtn,listProductBtn,orderBtn,listOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uiInit();

    }

    void uiInit(){
        pmCrd = (CardView)findViewById(R.id.crdProductManage);
        lpCrd = (CardView) findViewById(R.id.crdListProduct);
        orderCard = (CardView) findViewById(R.id.crdOrder);
        loCard =(CardView) findViewById(R.id.crdListOrder);

        productManageBtn=(Button) findViewById(R.id.btnProductManage);
        listProductBtn=(Button) findViewById(R.id.btnListProduct);
        orderBtn=(Button) findViewById(R.id.btnOrder);
        listOrderBtn=(Button) findViewById(R.id.btnListOrder);

        if(UserInfo.getUserType().equals("A")||UserInfo.getUserType().equals("a")){
            pmCrd.setVisibility(View.VISIBLE);
        }else{
            pmCrd.setVisibility(View.INVISIBLE);
        }

        productManageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this,ProductMngActivity.class);
            }
        });
        listProductBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this,ListOfProductActivity.class);
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.toGoNewPage(HomeActivity.this,MakeOrderActivity.class);
            }
        });
        listOrderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SystemOprations.showMessage(UserInfo.getUserType(), "",HomeActivity.this, 3);
                SystemOprations.toGoNewPage(HomeActivity.this,ViewOrderActivity.class);
            }
        });
    }
}