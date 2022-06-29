package com.esgi.framework_JEE.basket.paiment.mapper;

import com.esgi.framework_JEE.basket.paiment.Buyer;
import com.esgi.framework_JEE.basket.paiment.UserPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class BuyerMapper {

    public Buyer toBuyer(UserPaymentRequest userPaymentRequest){
        return new Buyer()
                .setId(userPaymentRequest.getId())
                .setEmail(userPaymentRequest.getEmail())
                .setFirstname(userPaymentRequest.getFirstname())
                .setLastname(userPaymentRequest.getLastname())
                .setCreditCard(userPaymentRequest.getCreditCard());
    }
}
