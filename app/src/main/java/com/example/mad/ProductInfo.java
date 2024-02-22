package com.example.mad;

public class ProductInfo {
    private String productName = null;
    private String productId = null;
    private Double productPrice = null;
    private Double productQty = null;
    private Double productDiscount = null;
    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Double getProductQty() {
        return productQty;
    }

    public Double getProductDiscount() {
        return productDiscount;
    }

    // Setters
    public void setProductName(String productName) {
        this.productName = productName;
    }



    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQty(Double productQty) {
        this.productQty = productQty;
    }

    public void setProductDiscount(Double productDiscount) {
        this.productDiscount = productDiscount;
    }
}
