package com.example.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductMngActivity extends AppCompatActivity {
    private  FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
    private  EditText proIdTxt, proNameTxt, proPriceTxt, proQtyTxt, proDisTxt;
    private  Button findBtn, newProductBtn,clearBtn,updateBtn,imageSelectBtn,imageUploadBtn;
    private  ProgressBar savePBar, findPBar;
    private    boolean isNewProduct = true;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private   ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_mng);
        uiInit();

        hideSavePrograss();
        hideFindPBar();

        newProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSavePrograss();
                productSave();

            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(proIdTxt.length()>=1){
                   showFindPBar();
                   ProductInfo pi = new ProductInfo();
                   Integer id = Integer.parseInt(proIdTxt.getText().toString());
                   String productId = String.format("%08d", id);
                   productFind(productId);
               }

            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
                clearError();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(proIdTxt.length()==8){
                    showSavePrograss();
                    saveProductInfo(proIdTxt.getText().toString());
                }
            }
        });

        imageSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();
            }
        });
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            filePath = data.getData();
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
                // Show an error message to the user
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a message indicating that no image was selected
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    void uiInit() {
        proIdTxt = (EditText) findViewById(R.id.txtProductId);
        proNameTxt = (EditText) findViewById(R.id.txtProductName);
        proPriceTxt = (EditText) findViewById(R.id.txtProductPrice);
        proQtyTxt = (EditText) findViewById(R.id.txtQty);
        proDisTxt = (EditText) findViewById(R.id.txtDiscount);
        findBtn = (Button) findViewById(R.id.btnFoundProduct);
        newProductBtn = (Button) findViewById(R.id.btnNewProduct);

        imageSelectBtn = (Button) findViewById(R.id.btnSelectImage);
        imageUploadBtn = (Button) findViewById(R.id.btnUploadImage);


        updateBtn = (Button) findViewById(R.id.btnUpdate);
        clearBtn = (Button) findViewById(R.id.btnClear);
        savePBar = (ProgressBar) findViewById(R.id.prgBarSave);
        findPBar = (ProgressBar) findViewById(R.id.prgFind);

        imageView = findViewById(R.id.imgViewPrImg);
    }

    void productSave() {


//       if(proIdTxt.getText().toString().length()<2){
//
//       }
        if (proNameTxt.getText().toString().length() < 2) {
            proNameTxt.setError("Please provide name for product");
        } else if (proPriceTxt.getText().toString() == null) {
            proPriceTxt.setError("Please add price for product");
        }
        if (proQtyTxt.getText().toString().length() <= 1) {
            proQtyTxt.setError("please provide product qty");
        } else if (proDisTxt.getText().toString() == null) {
            proDisTxt.setText("0");
            proDisTxt.setError("no discount activated");
        } else {

            if (isNewProduct == true) {
                firebaseAuthClass.getNextId("Product_List", new FirebaseAuthClass.NextIdCallback() {
                    @Override
                    public void onNextId(int nextId) {
                        // SystemOprations.showMessage("A","error on product id create",ProductMngActivity.this,3);
                        String productId = String.format("%08d", nextId);
                        saveProductInfo(productId);
                        hideSavePrograss();
                    }

                    @Override
                    public void onError(Exception e) {
                        SystemOprations.showMessage(e.getMessage(), "error on product id create", ProductMngActivity.this, 3);
                        hideSavePrograss();
                    }
                });
            } else {

                proIdTxt.setText((int) Double.parseDouble(proIdTxt.getText().toString()));
                saveProductInfo(proIdTxt.getText().toString());

            }
        }

    }

    void productFind(String productId) {
        firebaseAuthClass.scanProductBaseOn(productId, new FirebaseAuthClass.ScanProductCallback() {
            @Override
            public void onScanProductSuccess(DocumentSnapshot documentSnapshot) {
                proIdTxt.setText(documentSnapshot.getString("proId").toString());
                proNameTxt.setText(documentSnapshot.getString("productName").toString());
                proDisTxt.setText(documentSnapshot.getDouble("Discount").toString());
                proQtyTxt.setText(documentSnapshot.getDouble("Quantity").toString());
                proPriceTxt.setText(documentSnapshot.getDouble("Price").toString());
                clearError();
                hideFindPBar();
            }

            @Override
            public void onScanProductFailure(Exception e) {
                proIdTxt.setError("Please check product id");
                clearText();
                hideFindPBar();
            }
        });

    }

    void clearError() {
        proNameTxt.setError(null);
        proDisTxt.setError(null);
        proQtyTxt.setError(null);
        proPriceTxt.setError(null);
    }

    void clearText() {
        proNameTxt.setText("");
        proDisTxt.setText("");
        proQtyTxt.setText("");
        proPriceTxt.setText("");
    }

    void saveProductInfo(String productId) {
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("proId", productId);
        productInfo.put("productName", proNameTxt.getText().toString());
        productInfo.put("Price", Double.parseDouble(proPriceTxt.getText().toString()));
        productInfo.put("Quantity", Double.parseDouble(proQtyTxt.getText().toString()));
        productInfo.put("Discount", Double.parseDouble(proDisTxt.getText().toString()));
        productInfo.put("DateTime", SystemOprations.curretDate().toString());
        firebaseAuthClass.intFirebaseFireStore(productInfo, "Product_List", productId, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                SystemOprations.showMessage("New Product save successful.\nproduct Id " + productId, "New Product save", ProductMngActivity.this, 1);
                hideSavePrograss();
            }

            @Override
            public void onFailure(Exception error) {
                SystemOprations.showMessage(error.getMessage(), "error on product saving", ProductMngActivity.this, 3);
                hideSavePrograss();
            }
        });
    }
    void updateProductInfo(String productId) {
        Map<String, Object> productInfo = new HashMap<>();
        productInfo.put("proId", productId);
        productInfo.put("productName", proNameTxt.getText().toString());
        productInfo.put("Price", Double.parseDouble(proPriceTxt.getText().toString()));
        productInfo.put("Quantity", Double.parseDouble(proQtyTxt.getText().toString()));
        productInfo.put("Discount", Double.parseDouble(proDisTxt.getText().toString()));
        productInfo.put("DateTime", SystemOprations.curretDate().toString());
        firebaseAuthClass.intFirebaseFireStore(productInfo, "Product_List", productId, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                SystemOprations.showMessage("New Product save successful.\nproduct Id " + productId, "New Product save", ProductMngActivity.this, 1);
                hideSavePrograss();
            }

            @Override
            public void onFailure(Exception error) {
                SystemOprations.showMessage(error.getMessage(), "error on product saving", ProductMngActivity.this, 3);
                hideSavePrograss();
            }
        });
    }
    void showSavePrograss() {
        newProductBtn.setVisibility(View.INVISIBLE);
        clearBtn.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);
        savePBar.setVisibility(View.VISIBLE);


    }

    void hideSavePrograss() {
        newProductBtn.setVisibility(View.VISIBLE);
        clearBtn.setVisibility(View.VISIBLE);
        updateBtn.setVisibility(View.VISIBLE);
        savePBar.setVisibility(View.INVISIBLE);

    }

    void showFindPBar() {
        findPBar.setVisibility(View.VISIBLE);
        findBtn.setVisibility(View.INVISIBLE);

    }

    void hideFindPBar() {
        findBtn.setVisibility(View.VISIBLE);
        findPBar.setVisibility(View.INVISIBLE);
    }
}