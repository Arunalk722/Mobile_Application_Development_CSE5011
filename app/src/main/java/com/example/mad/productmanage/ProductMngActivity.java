package com.example.mad.productmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mad.otherwidget.SignInActivity;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.R;
import com.example.mad.systeminfos.SystemOprations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProductMngActivity extends AppCompatActivity {
    private FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
    private EditText proIdTxt, proNameTxt, proPriceTxt, proQtyTxt, proDisTxt;
    private Button findBtn, newProductBtn, clearBtn, updateBtn, imageSelectBtn, imageUploadBtn;
    private ProgressBar savePBar, findPBar, imageUploadPBar;
    private boolean isNewProduct = true;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private ImageView imageView;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_mng);
        uiInit();

        hideSavePrograss();
        hideFindPBar();
        // hideUploadImage();

        newProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("would you like to create new product", "new product", ProductMngActivity.this, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        showSavePrograss();
                        productSave();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(ProductMngActivity.this, "new product creation not confirmed", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proIdTxt.length() >= 1) {
                    Integer id = Integer.parseInt(proIdTxt.getText().toString());
                    String productId = String.format("%08d", id);
                    SystemOprations.ynDialog("would you like to find product id\n" + productId, "find product", ProductMngActivity.this, new SystemOprations.dialogCallback() {
                        @Override
                        public void onPositiveButtonClicked() {
                            showFindPBar();
                            productFind(productId);
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            Toast.makeText(ProductMngActivity.this, "product find not confirmed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("Would you like to reset layout", "Clear data", ProductMngActivity.this, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        clearText();
                        clearError();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(ProductMngActivity.this, "Layout clear not confirmed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("Would you like to update product information", "product update", ProductMngActivity.this, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        if (proIdTxt.length() == 8) {
                            showSavePrograss();
                            uploadImage(filePath, proIdTxt.getText().toString());
                        }
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(ProductMngActivity.this, "Image Update not confirmed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        imageSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("Would you like to select product image from your gallery", "Select image", ProductMngActivity.this, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        SelectImage();
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(ProductMngActivity.this, "Image selection not confirmed", Toast.LENGTH_LONG).show();
                    }
                });

            }

        });
    }


    private void SelectImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            "Select Image from here..."),
                    PICK_IMAGE_REQUEST);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
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
        updateBtn = (Button) findViewById(R.id.btnUpdate);
        clearBtn = (Button) findViewById(R.id.btnClear);
        savePBar = (ProgressBar) findViewById(R.id.prgBarSave);
        findPBar = (ProgressBar) findViewById(R.id.prgFind);
        imageView = findViewById(R.id.imgViewPrImg);
    }

    void productSave() {
        try {
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

                            String productId = String.format("%08d", nextId);
                            uploadImage(filePath, productId);
                        }

                        @Override
                        public void onError(Exception e) {
                            SystemOprations.showMessage(e.getMessage(), "error on product id create", ProductMngActivity.this, 3);
                            hideSavePrograss();
                        }
                    });
                } else {

                    proIdTxt.setText((int) Double.parseDouble(proIdTxt.getText().toString()));
                    uploadImage(filePath, proIdTxt.getText().toString());

                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void uploadImage(Uri imageUri, String productId) {
        try {
            storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("product/" + productId);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();

                                    saveProductInfo(productId, Uri.parse(downloadUrl));
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Image upload failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void productFind(String productId) {
        try {
            firebaseAuthClass.scanFromFirestore(productId, "Product_List", new FirebaseAuthClass.ScanProductCallback() {
                @Override
                public void onScanProductSuccess(DocumentSnapshot documentSnapshot) {
                    proIdTxt.setText(documentSnapshot.getString("proId").toString());
                    proNameTxt.setText(documentSnapshot.getString("productName").toString());
                    proDisTxt.setText(documentSnapshot.getDouble("Discount").toString());
                    proQtyTxt.setText(documentSnapshot.getDouble("Quantity").toString());
                    proPriceTxt.setText(documentSnapshot.getDouble("Price").toString());
                    Glide.with(ProductMngActivity.this)
                            .load(documentSnapshot.getString("imagePath"))
                            .placeholder(null)
                            .into(imageView);
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
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

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
        imageView.setImageBitmap(null);
    }

    void saveProductInfo(String productId, Uri imageUrl) {
        try {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("proId", productId);
            productInfo.put("productName", proNameTxt.getText().toString());
            productInfo.put("Price", Double.parseDouble(proPriceTxt.getText().toString()));
            productInfo.put("Quantity", Double.parseDouble(proQtyTxt.getText().toString()));
            productInfo.put("Discount", Double.parseDouble(proDisTxt.getText().toString()));
            productInfo.put("DateTime", SystemOprations.curretDate().toString());
            productInfo.put("imagePath", imageUrl);
            firebaseAuthClass.saveToFireStore(productInfo, "Product_List", productId, new FirebaseAuthClass.FirestoreCallback() {
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
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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