package com.esgi.framework_JEE.invoice;

import com.esgi.framework_JEE.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class InvoiceFixtures {

    public static Response create(Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .post("/api/v1/invoice");
    }

    public static Response generateInvoice(int user_id, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .post("/api/v1/invoice/generate/" + user_id);
    }

}
