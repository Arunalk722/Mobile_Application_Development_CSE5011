package com.example.mad.orderlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mad.R;
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




        if (UserInfo.isAdmin() == true) {
            showAllOrders();
        } else {
            showUserOrders();
        }
    }

    private void showUserOrders() {
        try {
            db.collection("Order_List")
                    .whereEqualTo("UserName", UserInfo.getUserName())
                    .whereEqualTo("IsApprove", false)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String orderUser = document.getString("UserName");
                                String productId = document.getString("ProId");
                                String productName = document.getString("ProductName");
                                String orderUID = document.getString("OrderUID");
                                boolean isApproved = document.getBoolean("IsApprove");
                                String orderId = document.getString("OrderID");
                                double rate = document.getDouble("Rate");
                                double qty = document.getDouble("SellQty");
                                double total = document.getDouble("Total");
                                double discount = document.getDouble("Discount");

                                OrderList orders = new OrderList(orderUser, productId, isApproved, rate, qty, total, orderId, discount, productName, orderUID);
                                orderLists.add(orders);
                            }
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showAllOrders() {
        try {
            db.collection("Order_List")
                    .whereEqualTo("UserName", UserInfo.getUserName()).whereEqualTo("IsApprove", false)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String orderUser = document.getString("UserName");
                                String productId = document.getString("ProId");
                                String productName = document.getString("ProductName");
                                boolean isApproved = document.getBoolean("IsApprove");
                                String orderId = document.getString("OrderID");
                                String orderUID = document.getString("OrderUID");
                                double rate = document.getDouble("Rate");
                                double qty = document.getDouble("SellQty");
                                double total = document.getDouble("Total");
                                double discount = document.getDouble("Discount");

                                OrderList orders = new OrderList(orderUser, productId, isApproved, rate, qty, total, orderId, discount, productName, orderUID);
                                orderLists.add(orders);
                            }
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();

                        }
                    });

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}