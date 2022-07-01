package com.esgi.framework_JEE.product.domain.repository;


import com.esgi.framework_JEE.product_category.domain.entities.ProductCategory;
import com.esgi.framework_JEE.product.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByProductCategoryOrPriceOrNutriscoreOrName(
            ProductCategory productCategory, Double price, String nutriscore, String name);

    List<Product> getAllByBasketId(int basket_id);

}
