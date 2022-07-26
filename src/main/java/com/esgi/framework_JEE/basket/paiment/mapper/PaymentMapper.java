package com.esgi.framework_JEE.basket.paiment.mapper;

import com.esgi.framework_JEE.basket.paiment.Payment;
import com.esgi.framework_JEE.basket.paiment.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toPayment(PaymentRequest paymentRequest){
        return new Payment()
                .setCheckout_id(paymentRequest.getCheckout_id());
    }
}
