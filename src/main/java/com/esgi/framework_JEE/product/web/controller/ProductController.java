package com.esgi.framework_JEE.product.web.controller;


import com.esgi.framework_JEE.product.web.request.ProductRequest;
import com.esgi.framework_JEE.product.web.command.ProductCommand;
import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.web.query.ProductQuery;
import com.esgi.framework_JEE.product_category.domain.entities.ProductCategory;
import com.esgi.framework_JEE.product_category.query.ProductCategoryQuery;
import com.sun.istack.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")

public class ProductController {
    final
    ProductCommand productCommand;

    final
    ProductQuery productQuery;

    final
    ProductCategoryQuery productCategoryQuery;

    public ProductController(ProductCommand productCommand, ProductQuery productQuery, ProductCategoryQuery productCategoryQuery) {
        this.productCommand = productCommand;
        this.productQuery = productQuery;
        this.productCategoryQuery = productCategoryQuery;
    }

    /**
     * Create - Add a new product
     * @param productRequest An object product
     * @return The product object saved
     */
    @PostMapping(value = "/create", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Product> create(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productCommand.create(productRequest), HttpStatus.CREATED) ;
    }

    /**
     * Read - Get one product
     * @param id The id of the product
     * @return An Product object full filled
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
        Product product = productQuery.getProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK) ;
    }


    /**
     * Read - Get all products
     * @return - An Iterable object of Product full filled
     */
    @GetMapping(value = "/", produces = { MimeTypeUtils.APPLICATION_JSON_VALUE }, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<List<Product>> getAllproducts(
            @RequestBody ProductRequest productRequest)
    {
        System.out.println("test dans postman");
        List<Product> products;
        if (productRequest != null) {
            ProductCategory productCategory = productRequest.productCategoryId != null ? productCategoryQuery.getById(productRequest.productCategoryId) : null;
            products = productQuery.getFilteredProducts(productCategory, productRequest.price, productRequest.nutriscore, productRequest.name);
        } else
            products = productQuery.getProducts();
        return new ResponseEntity<> (products, HttpStatus.OK);
    }

    /**
     * Update - Update an existing product
     * @param productRequest An object product
     * @return The product object updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody ProductRequest productRequest) {
        if(productQuery.getProduct(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productCommand.updateProduct(productRequest, id));
    }

    /**
     * Delete - Delete an product
     * @param id - The id of the product to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id) {
        Product product = productQuery.getProduct(id);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productCommand.deleteProduct(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.NO_CONTENT);
    }


}
