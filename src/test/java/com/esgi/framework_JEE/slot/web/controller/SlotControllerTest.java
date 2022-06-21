package com.esgi.framework_JEE.slot.web.controller;

import com.esgi.framework_JEE.TokenFixture;
import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.web.request.SlotRequest;
import com.esgi.framework_JEE.slot.web.response.SlotResponse;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SlotControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }



    @Test
    public void should_create_get_update_and_delete_slot() throws ParseException {
        var token = TokenFixture.userToken();

        var slotRequest = new SlotRequest();
        slotRequest.start = "09/05/2022 09:00";
        slotRequest.end = "09/05/2022 10:00";

        /*
         * create
         */
        var slotResponse = SlotFixtures.create(slotRequest, token)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);

        var id = slotResponse.getId();


        assertThat(DateManipulator.stringToDate(slotResponse.getStart())).isEqualTo(DateManipulator.stringToDate(slotRequest.start));
        assertThat(slotResponse.getEnd()).isEqualTo(slotRequest.end);


        /*
         * get by id
         */
        var getSlotResponse = SlotFixtures.getById(id, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);


        assertThat(getSlotResponse.getStart()).isEqualTo(slotRequest.start);
        assertThat(getSlotResponse.getEnd()).isEqualTo(slotRequest.end);
        assertThat(getSlotResponse.getId()).isEqualTo(id);


        /*
         * PATCH (change start slotRequest)
         */
        slotRequest.start = "08/05/2022 11:00";
        var updatedStartSlotResponse = SlotFixtures.changeSlotStart(id,slotRequest, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);

        assertThat(updatedStartSlotResponse.getStart()).isEqualTo(slotRequest.start);
        assertThat(updatedStartSlotResponse.getEnd()).isEqualTo(slotRequest.end);
        assertThat(updatedStartSlotResponse.getId()).isEqualTo(id);


        /*
         * PATCH (change end slotRequest)
         */
        slotRequest.end = "08/05/2022 12:00";
        var updatedEndSlotResponse = SlotFixtures.changeSlotEnd(id,slotRequest, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);


        assertThat(updatedEndSlotResponse.getStart()).isEqualTo(slotRequest.start);
        assertThat(updatedEndSlotResponse.getEnd()).isEqualTo(slotRequest.end);
        assertThat(updatedEndSlotResponse.getId()).isEqualTo(id);

        /*
         * PATCH (change start slotRequest) (return 400, start cannot be after end)
         */
        slotRequest.start = "09/05/2022 11:00";
        var updatedSlotResponse = SlotFixtures.changeSlotStart(id,slotRequest, token)
                .then()
                .statusCode(400)
                .extract().body().asString();


        assertThat(updatedSlotResponse).isEqualTo("");

        /*
         * PATCH (change start slotRequest) (return 400, start cannot be after end)
         */
        slotRequest.end = "09/05/2012 11:00";
        var updatedSlotResponse2 = SlotFixtures.changeSlotEnd(id,slotRequest, token)
                .then()
                .statusCode(400)
                .extract().body().asString();


        assertThat(updatedSlotResponse2).isEqualTo("");

        /*
         * delete
         */

        var deleteSlotResponse = SlotFixtures.deleteById(id, token)
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertThat(deleteSlotResponse).isEqualTo("Slot " + id + " deleted");

        /*
         * delete again
         */
        deleteSlotResponse = SlotFixtures.deleteById(id, token)
                .then()
                .statusCode(400)
                .extract().body().asString();


        assertThat(deleteSlotResponse).isEqualTo("Slot " + id + " not exist");
    }

    @Test
    public void should_return_bad_request() {
        var token = TokenFixture.userToken();

        var slotRequest = new SlotRequest();
        slotRequest.start = "09/05/2022 11:00";
        slotRequest.end = "09/05/2022 10:00";

        var slotResponse = SlotFixtures.create(slotRequest, token)
                .then()
                .statusCode(400)
                .extract().body().asString();
        assertThat(slotResponse).isEqualTo("");

        /*
         * get by id
         */
        var getSlotResponse = SlotFixtures.getById(0, token)
                .then()
                .statusCode(400)
                .extract().body().asString();

        assertThat(getSlotResponse).isEqualTo("");
    }



    @Test
    public void should_get_all() {
        var token = TokenFixture.userToken();

        var slotRequest = new SlotRequest();
        slotRequest.start = "09/05/2022 09:00";
        slotRequest.end = "09/05/2022 10:00";

        var slot1 = SlotFixtures.create(slotRequest, token)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);

        slotRequest.start = "09/05/2022 10:00";
        slotRequest.end = "09/05/2022 11:00";
        var slot2 = SlotFixtures.create(slotRequest, token)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", SlotResponse.class);

        var slotResponse = SlotFixtures.getAll( token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", SlotResponse.class);

        //assertThat(slotResponse.contains(slot1)).isTrue();

        slotRequest.start = "09/05/2022 09:00";
        slotResponse = SlotFixtures.getByStart(slotRequest, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", SlotResponse.class);
        assertThat(slotResponse.get(0).getStart()).isEqualTo(slotRequest.start);
    }



}