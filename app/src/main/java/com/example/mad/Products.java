package com.example.mad;

public class Products {
    private String productId;
    private String productName;
    private String productImg;
    private double price;
    private double quantity;
    private double discount;

    public Products(String productId, String productName, double price, double quantity, double discount,String productImg) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.productImg = productImg;
    }

    public String getProductName() {
        return  productName;
    }

    public String getProductId() {
        return productId;
    }
    public String getProductImg(){
        return  productImg;
    }
    public  double getPrice(){
        return price;
    }
    public  double getquantity(){
        return quantity;
    }
    public  double getdiscount(){
        return discount;
    }
}
