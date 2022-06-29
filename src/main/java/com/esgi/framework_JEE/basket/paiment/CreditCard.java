package com.esgi.framework_JEE.basket.paiment;

import java.util.Date;

public class CreditCard {

    private String number;
    private String cryptogram;
    private String ownerLastname;
    private Date expirationDate;


    public String getNumber() {
        return number;
    }

    public CreditCard setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getCryptogram() {
        return cryptogram;
    }

    public CreditCard setCryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public CreditCard setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public CreditCard setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
}
