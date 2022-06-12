package com.esgi.framework_JEE.visited_product.web.query;

import com.esgi.framework_JEE.visited_product.domain.repository.VisitedProductRepository;
import com.esgi.framework_JEE.visited_product.domain.entities.VisitedProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitedProductQuery {

    @Autowired
    private VisitedProductRepository visitedProductRepository;

    public Iterable<VisitedProduct> getVisitedProducts() {
        return visitedProductRepository.findAll();
    }

}
