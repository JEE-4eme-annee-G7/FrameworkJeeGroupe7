package com.esgi.framework_JEE.basket.infrastructure.web.response;

public class BasketResponse {

    private int userId;
    private Double amount;

    public int getUserId() {
        return userId;
    }

    public BasketResponse setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public BasketResponse setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
