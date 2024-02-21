package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

public class ProductMngActivity extends AppCompatActivity {
    FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
    EditText proIdTxt, proNameTxt, proPriceTxt, proQtyTxt, proDisTxt;
    Button findBtn, saveBtn;
    ProgressBar pBar;
    boolean isNewProduct= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_mng);
        uiInit();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSave();
            }
        });
    }

    void uiInit() {
        proIdTxt = (EditText) findViewById(R.id.txtProductId);
        proNameTxt = (EditText) findViewById(R.id.txtProductName);
        proPriceTxt = (EditText) findViewById(R.id.txtProductPrice);
        proQtyTxt = (EditText) findViewById(R.id.txtQty);
        proDisTxt = (EditText) findViewById(R.id.txtDiscount);
        findBtn = (Button) findViewById(R.id.btnFoundProduct);
        saveBtn = (Button) findViewById(R.id.btnSave);
        pBar = (ProgressBar) findViewById(R.id.prgBarSave);
    }

    void productSave() {

        showPrograss();

//       if(proIdTxt.getText().toString().length()<2){
//
//       }
       if(proNameTxt.getText().toString().length()<2){
           proNameTxt.setError("Please provide name for product");
       }
     else   if(proPriceTxt.getText().toString()==null){
            proPriceTxt.setError("Please add price for product");
       }
        if(proQtyTxt.getText().toString().length()<=1){
            proQtyTxt.setError("please provide product qty");
       }
        else   if(proDisTxt.getText().toString()==null){
            proDisTxt.setText("0");
            proDisTxt.setError("no discount activated");
       }
     else{

         if(isNewProduct==true)
         {
             firebaseAuthClass.getNextId("ProductList", new FirebaseAuthClass.NextIdCallback() {
                 @Override
                 public void onNextId(int nextId) {
                     // SystemOprations.showMessage("A","error on product id create",ProductMngActivity.this,3);
                     String productId =String.format("%08d", nextId);
                     saveProductInfo(productId);
                 }

                 @Override
                 public void onError(Exception e) {
                     SystemOprations.showMessage(e.getMessage(),"error on product id create",ProductMngActivity.this,3);
                 }
             });
         }else{

             proIdTxt.setText((int) Double.parseDouble(proIdTxt.getText().toString()));
             saveProductInfo(proIdTxt.getText().toString());
         }
        }
     hidePrograss();
    }

    void saveProductInfo(String productId) {

        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("productName", proNameTxt.getText().toString());
        productInfo.put("Price", Double.parseDouble(proPriceTxt.getText().toString()));
        productInfo.put("Quantity", Double.parseDouble(proQtyTxt.getText().toString()));
        productInfo.put("Discount", Double.parseDouble(proDisTxt.getText().toString()));
        productInfo.put("DateTime", SystemOprations.curretDate().toString());
        firebaseAuthClass.intFirebaseFireStore(productInfo, "ProductList", productId, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                SystemOprations.showMessage("New Product save successful.\nproduct Id "+productId,"New Product save",ProductMngActivity.this,1);
            }

            @Override
            public void onFailure(Exception error) {
                SystemOprations.showMessage(error.getMessage(),"error on product saving",ProductMngActivity.this,3);
            }
        });
    }
    void showPrograss(){
        saveBtn.setVisibility(View.INVISIBLE);
        pBar.setVisibility(View.VISIBLE);
    }void  hidePrograss(){
        saveBtn.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.INVISIBLE);
    }
}