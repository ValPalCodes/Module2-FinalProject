package com.techelevator.model;

public class CartItem {
    private int id;
    private int user_id;
    private int productId;
    private int quantity;
    private Product product;


    public CartItem() {}

    public CartItem(int id, int user_id, int productId, int quantity) {
        this.id = id;
        this.user_id = user_id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", product_id=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
