package com.esgi.framework_JEE.visited_product;

import com.esgi.framework_JEE.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class VisitedProductFixture {
    public static Response addProduct(int productId, int userId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userId)
                .header("Authorization","Bearer "+token.access_token)
                .post("/products/" + productId + "/visited/");
    }

    public static Response getAllVisitedProducts(int userId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userId)
                .header("Authorization","Bearer "+token.access_token)
                .post("/products/visited");
    }

}
