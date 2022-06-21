package com.esgi.framework_JEE.slot.web.controller;

import com.esgi.framework_JEE.Token;
import com.esgi.framework_JEE.slot.web.request.SlotRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SlotFixtures {
    public static Response create(SlotRequest slotRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(slotRequest)
                .header("Authorization","Bearer "+token.access_token)
                .post("/slot/create");
    }

    public static Response getByStart(SlotRequest slotRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(slotRequest)
                .header("Authorization","Bearer "+token.access_token)
                .get("/slot/start");
    }

    public static Response getById(int slotId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)

                .get("/slot/" + slotId);
    }

    public static Response getAll(Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/slot/");
    }


    public static Response changeSlotStart(int slotId, SlotRequest slotRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(slotRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/slot/start/" + slotId);
    }

    public static Response changeSlotEnd(int slotId, SlotRequest slotRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(slotRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/slot/end/" + slotId);
    }


    public static Response deleteById(int slotId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .delete("/slot/" + slotId);
    }
}
