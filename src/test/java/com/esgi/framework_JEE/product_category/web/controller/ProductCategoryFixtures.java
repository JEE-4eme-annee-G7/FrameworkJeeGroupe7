package com.esgi.framework_JEE.product_category.web.controller;

import com.esgi.framework_JEE.Token;
import com.esgi.framework_JEE.product_category.web.request.ProductCategoryRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductCategoryFixtures {
    public static Response create(ProductCategoryRequest productCategoryRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(productCategoryRequest)
                .header("Authorization","Bearer "+token.access_token)
                .post("/product_category/create");
    }

    public static Response getById(int product_categoryId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/product_category/" + product_categoryId);
    }


    public static Response changeProductCategoryName(int product_categoryId, ProductCategoryRequest productCategoryRequest, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .body(productCategoryRequest)
                .header("Authorization","Bearer "+token.access_token)
                .patch("/product_category/" + product_categoryId);
    }


    public static Response deleteById(int product_categoryId, Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .delete("/product_category/" + product_categoryId);
    }

    public static Response getAll(Token token){
        return given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization","Bearer "+token.access_token)
                .get("/product_category/");
    }

}
