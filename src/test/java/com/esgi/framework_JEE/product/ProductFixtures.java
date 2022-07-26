package com.esgi.framework_JEE.product;

import com.esgi.framework_JEE.Token;
import com.esgi.framework_JEE.product.web.request.ProductRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductFixtures {

    public static Response addProduct(ProductRequest productRequest, Token token){
        return given()
            .contentType(ContentType.JSON)
            .when()
            .body(productRequest)
            .header("Authorization","Bearer "+token.access_token)
            .post("/products/create");
    }

    public static Response getProductById(int productId, Token token){
        return given()
            .contentType(ContentType.JSON)
            .when()
            .header("Authorization","Bearer "+token.access_token)
            .get("/products/" + productId);
    }

    public static Response getAllProducts(ProductRequest productRequest, Token token){
        return given()
            .contentType(ContentType.JSON)
            .when()
            .body(productRequest)
            .header("Authorization","Bearer "+token.access_token)
            .get("/products/");
    }

    public static Response updateProduct(int productId, ProductRequest productRequest, Token token){
        return given()
            .contentType(ContentType.JSON)
            .when()
            .body(productRequest)
            .header("Authorization","Bearer "+token.access_token)
            .put("/products/" + productId);
    }

    public static Response deleteProductById(int productId, Token token){
        return given()
            .contentType(ContentType.JSON)
            .when()
            .header("Authorization","Bearer "+token.access_token)
            .delete("/products/" + productId);
    }

}
