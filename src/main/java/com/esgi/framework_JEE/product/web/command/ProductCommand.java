package com.esgi.framework_JEE.product.web.command;

import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.domain.repository.ProductRepository;
import com.esgi.framework_JEE.product.web.query.ProductQuery;
import com.esgi.framework_JEE.product.web.request.ProductRequest;
import com.esgi.framework_JEE.product.web.services.NutriscoreApiService;
import com.esgi.framework_JEE.product_category.query.ProductCategoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCommand {

    final ProductRepository productRepository;

    final ProductQuery productQuery;

    final ProductCategoryQuery productCategoryQuery;

    final NutriscoreApiService nutriscoreApiService;

    public ProductCommand(ProductRepository productRepository, ProductQuery productQuery, ProductCategoryQuery productCategoryQuery, NutriscoreApiService nutriscoreApiService) {
        this.productRepository = productRepository;
        this.productQuery = productQuery;
        this.productCategoryQuery = productCategoryQuery;
        this.nutriscoreApiService = nutriscoreApiService;
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Product create(ProductRequest productRequest){
        var product = new Product();
        product.setName(productRequest.name);
        product.setPrice(productRequest.price);
        product.setProductCategory(productCategoryQuery.getById(productRequest.productCategoryId));
        //product.setNutriscore((product.getCategory().name == "Alimentaire") ? (nutriscoreService.getNutriscore) : null);
        product.setNutriscore(productRequest.nutriscore);
        return saveProduct(product);
    }

    public Product updateProduct(ProductRequest productRequest, int id){
        Product productToUpdate = productQuery.getProduct(id);
        if(productToUpdate != null){
            Product product = productToUpdate;
            String name = productRequest.name;
            if(name != null) {
                product.setName(name);
            }
            Double price = productRequest.price;
            if(price != null) {
                product.setPrice(price);
            }
            Integer productCategoryId = productRequest.productCategoryId;
            if(productCategoryId != null){
                product.setProductCategory(productCategoryQuery.getById(productCategoryId));
            }
            String nutriscore = productRequest.nutriscore;
            if(nutriscore != null){
                product.setNutriscore(nutriscore);
            }
            //return this.saveProduct(product);
            return productToUpdate;
        }
        return null;
    }

    public void deleteProduct(int id) {
        /*Optional<Product> productToDelete = productQuery.getProduct(id);
        productToDelete.ifPresent(
            product -> productRepository.deleteById(product.getId())
        );*/

        productRepository.deleteById(id);
    }
}
