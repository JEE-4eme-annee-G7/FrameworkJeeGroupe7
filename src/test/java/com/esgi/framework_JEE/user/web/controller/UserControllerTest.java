package com.esgi.framework_JEE.user.web.controller;

import com.esgi.framework_JEE.TestFixtures;
import com.esgi.framework_JEE.Token;
import com.esgi.framework_JEE.TokenFixture;
import com.esgi.framework_JEE.user.web.request.UserRequest;
import com.esgi.framework_JEE.user.web.response.UserResponse;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    int port;

    public UserRequest user1 = new UserRequest();
    public UserRequest user2 = new UserRequest();
    public UserRequest user3 = new UserRequest();

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());


        user1.email= TestFixtures.randomEmail();
        user1.firstname = "lucas";
        user1.lastname = "jehanno";
        user1.password = "securite !";

        user2.email = TestFixtures.randomEmail();
        user2.firstname = "kelyan";
        user2.lastname = "bervin";
        user2.password = "j aime les pates";


        user3.email = TestFixtures.randomEmail();
        user3.firstname = "halimatou";
        user3.lastname = "bah";
        user3.password = "bahbibel";
    }

    @Test
    public void should_create_get_update_and_delete_user(){

        /*
         * create
         */



        var userResponse = UserFixture.create(user1)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", UserResponse.class);



        UserFixture.create(user1)
                .then()
                .statusCode(400);

        var id = userResponse.getId();
        var token = TokenFixture.getToken(user1);

        assertThat(userResponse.getEmail()).isEqualTo(user1.email);
        assertThat(userResponse.getFirstname()).isEqualTo(user1.firstname);
        assertThat(userResponse.getLastname()).isEqualTo(user1.lastname);


        /*
         * get by id
         */
        var getUserResponse = UserFixture.getById(id, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", UserResponse.class);


        assertThat(userResponse.getEmail()).isEqualTo(user1.email);
        assertThat(userResponse.getFirstname()).isEqualTo(user1.firstname);
        assertThat(userResponse.getLastname()).isEqualTo(user1.lastname);

        assertThat(getUserResponse.getId()).isEqualTo(id);


        /*
         * update (change lastname)
         */
        user1.lastname = "";
        UserFixture.changeLastname(id,user1, token)
                .then()
                .statusCode(400);

        user1.lastname = "JEHANNO";
        var updatedUserResponse = UserFixture.changeLastname(id,user1, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", UserResponse.class);

        assertThat(updatedUserResponse.getEmail()).isEqualTo(user1.email);
        assertThat(updatedUserResponse.getFirstname()).isEqualTo(user1.firstname);
        assertThat(updatedUserResponse.getLastname()).isEqualTo(user1.lastname);
        assertThat(updatedUserResponse.getId()).isEqualTo(id);

        /*
         * update (change firstname)
         */
        user1.firstname = "";
        UserFixture.changeFirstname(id,user1, token)
                .then()
                .statusCode(400);

        user1.firstname = "LUCAS";
        var updatedFirstnameUserResponse = UserFixture.changeFirstname(id,user1, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", UserResponse.class);

        assertThat(updatedFirstnameUserResponse.getEmail()).isEqualTo(user1.email);
        assertThat(updatedFirstnameUserResponse.getFirstname()).isEqualTo(user1.firstname);
        assertThat(updatedFirstnameUserResponse.getLastname()).isEqualTo(user1.lastname);
        assertThat(updatedFirstnameUserResponse.getId()).isEqualTo(id);

        /*
         * update (change password)
         */
        user1.password = "";
        UserFixture.changePassword(id,user1, token)
                .then()
                .statusCode(400);

        user1.password = "coucou c est mon nouveau mdp";
        var updatedPasswordUserResponse = UserFixture.changePassword(id,user1, token)
                .then()
                .statusCode(200);


        /*
         * CHANGE EMAIL
         */

        user1.email = TestFixtures.randomEmail();
        var updatedEmailUserResponse = UserFixture.changeEmail(id,user1, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", UserResponse.class);

        assertThat(updatedEmailUserResponse.getEmail()).isEqualTo(user1.email);
        assertThat(updatedEmailUserResponse.getFirstname()).isEqualTo(user1.firstname);
        assertThat(updatedEmailUserResponse.getLastname()).isEqualTo(user1.lastname);
        assertThat(updatedEmailUserResponse.getId()).isEqualTo(id);

        /*
         * delete
         */
        var deleteUserResponse = UserFixture.deleteById(id, token)
                .then()
                .statusCode(202)
                .extract().body().asString();


        assertThat(deleteUserResponse).isEqualTo("User " + id + " deleted");

        deleteUserResponse = UserFixture.deleteById(id, token)
                .then()
                .statusCode(400)
                .extract().body().asString();


        assertThat(deleteUserResponse).isEqualTo("User " + id + " not exist");
    }

    @Test
    public void should_return_bad_request() {

        user1.email = "";

        var token = TokenFixture.userToken();

        var response = UserFixture.create(user1)
                .then()
                .statusCode(400)
                .extract().body().asString();
        assertThat(response).isEqualTo("");

        /*
         * get by id
         */
        var response2 = UserFixture.getById(0, token)
                .then()
                .statusCode(400)
                .extract().body().asString();

        assertThat(response2).isEqualTo("");

        var response3 = UserFixture.changeEmail(0,new UserRequest(), token)
                .then()
                .statusCode(400)
                .extract().body().asString();

        assertThat(response3).isEqualTo("");

    }

    @Test
    public void should_test_get_by_email(){
        var created = UserFixture.create(user3)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", UserResponse.class);
        var token = TokenFixture.getToken(user3);
        var get = UserFixture.getByEmail(user3.email, token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", UserResponse.class);
        assertThat(created.email).isEqualTo(get.email);
        assertThat(created.firstname).isEqualTo(get.firstname);
        assertThat(created.lastname).isEqualTo(get.lastname);
        assertThat(created.id).isEqualTo(get.id);


        UserFixture.getByEmail("email qui existe pas et qui n est meme aps valide :')", token)
                .then()
                .statusCode(404);


    }

    @Test
    public void should_test_login_route(){
        UserFixture.create(user3)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", UserResponse.class);

        TokenFixture.login(user3)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(".", Token.class);

        user3.email = "email qui n est pas le bon";
        user3.password = "mdp qui n est pas le bon non plus";

        TokenFixture.login(user3)
                .then()
                .statusCode(403)
                .extract().body().asString();
    }

    @Test
    public void should_get_all() {
        user1.email= TestFixtures.randomEmail();
        user1.firstname = "fsdsd";
        user1.lastname = "sdf";
        user1.password = "sdqfdqsf !";
        UserFixture.create(user1)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", UserResponse.class);

        var token = TokenFixture.getToken(user1);

        var slotResponse = UserFixture.getAll(token)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", UserResponse.class);

        assertThat(slotResponse).isNotEmpty();
    }


}