package com.esgi.framework_JEE.product;

import com.esgi.framework_JEE.TokenFixture;
import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.web.request.ProductRequest;
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
public class ProductControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void should_create_get_update_product() throws ParseException {
        var adminToken = TokenFixture.adminToken();
        var userToken = TokenFixture.userToken();

        var productRequest = new ProductRequest();
        productRequest.name = "product name for test";
        productRequest.price = 34.5;
        productRequest.nutriscore = "D";
        productRequest.productCategoryId = 1;

        /*
         * create
         */
        var productResponse = ProductFixtures.addProduct(productRequest, adminToken)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", Product.class);

        //System.out.println("***** created product = ***** " + productResponse.getId());

        assertThat(productResponse.getName()).isEqualTo(productRequest.name);
        assertThat(productResponse.getPrice()).isEqualTo(productRequest.price);
        assertThat(productResponse.getNutriscore()).isEqualTo(productRequest.nutriscore);

        var createdProductId = productResponse.getId();

        /*
         * get by id
         */
        var getProductResponse = ProductFixtures.getProductById(createdProductId,userToken)
                .then()
                .statusCode(200);
                //.extract().body().jsonPath().getObject(".", Product.class);

        System.out.println("******** get product = ****** " + getProductResponse);

        /*assertThat(getProductResponse.getName()).isEqualTo(productRequest.name);
        assertThat(productResponse.getPrice()).isEqualTo(productRequest.price);
        assertThat(productResponse.getNutriscore()).isEqualTo(productRequest.nutriscore);
        assertThat(getProductResponse.getId()).isEqualTo(createdProductId); */

    }

    @Test
    public void should_get_all_or_some() {
        var adminToken = TokenFixture.adminToken();
        var userToken = TokenFixture.userToken();

        var productRequest1 = new ProductRequest();
        productRequest1.name = "product1";
        productRequest1.price = 34.5;
        productRequest1.nutriscore = "D";
        productRequest1.productCategoryId = 1;

        var productRequest2 = new ProductRequest();
        productRequest2.name = "product2";
        productRequest2.price = 32.5;
        productRequest2.nutriscore = "E";
        productRequest2.productCategoryId = 1;

        /*
         * create
         */
        var product1 = ProductFixtures.addProduct(productRequest1, adminToken)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", Product.class);

        var product2 = ProductFixtures.addProduct(productRequest2, adminToken)
                .then()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", Product.class);

       /* ProductRequest productRequest = new ProductRequest();
        productRequest.name = "product1";

        var allProducts = ProductFixtures.getAllProducts(null, userToken)
                .then()
                .statusCode(200);

        System.out.println("********** found all products: " + allProducts);
        */
    }
}
