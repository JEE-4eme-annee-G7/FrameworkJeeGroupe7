package com.esgi.framework_JEE;

import com.esgi.framework_JEE.user.web.request.UserRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TokenFixture {
    public static Response login(UserRequest userRequest){
        return given()
                .when()
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("email", userRequest.email)
                .formParam("password",userRequest.password)
                .post("/login");
    }

    public static Response refresh(Token token){
        return given()
                .when()
                .formParam("Authorization", "Bearer "+token.refresh_token)
                .post("/token/refresh");
    }

    public static Token adminToken(){
        var request = new UserRequest();
        request.email = "lucas@hotmail.fr";
        request.password = "azerty1234";
        return TokenFixture.login(request).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".",Token.class);
    }

    public static Token userToken(){
        var request = new UserRequest();
        request.email = "test@test.fr";
        request.password = "test1234test";
        return TokenFixture.login(request).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".",Token.class);
    }

    public static Token getToken(UserRequest request){
        return TokenFixture.login(request).then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".",Token.class);
    }
}
