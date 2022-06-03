package com.esgi.framework_JEE.use_case.product.domain.repository;

import com.esgi.framework_JEE.use_case.product.domain.entities.Product;
import com.esgi.framework_JEE.use_case.product_category.domain.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByProductCategoryOrPriceOrNutriscoreOrName(
            ProductCategory productCategory, Double price, String nutriscore, String name);

}
