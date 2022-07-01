package com.esgi.framework_JEE.basket.paiment;

public class UserPaymentRequest {
    private String email;
    private String firstname;
    private String lastname;
    private CreditCard creditCard;


    public String getEmail() {
        return email;
    }

    public UserPaymentRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserPaymentRequest setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserPaymentRequest setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public UserPaymentRequest setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }
}
