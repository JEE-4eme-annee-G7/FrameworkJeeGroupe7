package com.esgi.framework_JEE.basket.paiment;

import java.util.UUID;

public class PaymentRequest {

    private int user_id;
    //private UserPaymentRequest user;
    private String checkout_id;

    private CreditCard creditCard;


    public PaymentRequest newUUID(){
        this.checkout_id = UUID.randomUUID().toString();
        return this;
    }

    public int getUser_id() {
        return user_id;
    }

    public PaymentRequest setUser_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getCheckout_id() {
        return checkout_id;
    }

    public PaymentRequest setCheckout_id(String checkout_id) {
        this.checkout_id = checkout_id;
        return this;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public PaymentRequest setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }
}

