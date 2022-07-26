package com.esgi.framework_JEE.visited_product.web.controller;

import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.web.query.ProductQuery;
import com.esgi.framework_JEE.visited_product.web.command.VisitedProductCommand;
import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.query.UserQuery;
import com.esgi.framework_JEE.visited_product.domain.entities.VisitedProduct;
import com.esgi.framework_JEE.visited_product.web.query.VisitedProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")

public class VisitedProductController {
    final
    VisitedProductQuery visitedProductQuery;

    final
    VisitedProductCommand visitedProductCommand;

    final
    ProductQuery productQuery;

    final
    UserQuery userQuery;

    public VisitedProductController(VisitedProductQuery visitedProductQuery, VisitedProductCommand visitedProductCommand, ProductQuery productQuery, UserQuery userQuery) {
        this.visitedProductQuery = visitedProductQuery;
        this.visitedProductCommand = visitedProductCommand;
        this.productQuery = productQuery;
        this.userQuery = userQuery;
    }

    /**
     * Create - Add a new visitedProduct
     * @param user_id An user id
     * @return The visited product object saved
     */
    @PostMapping(value = "/{product_id}/visited", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<VisitedProduct> create(@PathVariable("product_id") int product_id, @RequestBody int user_id) {
        Product product = productQuery.getProduct(product_id);
        User user = userQuery.getById(user_id);

        if(product == null || user == null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity (visitedProductCommand.saveVisitedProduct(product, user), HttpStatus.CREATED);
    }

    /**
     * Read - Get all visited products
     * @return - An Iterable object of Employee full filled
     */
    @GetMapping("/visited")
    @ResponseBody
    public ResponseEntity<List<VisitedProduct>> getAllVisitedProducts(@RequestBody int user_id) {
        return new ResponseEntity<>(visitedProductQuery.getVisitedProducts(user_id), HttpStatus.OK) ;
    }

}
