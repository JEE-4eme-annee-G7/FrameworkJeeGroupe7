package com.esgi.framework_JEE.product_category.validation;

import com.esgi.framework_JEE.product_category.domain.entities.ProductCategory;

import java.util.Objects;

public class ProductCategoryValidation {
    public boolean isValid(ProductCategory productCategory){
        return !productCategory.getName().isBlank() && !Objects.equals(productCategory.getName(), "");
    }
}
