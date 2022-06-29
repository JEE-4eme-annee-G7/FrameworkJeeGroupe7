package com.esgi.framework_JEE.basket.paiment;

public class Payment {

    private Buyer buyer;
    private String checkout_id;
    private Double amount;


    public Buyer getBuyer() {
        return buyer;
    }

    public Payment setBuyer(Buyer buyer) {
        this.buyer = buyer;
        return this;
    }

    public String getCheckout_id() {
        return checkout_id;
    }

    public Payment setCheckout_id(String checkout_id) {
        this.checkout_id = checkout_id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
