package com.esgi.framework_JEE.basket.paiment.mapper;

import com.esgi.framework_JEE.basket.paiment.Buyer;
import com.esgi.framework_JEE.basket.paiment.CreditCard;
import com.esgi.framework_JEE.basket.paiment.UserPaymentRequest;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BuyerMapper {

    public Buyer toBuyer(UserPaymentRequest userPaymentRequest){
        return new Buyer()
                //.setId(userPaymentRequest.getId())
                .setEmail(userPaymentRequest.getEmail())
                .setFirstname(userPaymentRequest.getFirstname())
                .setLastname(userPaymentRequest.getLastname())
                .setCreditCard(userPaymentRequest.getCreditCard());
    }

    public Buyer userToBuyer(User user, CreditCard creditCard){
        return new Buyer()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstname(user.getFirstname())
                .setLastname(user.getLastname())
                .setCreditCard(creditCard);
    }
}
