package com.example.mad.orderlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mad.R;
import com.example.mad.makeorder.ProductAdapter;
import com.example.mad.makeorder.Products;
import com.example.mad.systeminfos.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ViewOrderActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<OrderList> orderLists;
    OrderAdapter orderAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        listView = findViewById(R.id.ordeLst);
        orderLists = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderLists);
        listView.setAdapter(orderAdapter);
        db = FirebaseFirestore.getInstance();

       if(UserInfo.getUserType().equals("A")||UserInfo.getUserType().equals("a")){
           showAllOrders();
       }else{
           showUserOrders();
       }
    }
    private void showUserOrders() {
        db.collection("Order_List")
                .whereEqualTo("UserName", UserInfo.getUserName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String orderUser = document.getString("UserName");
                            String productId = document.getString("proId");
                            String productName = document.getString("productName");
                            String orderUID = document.getString("OrderUID");
                            boolean isApproved = document.getBoolean("IsApprove");
                            String orderId = document.getString("OrderID");
                            double rate = document.getDouble("Rate");
                            double qty = document.getDouble("SellQty");
                            double total = document.getDouble("Total");
                            double discount = document.getDouble("Discount");

                            OrderList orders = new OrderList(orderUser,productId,isApproved,rate,qty,total,orderId,discount,productName,orderUID);
                            orderLists.add(orders);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    private void showAllOrders() {
        db.collection("Order_List")
                .whereEqualTo("UserName", UserInfo.getUserName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String orderUser = document.getString("UserName");
                            String productId = document.getString("proId");
                            String productName = document.getString("productName");
                            boolean isApproved = document.getBoolean("IsApprove");
                            String orderId = document.getString("OrderID");
                            String orderUID = document.getString("OrderUID");
                            double rate = document.getDouble("Rate");
                            double qty = document.getDouble("SellQty");
                            double total = document.getDouble("Total");
                            double discount = document.getDouble("Discount");

                            OrderList orders = new OrderList(orderUser,productId,isApproved,rate,qty,total,orderId,discount,productName,orderUID);
                            orderLists.add(orders);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();

                    }
                });


    }

}