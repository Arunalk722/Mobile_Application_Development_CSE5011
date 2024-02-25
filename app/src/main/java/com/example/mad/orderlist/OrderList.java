package com.example.mad.orderlist;

public class OrderList {
    private String orderUser;
    private String productId;
    private String pName;
    private boolean isApproved;
    private String orderId;
    private double rate;
    private double qty;
    private double total;

    private double discount; public OrderList(String orderUser,String productId, boolean isApproved, double rate, double qty, double total, String orderId, double discount,String pName) {
        this.orderUser = orderUser;
        this.productId = productId;
        this.isApproved = isApproved;
        this.orderId = orderId;
        this.rate = rate;
        this.qty = qty;
        this.total = total;
        this.discount = discount;
        this.pName = pName;
    }

    // Getters
    public String getProductId() {
        return productId;
    }
    public  String getOrderUser(){
        return  orderUser;
    }
    public boolean isApproved() {
        return isApproved;
    }
    public  String getPName(){return pName;}
    public String getOrderId() {
        return orderId;
    }
    public double getRate() {
        return rate;
    }
    public double getQty() {
        return qty;
    }
    public double getTotal() {
        return total;
    }
    public double getDiscount() {
        return discount;
    }
}