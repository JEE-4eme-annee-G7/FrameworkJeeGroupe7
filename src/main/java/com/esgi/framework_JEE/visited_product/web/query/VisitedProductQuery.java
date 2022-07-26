package com.esgi.framework_JEE.visited_product.web.query;

import com.esgi.framework_JEE.visited_product.domain.repository.VisitedProductRepository;
import com.esgi.framework_JEE.visited_product.domain.entities.VisitedProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitedProductQuery {

    private final VisitedProductRepository visitedProductRepository;

    public VisitedProductQuery(VisitedProductRepository visitedProductRepository) {
        this.visitedProductRepository = visitedProductRepository;
    }

    public List<VisitedProduct> getVisitedProducts(int userId) {
        return visitedProductRepository.findAll()
                .stream()
                .filter(vp -> vp.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

}
