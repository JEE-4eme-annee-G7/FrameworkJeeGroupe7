package com.esgi.framework_JEE.product.web.query;

import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.domain.repository.ProductRepository;
import com.esgi.framework_JEE.product.web.services.Levenshtein;
import com.esgi.framework_JEE.product_category.domain.entities.ProductCategory;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductQuery {

    private final ProductRepository productRepository;
    private final Levenshtein levenshtein;

    public ProductQuery(ProductRepository productRepository, Levenshtein levenshtein) {
        this.productRepository = productRepository;
        this.levenshtein = levenshtein;
    }

    public Product getProduct(int id) {
        return productRepository.findById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getFilteredProducts(
            ProductCategory productCategory, Double price, String nutriscore, String name
    ){
        List<Product> allProducts = productRepository.findAll();
        if(productCategory == null && price == null && nutriscore == null && name == null){
            return allProducts;
        }
        List<Product> foundProducts = allProducts.stream().filter(product -> name != null && name.equals("") && levenshtein.calculate(name.toUpperCase(), product.getName().toUpperCase()) < 3
                || (productCategory != null && product.getProductCategory().equals(productCategory))
                || (price != null && product.getPrice() == price)
                || (nutriscore != null && product.getNutriscore() == nutriscore)).collect(Collectors.toList());

        return foundProducts;
    }

    public List<Product> getProductsByBasketId(int basket_id){
        return productRepository.getAllByBasketId(basket_id);
    }
}
