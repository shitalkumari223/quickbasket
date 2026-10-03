package com.blinkit.model;

import java.math.BigDecimal;

public class CartItem {

    private int id;          // cart table ki apni row id (update/delete ke liye)
    private int userId;
    private int productId;
    private String name;
    private BigDecimal price;
    private String image;
    private int quantity;

    public CartItem() {}

    public CartItem(int id, int userId, int productId, String name,
                     BigDecimal price, String image, int quantity) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}