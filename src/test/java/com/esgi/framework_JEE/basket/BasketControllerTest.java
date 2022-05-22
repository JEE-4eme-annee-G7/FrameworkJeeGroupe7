package com.esgi.framework_JEE.basket;


import com.esgi.framework_JEE.invoice.InvoiceFixtures;
import com.esgi.framework_JEE.use_case.basket.infrastructure.web.response.BasketResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


import java.util.*;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BasketControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }


    @Test
    public void shouldGenerateBasketWithUserId(){

        int user_id = 1;

        var location = BasketFixtures.generateInvoice(user_id)
                .then()
                .statusCode(201)
                .extract().header("Location");

        var basketResponse = when()
                .get(location)
                .then()
                .statusCode(302)
                .extract().body().jsonPath().getObject(".", BasketResponse.class);

        assertThat(basketResponse.getUserId()).isEqualTo(user_id);

    }

    @Test
    public void shouldDeleteBasket(){

        var locationBasketCreated = BasketFixtures.create()
                .then()
                .statusCode(201)
                .extract().header("Location");

        when()
                .delete(locationBasketCreated)
                .then()
                .statusCode(200);

    }

}