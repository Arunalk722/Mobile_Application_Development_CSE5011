package com.example.mad.orderlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.mad.R;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;
import com.example.mad.systeminfos.UserInfo;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter  extends ArrayAdapter<OrderList> {

    private Context mContext;
    private List<OrderList> orderLists;

    public OrderAdapter(Context context, ArrayList<OrderList> list) {
        super(context, 0, list);
        mContext = context;
        orderLists = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_of_orders, parent, false);
        }
        //card view
        OrderList currentOrder = orderLists.get(position);
        CardView acceptCrd = listItemView.findViewById(R.id.crdAccept);
        ImageView imgCurrectMark = listItemView.findViewById(R.id.imgViewCurretMark);
        if (UserInfo.isAdmin() == true&&currentOrder.isApproved()==false) {
            acceptCrd.setVisibility(View.VISIBLE);
            imgCurrectMark.setVisibility(View.INVISIBLE);
        } else {
            acceptCrd.setVisibility(View.INVISIBLE);
            imgCurrectMark.setVisibility(View.VISIBLE);
        }




        //product name
        TextView orderUser = listItemView.findViewById(R.id.lblCustomer);
        orderUser.setText(currentOrder.getOrderUser());

        //product id
        TextView productId = listItemView.findViewById(R.id.lblPName);
        productId.setText(currentOrder.getPName());


        //discount
        double discount = currentOrder.getDiscount();
        TextView discountTxt = listItemView.findViewById(R.id.lblDiscount);
        discountTxt.setText(String.valueOf(discount) + " LKR");

        double total = currentOrder.getTotal();
        TextView totalTxt = listItemView.findViewById(R.id.lblTotal);
        totalTxt.setText(String.valueOf(total) + " LKR");

        double qty = currentOrder.getQty();
        TextView qtyTxt = listItemView.findViewById(R.id.lblQty);
        qtyTxt.setText(String.valueOf(qty) + " NOS");



        if (discount <= 0) {
            discountTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        } else {
            discountTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.clearRed));

        }
        Button acceptBtn = listItemView.findViewById(R.id.btnAcceptOrder);
        Button declineBtn = listItemView.findViewById(R.id.btnDecline);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("would you like to approve selected order", "approving order", mContext, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        updateStatusOnOrder(true, currentOrder.getOrderUID());
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(mContext, "order approving not confirmed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("would you like to decline order", "decline order", mContext, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        deleteOrder(currentOrder.getOrderUID());
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(mContext, "order declined not accepted", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return listItemView;
    }

    void updateStatusOnOrder(boolean isApprove, String orderUid) {
        try {
            Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put("IsApprove", isApprove);
            FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
            firebaseAuthClass.updateFirebaseFirestore(updateStatus, "Order_List", orderUid, new FirebaseAuthClass.FirestoreCallback() {
                @Override
                public void onSuccess() {
                    if (isApprove == true) {
                        Toast.makeText(mContext, "Order was accepted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "order decline", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Exception error) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void deleteOrder(String orderUid) {
        try {
            FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
            firebaseAuthClass.deleteFirebaseFirestore("Order_List", orderUid, new FirebaseAuthClass.FirestoreCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "Order was deleted.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception error) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
