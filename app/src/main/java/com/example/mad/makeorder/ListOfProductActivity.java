package com.example.mad.makeorder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mad.R;
import com.example.mad.makeorder.ProductAdapter;
import com.example.mad.makeorder.Products;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListOfProductActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Products> productList;
    ProductAdapter adapter;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_product);
        listView = findViewById(R.id.lstProducts);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        listView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        retrieveProducts();

    }
    private void retrieveProducts() {
        try{

            db.collection("Product_List")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String productId = document.getString("ProId");
                            String productName = document.getString("ProductName");
                            String prodImg = document.getString("ImagePath");
                            double price = document.getDouble("Price");
                            double quantity = document.getDouble("Quantity");
                            double discount = document.getDouble("Discount");
                            Products products = new Products(productId, productName, price, quantity, discount, prodImg);
                            productList.add(products);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();

                    }
                });

        }
        catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

}