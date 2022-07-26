package com.esgi.framework_JEE.user.web.controller;

import com.esgi.framework_JEE.Token;
import com.esgi.framework_JEE.user.web.request.UserRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserFixture {
    public static Response create(UserRequest userRequest){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userRequest)
                .post("/user/create");
    }

    public static Response getById(int userId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/user/" + userId);
    }

    public static Response getByEmail(String email, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/user/email/" + email);
    }

    public static Response changeLastname(int userId, UserRequest userRequest, Token token){

        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/user/lastname/" + userId);
    }

    public static Response changeFirstname(int userId, UserRequest userRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/user/firstname/" + userId);
    }
    public static Response changePassword(int userId, UserRequest userRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/user/password/" + userId);
    }

    public static Response changeEmail(int userId, UserRequest userRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(userRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/user/email/" + userId);

    }


    public static Response deleteById(int userId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .delete("/user/" + userId);
    }

    public static Response getAll(Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/user/");
    }

//
//    public static Response login(UserRequest userRequest){
//        return given()
//                .contentType(ContentType.JSON)
//                .when()
//                .body(userRequest)
//                .get("/user/login");
//    }

}


