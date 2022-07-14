package com.esgi.framework_JEE.invoice;


import com.esgi.framework_JEE.TestFixtures;
import com.esgi.framework_JEE.TokenFixture;
import com.esgi.framework_JEE.invoice.domain.Invoice;
import com.esgi.framework_JEE.invoice.infrastructure.web.response.InvoiceResponse;
import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.web.controller.UserFixture;
import com.esgi.framework_JEE.user.web.request.UserRequest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


import java.util.*;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class InvoiceControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void shouldGenerateInvoiceWithUserId(){
        var token = TokenFixture.userToken();
        var userRequest = new UserRequest();
        userRequest.firstname = "kelyan";
        userRequest.lastname = "bervin";
        userRequest.email = TestFixtures.randomEmail();
        userRequest.password = "mot de passe";

        var user = UserFixture.create(userRequest)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", User.class);

        //var token = TokenFixture.getToken(userRequest);

        var location = InvoiceFixtures.generateInvoice(user.getId(), token)
                .then()
                .statusCode(201)
                .extract().header("Location");

        var invoiceResponse = given()
                .header("Authorization","Bearer "+token.access_token)
                .when()
                .get(location)
                .then()
                .statusCode(302)
                .extract().body().jsonPath().getObject(".", InvoiceResponse.class);

        assertThat(invoiceResponse.getUser_id()).isEqualTo(user.getId());

        UserFixture.deleteById(user.getId(), token);
    }



    @Test
    public void shouldGetAllInvoice(){
        var token = TokenFixture.userToken();
        var invoicesInDatabaseBefore = given()
                .header("Authorization","Bearer "+token.access_token)
                .when()
                .get("/api/v1/invoice")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".",  new TypeRef<List<Invoice>>() {});


        InvoiceFixtures.create(token)
                .then()
                .statusCode(201)
                .extract().header("Location");

        InvoiceFixtures.create(token)
                .then()
                .statusCode(201)
                .extract().header("Location");

        var invoicesInDatabaseAfter = given()
                .header("Authorization","Bearer "+token.access_token)
                .when()
                .get("/api/v1/invoice")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".",  new TypeRef<List<Invoice>>() {});

        assertThat(invoicesInDatabaseAfter).hasSize(invoicesInDatabaseBefore.size() + 2);
    }


    @Test
    public void shouldDeleteInvoice(){
        var token = TokenFixture.userToken();
        var locationInvoiceCreated = InvoiceFixtures.create(token)
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .header("Authorization","Bearer "+ TokenFixture.adminToken().access_token)
                .when()
                .delete(locationInvoiceCreated)
                .then()
                .statusCode(200);
    }

}
