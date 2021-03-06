package com.esgi.framework_JEE.product_category.query;

import com.esgi.framework_JEE.product_category.domain.entities.ProductCategory;
import com.esgi.framework_JEE.product_category.domain.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryQuery {

    final
    ProductCategoryRepository productCategoryRepository;

    public ProductCategoryQuery(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategory getById(int id){
        return productCategoryRepository.findById(id);
    }

    public List<ProductCategory> getAll(){
        return productCategoryRepository.findAll();
    }


}
