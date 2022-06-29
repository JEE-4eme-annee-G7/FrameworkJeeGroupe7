package com.esgi.framework_JEE.basket.paiment;

import java.util.UUID;

public class PaymentRequest {

    private UserPaymentRequest user;
    private String checkout_id;


    public PaymentRequest newUUID(){
        this.checkout_id = UUID.randomUUID().toString();
        return this;
    }


    public UserPaymentRequest getUser() {
        return user;
    }

    public PaymentRequest setUser(UserPaymentRequest user) {
        this.user = user;
        return this;
    }

    public String getCheckout_id() {
        return checkout_id;
    }

    public PaymentRequest setCheckout_id(String checkout_id) {
        this.checkout_id = checkout_id;
        return this;
    }

}

