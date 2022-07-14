package com.esgi.framework_JEE.basket;

import com.esgi.framework_JEE.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BasketFixtures {

    public static Response create(Token token){
        return given()
                .header("Authorization","Bearer "+token.access_token)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/basket");
    }

    public static Response generateInvoice(int user_id, Token token){
        return given()
                .header("Authorization","Bearer "+token.access_token)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/basket/generate/" + user_id);
    }
}
