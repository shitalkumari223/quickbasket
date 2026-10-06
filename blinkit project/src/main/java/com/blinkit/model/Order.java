package com.blinkit.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order {

    private int id;

    private int userId;

    private BigDecimal totalAmount;

    private String status;

    private String address;

    private Timestamp createdAt;

    private List<CartItem> items;


    public Order() {
    }


    public Order(
            int id,
            int userId,
            BigDecimal totalAmount,
            String status,
            String address,
            Timestamp createdAt) {

        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.address = address;
        this.createdAt = createdAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            BigDecimal totalAmount) {

        this.totalAmount = totalAmount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            Timestamp createdAt) {

        this.createdAt = createdAt;
    }


    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(
            List<CartItem> items) {

        this.items = items;
    }
}