package com.example.mad.makeorder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.example.mad.systeminfos.FirebaseAuthClass;
import com.example.mad.R;
import com.example.mad.systeminfos.SystemOprations;
import com.example.mad.systeminfos.UserInfo;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends ArrayAdapter<Products> {
    FirebaseAuthClass firebaseAuthClass = new FirebaseAuthClass();
    private Context mContext;
    private List<Products> productList;

    public ProductAdapter(Context context, ArrayList<Products> list) {
        super(context, 0, list);
        mContext = context;
        productList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
        }
        LinearLayout orderPanel = listItemView.findViewById(R.id.lstOrderPanel);

        if(UserInfo.isAdmin()==true){
            orderPanel.setVisibility(View.INVISIBLE);
        }
        else{
            orderPanel.setVisibility(View.VISIBLE);
        }

        //card view
        LinearLayout discountCrd = listItemView.findViewById(R.id.lstDiscount);
        LinearLayout orderItemPanel = listItemView.findViewById(R.id.lstOrderPanel);

        Products currentProduct = productList.get(position);
        //product name
        TextView productName = listItemView.findViewById(R.id.txtProductName);
        productName.setText(currentProduct.getProductName());

        //product id
        TextView productId = listItemView.findViewById(R.id.txtProductId);
        productId.setText(currentProduct.getProductId());

        //product umage
        CircleImageView productImage = listItemView.findViewById(R.id.imgViewPrImgList);
        Glide.with(mContext)
                .load(currentProduct.getProductImg())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.f)
                .into(productImage);

        //discount
        double discount = currentProduct.getdiscount();
        TextView discountTxt = listItemView.findViewById(R.id.txtDiscount);
        discountTxt.setText(String.valueOf(discount));

        if (discount <= 0) {
            discountCrd.setVisibility(View.INVISIBLE);
        } else {
            discountCrd.setVisibility(View.VISIBLE);
        }


        //product price
        double price = currentProduct.getPrice();
        TextView priceTxt = listItemView.findViewById(R.id.txtPrice);
        priceTxt.setText(String.valueOf(price));

        //stock qty
        double stockQty = currentProduct.getquantity();
        TextView stockTxt = listItemView.findViewById(R.id.txtQty);
        stockTxt.setText(String.valueOf(stockQty));

        //input button
        Button orderBtn = listItemView.findViewById(R.id.btnOrder);

        EditText orderQtyTxt = listItemView.findViewById(R.id.txtOrderQty);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemOprations.ynDialog("would you like to buying a new item", "buy a new item", mContext, new SystemOprations.dialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        double sellQty = Double.parseDouble(orderQtyTxt.getText().toString());
                        double newQty = (stockQty) - (sellQty);
                        double totalDiscount = discount * sellQty;
                        double total = (sellQty * price) - totalDiscount;
                        makeOrder(productId.getText().toString(), sellQty, totalDiscount, total, orderItemPanel, newQty, price, productName.getText().toString());

                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        Toast.makeText(mContext, "order not confirmed", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        return listItemView;
    }

    void makeOrder(String productId, double sellQty, double discount, double total, LinearLayout orderItemPanel, double newQty, double rate, String pName) {
        try{

            String newOrderId = SystemOprations.makeGUID();
        Map<String, Object> makeOrder = new HashMap<>();
        UserInfo userInfo = new UserInfo();
        makeOrder.put("OrderUID", newOrderId);
        makeOrder.put("OrderID", userInfo.getOrderGUID());
        makeOrder.put("UserName", userInfo.getUserName());
        makeOrder.put("ProId", productId);
        makeOrder.put("SellQty", sellQty);
        makeOrder.put("Rate", rate);
        makeOrder.put("Discount", discount);
        makeOrder.put("Total", total);
        makeOrder.put("IsApprove", false);
        makeOrder.put("ProductName", pName);

        makeOrder.put("DateTime", SystemOprations.curretDate());
        firebaseAuthClass.saveToFireStore(makeOrder, "Order_List", newOrderId, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                updateProductQuantity(productId, newQty, orderItemPanel);
            }

            @Override
            public void onFailure(Exception error) {

            }
        });
    }
       catch (Exception ex){
        Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_LONG).show();
    }
    }

    void updateProductQuantity(String productId, double newQuantity, LinearLayout orderItemPanel) {
        try{
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("Quantity", newQuantity);

        firebaseAuthClass.updateFirebaseFirestore(updateInfo, "Product_List", productId, new FirebaseAuthClass.FirestoreCallback() {
            @Override
            public void onSuccess() {
                orderItemPanel.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "order placed, thanks!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception error) {
                orderItemPanel.setVisibility(View.VISIBLE);
            }
        });
        }
        catch (Exception ex){
            Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
