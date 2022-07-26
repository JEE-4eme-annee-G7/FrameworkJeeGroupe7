package com.esgi.framework_JEE.product;

import com.esgi.framework_JEE.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void should_find_empty() {
        var products = productRepository.findAll();
        System.out.println("*******products: ********" + products);
        assertThat(products).isEmpty();
    }

}
