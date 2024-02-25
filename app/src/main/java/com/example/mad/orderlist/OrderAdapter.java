package com.example.mad.orderlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.mad.R;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.systeminfos.SystemOprations;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter  extends ArrayAdapter<OrderList> {

    private Context mContext;
    private List<OrderList> orderLists;
    public OrderAdapter(Context context, ArrayList<OrderList> list) {
        super(context, 0 , list);
        mContext = context;
        orderLists = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_of_orders,parent,false);
        }
        //card view
      /*  CardView discountCrd = listItemView.findViewById(R.id.crdDiscount);
        CardView orderItem = listItemView.findViewById(R.id.cardProduct);*/

        OrderList currentOrder= orderLists.get(position);
        //product name
        TextView orderUser = listItemView.findViewById(R.id.lblCustomer);
        orderUser.setText(currentOrder.getOrderUser());

        //product id
        TextView productId = listItemView.findViewById(R.id.lblPName);
        productId.setText(currentOrder.getPName());


        //discount
        double discount = currentOrder.getDiscount();
        TextView discountTxt =listItemView.findViewById(R.id.lblDiscount);
        discountTxt.setText(String.valueOf(discount)+ " LKR");

        double total = currentOrder.getTotal();
        TextView totalTxt =listItemView.findViewById(R.id.lblTotal);
        totalTxt.setText(String.valueOf(total)+ " LKR");

        double qty = currentOrder.getQty();
        TextView qtyTxt = listItemView.findViewById(R.id.lblQty);
        qtyTxt.setText(String.valueOf(qty) + " NOS");
      if(discount<=0){
          discountTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }else{
          discountTxt.setTextColor(ContextCompat.getColor(getContext(), R.color.clearRed));

        }

        Button accpetBtn = listItemView.findViewById(R.id.btnAcceptOrder);
        Button declineBtn = listItemView.findViewById(R.id.btnDecline);
        accpetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,currentOrder.getOrderId(),Toast.LENGTH_LONG);
                updateStatusOnOrder(true, currentOrder.getOrderUID());
            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,currentOrder.getOrderUID(),Toast.LENGTH_LONG);
                updateStatusOnOrder(false, currentOrder.getOrderUID());
            }
        });


 /*
        //product price
        double price = currentProduct.getPrice();
        TextView priceTxt =listItemView.findViewById(R.id.txtPrice);
        priceTxt.setText(String.valueOf(price));

        //stock qty
        double stockQty = currentProduct.getquantity();
        TextView stockTxt =listItemView.findViewById(R.id.txtQty);
        stockTxt.setText(String.valueOf(stockQty));

        //input button
        Button orderBtn = listItemView.findViewById(R.id.btnOrder);

        EditText orderQtyTxt = listItemView.findViewById(R.id.txtOrderQty);*/
    /*    orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sellQty = Double.parseDouble(orderQtyTxt.getText().toString());
                double newQty = (stockQty)-(sellQty);
                double totalDiscount = discount*sellQty;
                double total = (sellQty*price)-totalDiscount;
                makeOrder(productId.getText().toString(),sellQty,totalDiscount,total,orderItem,newQty,price);
            }
        });*/
        return listItemView;
    }

    void updateStatusOnOrder(boolean isApprove,String orderUid){
        Map<String,Object> updateStatus = new HashMap<>();
        updateStatus.put("IsApprove",isApprove);
        FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
        firebaseAuthClass.updateFirebaseFirestore(updateStatus,  "f64bf3a9-ff1e-4cd9-af4a-cf8743373995","Product_List", new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                if(isApprove==true){
                    Toast.makeText(mContext, "product was accepted.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, "order decline", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
